package com.example.eye.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @Author:gxs
 *
 * @Date 2022/2/10 21:19
 *
 * @Description:通知-推送消息列表，响应实体类。
 *
 */
data class PushMessage(@SerializedName("messageList") val itemList: List<Message>, val nextPageUrl: String?, val updateTime: Long) : Model() {

    data class Message(
        val actionUrl: String,
        val content: String,
        val date: Long,
        val icon: String,
        val id: Int,
        val ifPush: Boolean,
        val pushStatus: Int,
        val title: String,
        val uid: Any,
        val viewed: Boolean
    )
}
