package com.example.eye.ui.notification.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eye.R
import com.example.eye.extension.start
import com.example.eye.ui.common.ui.BaseFragment
import com.example.eye.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_notification_login_tips.*

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 20:29
 *
 * @Description:
 *
 */
class InboxFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_notification_login_tips,
            container,
            false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvLogin.setOnClickListener { start<LoginActivity>(activity) }
    }
    companion object {

        fun newInstance() = InboxFragment()
    }
}