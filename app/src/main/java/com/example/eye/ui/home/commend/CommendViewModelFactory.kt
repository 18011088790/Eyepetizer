package com.example.eye.ui.home.commend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eye.logic.MainPageRepository

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 13:10
 *
 * @Description:
 *
 */
class CommendViewModelFactory(private val repository: MainPageRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CommendViewModel(repository) as T
    }
}