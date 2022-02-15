package com.example.eye.logic.network.api

import com.example.eye.logic.model.VideoBeanForClient
import com.example.eye.logic.model.VideoRelated
import com.example.eye.logic.model.VideoReplies
import com.example.eye.logic.network.ServiceCreator
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * @Author:gxs
 *
 * @Date 2022/2/10 21:39
 *
 * @Description:与视频页面相关API请求
 *
 */
interface VideoService {

    /**
     * 视频详情-视频信息
     */
    @GET("api/v2/video/{id}")
    fun getVideoBeanForClient(@Path("id") videoId: Long): Call<VideoBeanForClient>

    /**
     * 视频详情-推荐列表
     */
    @GET("api/v4/video/related")
    fun getVideoRelated(@Query("id") videoId: Long): Call<VideoRelated>

    /**
     * 视频详情-评论列表
     */
    @GET
    fun getVideoReplies(@Url url: String): Call<VideoReplies>

    companion object {

        /**
         * 视频详情-评论列表URL
         */
        const val VIDEO_REPLIES_URL = "${ServiceCreator.BASE_URL}api/v2/replies/video?videoId="
    }

}