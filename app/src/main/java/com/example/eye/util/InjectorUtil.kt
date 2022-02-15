package com.example.eye.util

import com.example.eye.logic.MainPageRepository
import com.example.eye.logic.VideoRepository
import com.example.eye.logic.dao.EyepetizerDatabase
import com.example.eye.logic.network.EyepetizerNetwork
import com.example.eye.ui.community.follow.FollowViewModelFactory
import com.example.eye.ui.home.daily.DailyViewModelFactory
import com.example.eye.ui.home.discovery.DiscoveryViewModelFactory
import com.example.eye.ui.newdetail.NewDetailViewModelFactory
import com.example.eye.ui.notification.push.PushViewModelFactory
import com.example.eye.ui.search.SearchViewModelFactory

/**
 * @Author:gxs
 *
 * @Date 2022/2/10 20:25
 *
 * @Description:应用程序逻辑控制管理类
 *
 */
object InjectorUtil {

    private fun getMainPageRepository() = MainPageRepository.getInstance(
        EyepetizerDatabase.getMainPageDao(),
        EyepetizerNetwork.getInstance()
    )

    private fun getViedoRepository() = VideoRepository.getInstance(
        EyepetizerDatabase.getVideoDao(),
        EyepetizerNetwork.getInstance()
    )

    fun getDiscoveryViewModelFactory() = DiscoveryViewModelFactory(getMainPageRepository())

    fun getHomePageCommendViewModelFactory() =
        com.example.eye.ui.home.commend.CommendViewModelFactory(getMainPageRepository())

    fun getDailyViewModelFactory() = DailyViewModelFactory(getMainPageRepository())

    fun getCommunityCommendViewModelFactory() =
        com.example.eye.ui.community.commend.CommendViewModelFactory(getMainPageRepository())

    fun getFollowViewModelFactory() = FollowViewModelFactory(getMainPageRepository())
//
    fun getPushViewModelFactory() = PushViewModelFactory(getMainPageRepository())
//
    fun getSearchViewModelFactory() = SearchViewModelFactory(getMainPageRepository())

    fun getNewDetailViewModelFactory() = NewDetailViewModelFactory(getViedoRepository())

}