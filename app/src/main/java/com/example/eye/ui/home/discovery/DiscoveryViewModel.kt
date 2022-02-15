package com.example.eye.ui.home.discovery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.eye.logic.MainPageRepository
import com.example.eye.logic.model.Discovery
import com.example.eye.logic.network.api.MainPageService

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 13:27
 *
 * @Description:
 *
 */
class DiscoveryViewModel(repository: MainPageRepository) : ViewModel() {

    var dataList = ArrayList<Discovery.Item>()

    private var requestParamLiveData = MutableLiveData<String>()

    var nextPageUrl: String? = null

    val dataListLiveData = Transformations.switchMap(requestParamLiveData) { url ->
        liveData {
            val resutlt = try {
                val discovery = repository.refreshDiscovery(url)
                Result.success(discovery)
            } catch (e: Exception) {
                Result.failure<Discovery>(e)
            }
            emit(resutlt)
        }
    }

    fun onRefresh() {
        requestParamLiveData.value = MainPageService.DISCOVERY_URL
    }

    fun onLoadMore() {
        requestParamLiveData.value = nextPageUrl ?: ""
    }
}
