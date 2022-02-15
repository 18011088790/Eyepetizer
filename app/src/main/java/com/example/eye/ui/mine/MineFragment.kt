package com.example.eye.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.multidex.BuildConfig
import com.example.eye.Const
import com.example.eye.R
import com.example.eye.extension.setOnClickListener
import com.example.eye.extension.showToast
import com.example.eye.extension.start
import com.example.eye.ui.common.ui.BaseFragment
import com.example.eye.ui.common.ui.BaseViewPagerFragment
import com.example.eye.ui.common.ui.WebViewActivity
import com.example.eye.ui.login.LoginActivity
import com.example.eye.ui.setting.SettingActivity
import com.example.eye.util.GlobalUtil
import com.flyco.tablayout.listener.CustomTabEntity
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 2:47
 *
 * @Description:
 *
 */
class MineFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_mine, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvVersionNumber.text = String.format(GlobalUtil.getString(R.string.version_show),
            GlobalUtil.eyepetizerVersionName)



        setOnClickListener(
            ivMore,
            ivAvatar,
            tvLoginTips,
            tvFavorites,
            tvCache,
            tvFollow,
            tvWatchRecord,
            tvNotificationToggle,
            tvMyBadge,
            tvFeedback,
            tvContribute,
            tvVersionNumber, rootView, llScrollViewContent
        ) {
            when (this) {
                ivMore -> {
                    start<SettingActivity>(activity)
                }
                ivAvatar, tvLoginTips, tvFavorites, tvCache, tvWatchRecord, tvNotificationToggle, tvMyBadge -> {
                    start<LoginActivity>(activity)
                }
                tvContribute -> {
                    WebViewActivity.start(activity,
                        WebViewActivity.DEFAULT_TITLE,
                        Const.Url.AUTHOR_OPEN,
                        false,
                        false)
                }
                tvFeedback -> {
                    WebViewActivity.start(activity,
                        WebViewActivity.DEFAULT_TITLE,
                        WebViewActivity.DEFAULT_URL,
                        true,
                        false,
                        WebViewActivity.MODE_SONIC_WITH_OFFLINE_CACHE)
                }
                tvVersionNumber -> {
                    WebViewActivity.start(activity,
                        WebViewActivity.DEFAULT_TITLE,
                        WebViewActivity.DEFAULT_URL,
                        true,
                        false,
                        WebViewActivity.MODE_SONIC_WITH_OFFLINE_CACHE)
                }
                this@MineFragment.rootView, llScrollViewContent -> {
                }
                else -> {
                }
            }
        }
        tvVersionNumber.setOnLongClickListener {
            String.format(GlobalUtil.getString(R.string.build_type), BuildConfig.BUILD_TYPE).showToast()
            true
        }
    }
}