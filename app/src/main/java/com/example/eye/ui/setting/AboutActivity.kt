package com.example.eye.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import com.example.eye.R
import com.example.eye.ui.common.ui.BaseActivity
import com.example.eye.ui.common.ui.WebViewActivity
import com.example.eye.ui.common.ui.WebViewActivity.Companion.DEFAULT_TITLE
import com.example.eye.ui.common.ui.WebViewActivity.Companion.DEFAULT_URL
import com.example.eye.util.GlobalUtil
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.layout_title_bar.*

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 22:29
 *
 * @Description:关于界面
 *
 */
class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    override fun setupViews() {
        super.setupViews()
        tvTitle.text = GlobalUtil.getString(R.string.about)
        val version = "${GlobalUtil.getString(R.string.version)} ${GlobalUtil.appVersionName}"
        tvAboutVersion.text = version
        tvThanksTips.text = String.format(GlobalUtil.getString(R.string.thanks_to), GlobalUtil.appName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvOpenSourceList.text = Html.fromHtml("<u>" + GlobalUtil.getString(R.string.open_source_project_list) + "</u>", 0)
        } else {
            tvOpenSourceList.text = Html.fromHtml("<u>" + GlobalUtil.getString(R.string.open_source_project_list) + "</u>")
        }
        ivLogo.setImageDrawable(GlobalUtil.getAppIcon())

        btnEncourage.setOnClickListener {
//            MobclickAgent.onEvent(activity, Const.Mobclick.EVENT5)
            WebViewActivity.start(this, DEFAULT_TITLE, DEFAULT_URL, true)
        }
        tvOpenSourceList.setOnClickListener {
            OpenSourceProjectsActivity.start(this@AboutActivity)
        }
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }
}