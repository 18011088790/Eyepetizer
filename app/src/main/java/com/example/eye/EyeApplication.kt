package com.example.eye

import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import com.example.eye.extension.preCreateSession
import com.example.eye.ui.SplashActivity
import com.example.eye.ui.common.ui.WebViewActivity
import com.example.eye.ui.common.view.NoStatusFooter
import com.example.eye.util.DialogAppraiseTipsWorker
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * @Author:gxs
 *
 * @Date 2022/2/10 20:35
 *
 * @Description:
 *
 */
class EyeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this

        IjkPlayerManager.setLogLevel(if (BuildConfig.DEBUG) IjkMediaPlayer.IJK_LOG_WARN else IjkMediaPlayer.IJK_LOG_SILENT)
        WebViewActivity.DEFAULT_URL.preCreateSession()
        if (!SplashActivity.isFirstEntryApp && DialogAppraiseTipsWorker.isNeedShowDialog) {
            WorkManager.getInstance(this).enqueue(DialogAppraiseTipsWorker.showDialogWorkRequest)
        }
    }

    init {
        SmartRefreshLayout.setDefaultRefreshInitializer { _, layout ->
            layout.setEnableLoadMore(true)
            layout.setEnableLoadMoreWhenContentNotFull(true)
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setEnableHeaderTranslationContent(true)
            MaterialHeader(context).setColorSchemeResources(R.color.blue,R.color.blue,R.color.blue)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator{ context,layout->
            layout.setEnableFooterFollowWhenNoMoreData(true)
            layout.setEnableFooterTranslationContent(true)
            layout.setFooterHeight(153f)
            layout.setFooterTriggerRate(0.6f)

            NoStatusFooter.REFRESH_FOOTER_NOTHING="- The End -"
            NoStatusFooter(context).apply {
                setAccentColorId(R.color.colorTextPrimary)
                setTextTitleSize(16f)
            }
        }
    }

    companion object {
        lateinit var context: Context
    }
}