package com.example.eye.ui.notification.push

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eye.R
import com.example.eye.event.MessageEvent
import com.example.eye.event.RefreshEvent
import com.example.eye.extension.showToast
import com.example.eye.ui.common.ui.BaseFragment
import com.example.eye.util.GlobalUtil
import com.example.eye.util.InjectorUtil
import com.example.eye.util.ResponseHandler
import com.scwang.smart.refresh.layout.constant.RefreshState
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 20:50
 *
 * @Description:推送
 *
 */
class PushFragment:BaseFragment() {

    private val viewModel by lazy { ViewModelProvider(this, InjectorUtil.getPushViewModelFactory()).get(PushViewModel::class.java) }

    private lateinit var adapter: PushAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_refresh_layout, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = PushAdapter(this, viewModel.dataList)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = null
        refreshLayout.setOnRefreshListener { viewModel.onRefresh() }
        refreshLayout.setOnLoadMoreListener { viewModel.onLoadMore() }
        observe()
    }

    override fun loadDataOnce() {
        super.loadDataOnce()
        startLoading()
    }

    override fun startLoading() {
        super.startLoading()
        viewModel.onRefresh()
    }

    @CallSuper
    override fun loadFailed(msg: String?) {
        super.loadFailed(msg)
        showLoadErrorView(msg ?: GlobalUtil.getString(R.string.unknown_error)) { startLoading() }
    }

    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && javaClass == messageEvent.activityClass) {
            refreshLayout.autoRefresh()
            if (recyclerView.adapter?.itemCount ?: 0 > 0) recyclerView.scrollToPosition(0)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observe() {
        viewModel.dataListLiveData.observe(viewLifecycleOwner, Observer { result ->
            val response = result.getOrNull()
            if (response == null) {
                ResponseHandler.getFailureTips(result.exceptionOrNull()).let { if (viewModel.dataList.isNullOrEmpty()) loadFailed(it) else it.showToast() }
                refreshLayout.closeHeaderOrFooter()
                return@Observer
            }
            loadFinished()
            viewModel.nextPageUrl = response.nextPageUrl
            if (response.itemList.isNullOrEmpty() && viewModel.dataList.isEmpty()) {
                //首次进入页面时，获取数据条目为0时处理。
                refreshLayout.closeHeaderOrFooter()
                return@Observer
            }
            if (response.itemList.isNullOrEmpty() && viewModel.dataList.isNotEmpty()) {
                //上拉加载数据时，返回数据条目为0时处理。
                refreshLayout.finishLoadMoreWithNoMoreData()
                return@Observer
            }
            when (refreshLayout.state) {
                RefreshState.None, RefreshState.Refreshing -> {
                    viewModel.dataList.clear()
                    viewModel.dataList.addAll(response.itemList)
                    adapter.notifyDataSetChanged()
                }
                RefreshState.Loading -> {
                    val itemCount = viewModel.dataList.size
                    viewModel.dataList.addAll(response.itemList)
                    adapter.notifyItemRangeInserted(itemCount, response.itemList.size)
                }
                else -> {
                }
            }
            if (response.nextPageUrl.isNullOrEmpty()) {
                refreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                refreshLayout.closeHeaderOrFooter()
            }
        })
    }

    companion object {

        fun newInstance() = PushFragment()
    }

}