package com.example.eye.ui.notification.push

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.eye.logic.MainPageRepository
import com.example.eye.logic.model.PushMessage
import com.example.eye.logic.network.api.MainPageService

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 20:37
 *
 * @Description:
 *
 */
class PushViewModel(val repository: MainPageRepository) : ViewModel() {

    var dataList = ArrayList<PushMessage.Message>()

    private var requestParamLiveData = MutableLiveData<String>()

    var nextPageUrl: String? = null

    val dataListLiveData = Transformations.switchMap(requestParamLiveData) { url ->
        liveData {
            val resutlt = try {
                val pushMessage = repository.refreshPushMessage(url)
                Result.success(pushMessage)
            } catch (e: Exception) {
                Result.failure<PushMessage>(e)
            }
            emit(resutlt)
        }
    }

    fun onRefresh() {
        requestParamLiveData.value = MainPageService.PUSHMESSAGE_URL
    }

    fun onLoadMore() {
        requestParamLiveData.value = nextPageUrl ?: ""
    }
}
