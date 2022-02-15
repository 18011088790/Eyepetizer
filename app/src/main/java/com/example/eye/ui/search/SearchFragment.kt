package com.example.eye.ui.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eye.R
import com.example.eye.extension.setDrawable
import com.example.eye.extension.showToast
import com.example.eye.extension.visibleAlphaAnimation
import com.example.eye.ui.common.ui.BaseActivity
import com.example.eye.ui.common.ui.BaseFragment
import com.example.eye.util.GlobalUtil
import com.example.eye.util.InjectorUtil
import kotlinx.android.synthetic.main.fragment_search.*
import java.lang.Exception

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 21:21
 *
 * @Description:
 *
 */
class SearchFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProvider(this, InjectorUtil.getSearchViewModelFactory())
            .get(SearchViewModel::class.java)
    }
    private lateinit var adapter: HotSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_search, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        llSearch.visibleAlphaAnimation(500)
        etQuery.setDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_search_gray_17dp),
            14f,
            14f)

        etQuery.setOnEditorActionListener(EditorActionListener())
        tvCancel.setOnClickListener {
            hideSoftKeyBoard()
            removeFragment(activity, this)
        }
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        adapter = HotSearchAdapter(this, viewModel.dataList)
        recyclerView.adapter = adapter
        viewModel.onRefresh()
        observe()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideSoftKeyBoard()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(activity, R.anim.anl_push_up_in)
        } else {
            AnimationUtils.loadAnimation(activity, R.anim.anl_push_top_out)
        }
    }

    private fun observe() {
        viewModel.dataListLiveData.observe(viewLifecycleOwner, Observer { result ->
            etQuery.showSoftKeyboard()
            val response = result.getOrNull()
            if (response == null) {
                result.exceptionOrNull()?.printStackTrace()
                return@Observer
            }
            if (response.isNullOrEmpty()&& viewModel.dataList.isEmpty()){
                return@Observer
            }
            if (response.isNullOrEmpty()&& viewModel.dataList.isNotEmpty()){
                return@Observer
            }
            viewModel.dataList.clear()
            viewModel.dataList.addAll(response)
            adapter.notifyDataSetChanged()
        })
    }

    /**
     * 影藏软键盘
     */
    private fun hideSoftKeyBoard() {
        try {
            activity.currentFocus?.run {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch (e: Exception) {
            Log.w(TAG, "hideSoftKeyBoard: ${e.message}", e)
        }
    }


    /**
     * 拉起软键盘
     */
    private fun View.showSoftKeyboard() {
        try {
            this.isFocusable = true
            this.isFocusableInTouchMode = true
            this.requestFocus()
            val manager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(this, 0)
        } catch (e: Exception) {
            Log.w(TAG, "showSoftKeyboard: ${e.message}", e)
        }
    }

    inner class EditorActionListener : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if (etQuery.text.toString().isEmpty()) {
                    GlobalUtil.getString(R.string.input_keywords_tips).showToast()
                    return false
                }
                R.string.currently_not_supported.showToast()
                return true
            }
            return true
        }
    }

    companion object {

        /**
         * 切换Fragment，会加入回退栈。
         */
        fun switchFragment(activity: Activity) {
            (activity as BaseActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, SearchFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        /**
         * 先移除Fragment，并将Fragment从堆栈弹出。
         */
        fun removeFragment(activity: Activity, fragment: Fragment) {
            (activity as BaseActivity).supportFragmentManager.run {
                beginTransaction().remove(fragment).commitAllowingStateLoss()
                popBackStack()
            }
        }
    }
}