package com.example.eye.ui.notification.push

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eye.logic.MainPageRepository

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 20:37
 *
 * @Description:
 *
 */
class PushViewModelFactory(private val repository: MainPageRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PushViewModel(repository) as T
    }
}