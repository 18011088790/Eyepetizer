package com.example.eye.event

/**
 * @Author:gxs
 *
 * @Date 2022/2/10 20:31
 *
 * @Description:EventBus通知Tab页切换界面
 *
 */
open class SwitchPagesEvent(var activityClass: Class<*>? = null) : MessageEvent()