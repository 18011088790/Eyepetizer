package com.example.eye.logic

import com.example.eye.logic.dao.VideoDao
import com.example.eye.logic.model.VideoDetail
import com.example.eye.logic.network.EyepetizerNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * @Author:gxs
 *
 * @Date 2022/2/10 21:36
 *
 * @Description:视频相关，对应的仓库数据管理。
 *
 */
class VideoRepository(private val dao: VideoDao, private val network: EyepetizerNetwork) {

    suspend fun refreshVideoReplies(repliesUrl: String) = requestVideoReplies(repliesUrl)

    suspend fun refreshVideoRelatedAndVideoReplies(videoId: Long, repliesUrl: String) = requestVideoRelatedAndVideoReplies(videoId, repliesUrl)

    suspend fun refreshVideoDetail(videoId: Long, repliesUrl: String) = requestVideoDetail(videoId, repliesUrl)

    private suspend fun requestVideoReplies(url: String) = withContext(Dispatchers.IO) {
        coroutineScope {
            val deferredVideoReplies = async { network.fetchVideoReplies(url) }
            val videoReplies = deferredVideoReplies.await()
            videoReplies
        }
    }

    private suspend fun requestVideoRelatedAndVideoReplies(videoId: Long, repliesUrl: String) = withContext(
        Dispatchers.IO) {
        coroutineScope {
            val deferredVideoRelated = async { network.fetchVideoRelated(videoId) }
            val deferredVideoReplies = async { network.fetchVideoReplies(repliesUrl) }
            val videoRelated = deferredVideoRelated.await()
            val videoReplies = deferredVideoReplies.await()
            val videoDetail = VideoDetail(null, videoRelated, videoReplies)
            videoDetail
        }
    }

    private suspend fun requestVideoDetail(videoId: Long, repliesUrl: String) = withContext(
        Dispatchers.IO) {
        coroutineScope {
            val deferredVideoRelated = async { network.fetchVideoRelated(videoId) }
            val deferredVideoReplies = async { network.fetchVideoReplies(repliesUrl) }
            val deferredVideoBeanForClient = async { network.fetchVideoBeanForClient(videoId) }
            val videoBeanForClient = deferredVideoBeanForClient.await()
            val videoRelated = deferredVideoRelated.await()
            val videoReplies = deferredVideoReplies.await()
            val videoDetail = VideoDetail(videoBeanForClient, videoRelated, videoReplies)

            if (videoDetail.videoBeanForClient != null && videoDetail.videoRelated?.count ?: 0 > 0 && videoDetail.videoReplies.count > 0) {
                dao.cacheVideoDetail(videoDetail)
            }
            videoDetail
        }
    }

    companion object {

        private var repository: VideoRepository? = null

        fun getInstance(dao: VideoDao, network: EyepetizerNetwork): VideoRepository {
            if (repository == null) {
                synchronized(VideoRepository::class.java) {
                    if (repository == null) {
                        repository = VideoRepository(dao, network)
                    }
                }
            }

            return repository!!
        }
    }

}