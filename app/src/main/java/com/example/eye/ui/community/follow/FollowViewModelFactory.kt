package com.example.eye.ui.community.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eye.logic.MainPageRepository

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 20:07
 *
 * @Description:
 *
 */
class FollowViewModelFactory(val repository: MainPageRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowViewModel(repository) as T
    }
}