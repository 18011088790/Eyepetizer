package com.example.eye.ui

import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.example.eye.R
import com.example.eye.extension.edit
import com.example.eye.extension.sharedPreferences
import com.example.eye.extension.start
import com.example.eye.ui.common.ui.BaseActivity
import com.example.eye.util.GlobalUtil
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.layout_auto_play_video_player.*
import kotlinx.android.synthetic.main.layout_main_page_title_bar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.jar.Manifest

class SplashActivity : BaseActivity() {

    private val job by lazy { Job() }

    // 闪屏时间 3*1000L ms
    private val splashDuration = 3 * 1000L

    // 透明动画
    private val alphaAnimation by lazy {
        AlphaAnimation(0.5f, 1.0f).apply {
            duration = splashDuration
            fillAfter = true
        }
    }

    //旋转动画
    private val scaleAnimation by lazy {
        ScaleAnimation(1f,
            1.05f,
            1f,
            1.05f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f).apply {
            duration = splashDuration
            fillAfter = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun setupViews() {
        super.setupViews()
        ivSlogan.startAnimation(alphaAnimation)
        ivSplashPicture.startAnimation(scaleAnimation)
        CoroutineScope(job).launch {
            delay(splashDuration)
            start<MainActivity>(this@SplashActivity)
            finish()
        }
        isFirstEntryApp = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
        requestWriteExternalStoragePermission()
    }

    private fun requestWriteExternalStoragePermission() {
        PermissionX.init(this).permissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .onExplainRequestReason { scope, deniedList ->
                val message = GlobalUtil.getString(R.string.request_permission_picture_processing)
                scope.showRequestReasonDialog(deniedList,
                    message,
                    GlobalUtil.getString(R.string.ok),
                    GlobalUtil.getString(R.string.cancel))
            }
            .onForwardToSettings { scope, deniedList ->
                val message = GlobalUtil.getString(R.string.request_permission_picture_processing)
                scope.showForwardToSettingsDialog(deniedList,
                    message,
                    GlobalUtil.getString(R.string.settings),
                    GlobalUtil.getString(R.string.cancel))
            }
            .request { _, _, _ ->
                requestReadPhoneStatePermission()
            }
    }

    private fun requestReadPhoneStatePermission() {
        PermissionX.init(this).permissions(android.Manifest.permission.READ_PHONE_STATE)
            .onExplainRequestReason { scope, deniedList ->
                val message = GlobalUtil.getString(R.string.request_permission_access_phone_info)
                scope.showRequestReasonDialog(deniedList,
                    message,
                    GlobalUtil.getString(R.string.ok),
                    GlobalUtil.getString(R.string.cancel))
            }
            .onForwardToSettings { scope, deniedList ->
                val message = GlobalUtil.getString(R.string.request_permission_access_phone_info)
                scope.showForwardToSettingsDialog(deniedList,
                    message,
                    GlobalUtil.getString(R.string.settings),
                    GlobalUtil.getString(R.string.cancel))
            }
            .request { _, _, _ ->
                setContentView(R.layout.activity_splash)
            }
    }

    companion object {
        /**
         * 是否首次进入APP应用
         */
        var isFirstEntryApp: Boolean
            get() = sharedPreferences.getBoolean("is_first_entry_app", true)
            set(value) = sharedPreferences.edit { putBoolean("is_first_entry_app", value) }
    }
}