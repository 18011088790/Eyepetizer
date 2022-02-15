package com.example.eye.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.eye.EyeApplication
import com.example.eye.ui.common.ui.ShareDialogFragment
import com.example.eye.util.GlobalUtil
import com.example.eye.util.ShareUtil


/**
 * 获取SharedPreferences实例。
 */
val sharedPreferences: SharedPreferences =
    EyeApplication.context.getSharedPreferences(GlobalUtil.appPackage + "_preferences",
        Context.MODE_PRIVATE)

/**
 * 批量设置控件点击事件。
 *
 * @param v 点击的控件
 * @param block 处理点击事件回调代码块
 */
fun setOnClickListener(vararg v: View?, block: View.() -> Unit) {
    val listener = View.OnClickListener { it.block() }
    v.forEach { it?.setOnClickListener(listener) }
}

/**
 * 批量设置控件点击事件。
 *
 * @param v 点击的控件
 * @param listener 处理点击事件监听器
 */
fun setOnClickListener(vararg v: View?, listener: View.OnClickListener) {
    v.forEach { it?.setOnClickListener(listener) }
}

/**
 * 调用系统原生分享。
 *
 * @param activity 上下文
 * @param shareContent 分享内容
 * @param shareType SHARE_MORE=0，SHARE_QQ=1，SHARE_WECHAT=2，SHARE_WEIBO=3，SHARE_QQZONE=4
 */
fun share(activity: Activity, shareContent: String, shareType: Int) {
    ShareUtil.share(activity, shareContent, shareType)
}

/**
 * 弹出分享对话框。
 *
 * @param activity 上下文
 * @param shareContent 分享内容
 */
fun showDialogShare(activity: Activity, shareContent: String) {
    if (activity is AppCompatActivity) {
        ShareDialogFragment().showDialog(activity, shareContent)
    }
}

inline fun <reified T> start(context: Context) {
    context.startActivity(Intent(context, T::class.java))
}

inline fun <reified T> start(context: Context, block: Intent.() -> Unit) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}

