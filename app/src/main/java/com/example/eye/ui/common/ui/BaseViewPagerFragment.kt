package com.example.eye.ui.common.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.eye.R
import com.example.eye.extension.setOnClickListener
import com.example.eye.extension.showToast
import com.example.eye.ui.search.SearchFragment
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.layout_main_page_title_bar.*

/**
 * @Author:gxs
 *
 * @Date 2022/2/10 22:17
 *
 * @Description:Fragment基类，适用场景：页面含有ViewPager+TabLayout的界面。
 *
 */
abstract class BaseViewPagerFragment:BaseFragment() {

    protected var viewPager: ViewPager2? = null

    protected var tabLayout: CommonTabLayout? = null

    protected var pageChangeCallback: PageChangeCallback? = null

    protected val adapter: VpAdapter by lazy { VpAdapter(requireActivity()).apply { addFragments(createFragments) } }

    protected var offscreenPageLimit = 1

    abstract val createTitles: ArrayList<CustomTabEntity>

    abstract val createFragments: Array<Fragment>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        pageChangeCallback?.run { viewPager?.unregisterOnPageChangeCallback(this) }
        pageChangeCallback = null
    }

    open fun setupViews() {
        initVeiwPager()
        setOnClickListener(ivCalendar, ivSearch) {
            if (this == ivCalendar) {
                R.string.currently_not_supported.showToast()
            } else if (this == ivSearch) {
                SearchFragment.switchFragment(activity)
            }
        }
    }

    protected fun initVeiwPager() {
        viewPager = rootView?.findViewById(R.id.viewPager)
        tabLayout = rootView?.findViewById(R.id.tabLayout)

        viewPager?.offscreenPageLimit = offscreenPageLimit
        viewPager?.adapter = adapter
        tabLayout?.setTabData(createTitles)
        tabLayout?.setOnTabSelectListener(object : OnTabSelectListener {

            override fun onTabSelect(position: Int) {
                viewPager?.currentItem = position
            }

            override fun onTabReselect(position: Int) {

            }
        })
        pageChangeCallback = PageChangeCallback()
        viewPager?.registerOnPageChangeCallback(pageChangeCallback!!)
    }

    inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            tabLayout?.currentTab = position
        }
    }

    inner class VpAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

        private val fragments = mutableListOf<Fragment>()

        fun addFragments(fragment: Array<Fragment>) {
            fragments.addAll(fragment)
        }

        override fun getItemCount() = fragments.size

        override fun createFragment(position: Int) = fragments[position]
    }
}