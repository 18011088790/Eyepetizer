package com.example.eye.ui.common.callback

/**
 * @Author:gxs
 *
 * @Date 2022/2/10 22:09
 *
 * @Description:在Activity或Fragment中进行网络请求所需要经历的生命周期函数
 *
 */
interface RequestLifecycle {
    fun startLoading()

    fun loadFinished()

    fun loadFailed(msg: String?)
}