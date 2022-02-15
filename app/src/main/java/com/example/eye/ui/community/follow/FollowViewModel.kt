package com.example.eye.ui.community.follow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.eye.logic.MainPageRepository
import com.example.eye.logic.model.Follow
import com.example.eye.logic.network.api.MainPageService

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 20:08
 *
 * @Description:
 *
 */
class FollowViewModel(repository: MainPageRepository) : ViewModel() {

    var dataList = ArrayList<Follow.Item>()

    private var requestParamLiveData = MutableLiveData<String>()

    var nextPageUrl: String? = null

    val dataListLiveData = Transformations.switchMap(requestParamLiveData) { url ->
        liveData {
            val resutlt = try {
                val follow = repository.refreshFollow(url)
                Result.success(follow)
            } catch (e: Exception) {
                Result.failure<Follow>(e)
            }
            emit(resutlt)
        }
    }

    fun onRefresh() {
        requestParamLiveData.value = MainPageService.FOLLOW_URL
    }

    fun onLoadMore() {
        requestParamLiveData.value = nextPageUrl ?: ""
    }
}