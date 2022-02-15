package com.example.eye.ui.community.follow

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
import com.example.eye.extension.gone
import com.example.eye.extension.showToast
import com.example.eye.extension.visible
import com.example.eye.ui.common.callback.AutoPlayScrollListener
import com.example.eye.ui.common.ui.BaseFragment
import com.example.eye.util.GlobalUtil
import com.example.eye.util.InjectorUtil
import com.example.eye.util.ResponseHandler
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 20:09
 *
 * @Description:社区-关注列表界面。
 *
 */
class FollowFragment:BaseFragment() {
    private val viewModel by lazy { ViewModelProvider(this, InjectorUtil.getFollowViewModelFactory()).get(FollowViewModel::class.java) }

    private lateinit var adapter: FollowAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_refresh_layout, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = FollowAdapter(this, viewModel.dataList)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = null
        recyclerView.addOnScrollListener(AutoPlayScrollListener(R.id.videoPlayer, AutoPlayScrollListener.PLAY_RANGE_TOP, AutoPlayScrollListener.PLAY_RANGE_BOTTOM))
        refreshLayout.setOnRefreshListener { viewModel.onRefresh() }
        refreshLayout.setOnLoadMoreListener { viewModel.onLoadMore() }
        refreshLayout.gone()
        observe()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    override fun loadDataOnce() {
        super.loadDataOnce()
        startLoading()
    }

    override fun startLoading() {
        super.startLoading()
        viewModel.onRefresh()
        refreshLayout.gone()
    }

    override fun loadFinished() {
        super.loadFinished()
        refreshLayout.visible()
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
                refreshLayout.closeHeaderOrFooter()
                return@Observer
            }
            if (response.itemList.isNullOrEmpty() && viewModel.dataList.isNotEmpty()) {
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

        fun newInstance() = FollowFragment()
    }
}