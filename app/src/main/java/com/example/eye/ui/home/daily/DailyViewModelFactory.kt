package com.example.eye.ui.home.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eye.logic.MainPageRepository

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 13:11
 *
 * @Description:
 *
 */
class DailyViewModelFactory(private val repository: MainPageRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DailyViewModel(repository) as T
    }
}