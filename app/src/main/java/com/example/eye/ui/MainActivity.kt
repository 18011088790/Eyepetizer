package com.example.eye.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.eye.R
import com.example.eye.event.MessageEvent
import com.example.eye.event.RefreshEvent
import com.example.eye.event.SwitchPagesEvent
import com.example.eye.extension.logD
import com.example.eye.extension.showToast
import com.example.eye.ui.common.ui.BaseActivity
import com.example.eye.ui.community.CommunityFragment
import com.example.eye.ui.community.commend.CommendFragment
import com.example.eye.ui.home.HomePageFragment
import com.example.eye.ui.mine.MineFragment
import com.example.eye.ui.notification.NotificationFragment
import com.example.eye.util.DialogAppraiseTipsWorker
import com.example.eye.util.GlobalUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.layout_bottom_navigation_bar.*
import org.greenrobot.eventbus.EventBus

class MainActivity : BaseActivity() {

    private var backPressTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun setupViews() {
        observe()
        val navBottom: BottomNavigationView = findViewById(R.id.nav_bottom)
        val navContainer = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navBottom, navContainer)


        navBottom.setOnNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener,
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                return when (item.itemId) {
                    R.id.navigation_home -> {
                        return if(item.isChecked){
                            notificationUiRefresh(0)
                            true
                        }else{
                            onNavDestinationSelected(item, navContainer)
                        }
                    }
                    R.id.navigation_community -> {
                        return if(item.isChecked){
                            notificationUiRefresh(1)
                            true
                        }else{
                            onNavDestinationSelected(item, navContainer)
                        }
                    }
                    R.id.navigation_notifications -> {
                        return if(item.isChecked){
                            notificationUiRefresh(2)
                            true
                        }else{
                            onNavDestinationSelected(item, navContainer)
                        }
                    }
                    R.id.navigation_mine -> {
                        return if(item.isChecked){
                            notificationUiRefresh(3)
                            true
                        }else{
                            onNavDestinationSelected(item, navContainer)
                        }
                    }
                    else -> {
                        return onNavDestinationSelected(item, navContainer)
                    }
                }
            }

        })

    }


    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        when {
            messageEvent is SwitchPagesEvent && CommendFragment::class.java == messageEvent.activityClass -> {
                btnCommunity.performClick()
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            processBackPressed()
        }
    }

    private fun processBackPressed() {
        val now = System.currentTimeMillis()
        if (now - backPressTime > 2000) {
            String.format(GlobalUtil.getString(R.string.press_again_to_exit), GlobalUtil.appName)
                .showToast()
            backPressTime = now
        } else {
            super.onBackPressed()
        }
    }

    private fun notificationUiRefresh(selectionIndex: Int) {
        when (selectionIndex) {
            0 -> {
                EventBus.getDefault()
                    .post(RefreshEvent(HomePageFragment::class.java))
            }
            1 -> {
                EventBus.getDefault()
                    .post(RefreshEvent(CommunityFragment::class.java))
            }
            2 -> {
                EventBus.getDefault()
                    .post(RefreshEvent(NotificationFragment::class.java))
            }
            3 -> {
                EventBus.getDefault()
                    .post(RefreshEvent(MineFragment::class.java))
            }
        }
    }

    private fun observe() {
        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(DialogAppraiseTipsWorker.showDialogWorkRequest.id)
            .observe(this, Observer { workInfo ->
                logD(TAG, "observe: workInfo.state = ${workInfo.state}")
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    WorkManager.getInstance(this).cancelAllWork()
                } else if (workInfo.state == WorkInfo.State.RUNNING) {
                    if (isActive) {
                        DialogAppraiseTipsWorker.showDialog(this)
                        WorkManager.getInstance(this).cancelAllWork()
                    }
                }
            })
    }

}