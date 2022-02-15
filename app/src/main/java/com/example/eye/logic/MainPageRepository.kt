package com.example.eye.logic

import com.example.eye.logic.dao.MainPageDao
import com.example.eye.logic.network.EyepetizerNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @Author:gxs
 *
 * @Date 2022/2/10 21:35
 *
 * @Description:主页界面，主要包含：（首页，社区，通知，我的），对应的仓库数据管理。
 *
 */
class MainPageRepository private constructor(private val mainPageDao: MainPageDao, private val eyepetizerNetwork: EyepetizerNetwork) {

    suspend fun refreshDiscovery(url: String) = requestDiscovery(url)

    suspend fun refreshHomePageRecommend(url: String) = requestHomePageRecommend(url)

    suspend fun refreshDaily(url: String) = requestDaily(url)

    suspend fun refreshCommunityRecommend(url: String) = requestCommunityRecommend(url)

    suspend fun refreshFollow(url: String) = requestFollow(url)

    suspend fun refreshPushMessage(url: String) = requestPushMessage(url)

    suspend fun refreshHotSearch() = requestHotSearch()

    private suspend fun requestDiscovery(url: String) = withContext(Dispatchers.IO) {
        val response = eyepetizerNetwork.fetchDiscovery(url)
        mainPageDao.cacheDiscovery(response)
        response
    }

    private suspend fun requestHomePageRecommend(url: String) = withContext(Dispatchers.IO) {
        val response = eyepetizerNetwork.fetchHomePageRecommend(url)
        mainPageDao.cacheHomePageRecommend(response)
        response
    }

    private suspend fun requestDaily(url: String) = withContext(Dispatchers.IO) {
        val response = eyepetizerNetwork.fetchDaily(url)
        mainPageDao.cacheDaily(response)
        response
    }

    private suspend fun requestCommunityRecommend(url: String) = withContext(Dispatchers.IO) {
        val response = eyepetizerNetwork.fetchCommunityRecommend(url)
        mainPageDao.cacheCommunityRecommend(response)
        response
    }

    private suspend fun requestFollow(url: String) = withContext(Dispatchers.IO) {
        val response = eyepetizerNetwork.fetchFollow(url)
        mainPageDao.cacheFollow(response)
        response
    }

    private suspend fun requestPushMessage(url: String) = withContext(Dispatchers.IO) {
        val response = eyepetizerNetwork.fetchPushMessage(url)
        mainPageDao.cachePushMessageInfo(response)
        response
    }

    private suspend fun requestHotSearch() = withContext(Dispatchers.IO) {
        val response = eyepetizerNetwork.fetchHotSearch()
        mainPageDao.cacheHotSearch(response)
        response
    }

    companion object {

        private var repository: MainPageRepository? = null

        fun getInstance(dao: MainPageDao, network: EyepetizerNetwork): MainPageRepository {
            if (repository == null) {
                synchronized(MainPageRepository::class.java) {
                    if (repository == null) {
                        repository = MainPageRepository(dao, network)
                    }
                }
            }

            return repository!!
        }
    }
}