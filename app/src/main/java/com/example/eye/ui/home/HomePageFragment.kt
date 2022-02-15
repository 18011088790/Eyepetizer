package com.example.eye.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eye.R
import com.example.eye.event.MessageEvent
import com.example.eye.event.RefreshEvent
import com.example.eye.event.SwitchPagesEvent
import com.example.eye.logic.model.TabEntity
import com.example.eye.ui.common.ui.BaseViewPagerFragment
import com.example.eye.ui.home.commend.CommendFragment
import com.example.eye.ui.home.daily.DailyFragment
import com.example.eye.ui.home.discovery.DiscoveryFragment
import com.example.eye.util.GlobalUtil
import com.flyco.tablayout.listener.CustomTabEntity
import org.greenrobot.eventbus.EventBus

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 2:49
 *
 * @Description:
 *
 */
class HomePageFragment : BaseViewPagerFragment() {

    override val createTitles = ArrayList<CustomTabEntity>().apply {
        add(TabEntity(GlobalUtil.getString(R.string.discovery)))
        add(TabEntity(GlobalUtil.getString(R.string.commend)))
        add(TabEntity(GlobalUtil.getString(R.string.daily)))

    }
    override val createFragments: Array<Fragment> = arrayOf(
        DiscoveryFragment.newInstance(), CommendFragment.newInstance(), DailyFragment.newInstance()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return super.onCreateView(inflater.inflate(R.layout.layout_main_container, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewPager?.currentItem=1
    }

    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && this::class.java == messageEvent.activityClass) {
            when (viewPager?.currentItem) {
                0 -> EventBus.getDefault().post(RefreshEvent(DiscoveryFragment::class.java))
                1 -> EventBus.getDefault().post(RefreshEvent(CommendFragment::class.java))
                2 -> EventBus.getDefault().post(RefreshEvent(DailyFragment::class.java))
                else -> {
                }
            }
        } else if (messageEvent is SwitchPagesEvent) {
            when (messageEvent.activityClass) {
                DiscoveryFragment::class.java -> viewPager?.currentItem = 0
                CommendFragment::class.java -> viewPager?.currentItem = 1
                DailyFragment::class.java -> viewPager?.currentItem = 2
                else -> {
                }
            }
        }
    }

    companion object {
        fun newInstance() = HomePageFragment()
    }
}