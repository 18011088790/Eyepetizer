package com.example.eye.event


/**
 * @Author:gxs
 *
 * @Date 2022/2/10 20:29
 *
 * @Description:EventBus通知刷新界面消息。
 *
 */
open class RefreshEvent (var activityClass:Class<*>?=null):MessageEvent(){

}