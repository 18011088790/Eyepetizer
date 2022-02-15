package com.example.eye.ui.home.commend

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.eye.logic.MainPageRepository
import com.example.eye.logic.VideoRepository
import com.example.eye.logic.model.HomePageRecommend
import com.example.eye.logic.network.api.MainPageService

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 12:47
 *
 * @Description:
 *
 */
class CommendViewModel(val repository: MainPageRepository) : ViewModel() {

    var dataList = ArrayList<HomePageRecommend.Item>()

    private var requestParamLiveData = MutableLiveData<String>()

    var nextPageUrl: String? = null

    val dataListLiveData = Transformations.switchMap(requestParamLiveData) { url ->
        liveData {
            val resutlt = try {
                val recommend = repository.refreshHomePageRecommend(url)
                Result.success(recommend)
            } catch (e: Exception) {
                Result.failure<HomePageRecommend>(e)
            }
            emit(resutlt)
        }
    }

    fun onRefresh() {
        requestParamLiveData.value = MainPageService.HOMEPAGE_RECOMMEND_URL
    }

    fun onLoadMore() {
        requestParamLiveData.value = nextPageUrl ?: ""
    }
}