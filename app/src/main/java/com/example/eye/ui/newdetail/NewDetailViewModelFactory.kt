package com.example.eye.ui.newdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eye.logic.VideoRepository

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 13:54
 *
 * @Description:
 *
 */
class NewDetailViewModelFactory(private val repository: VideoRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewDetailViewModel(repository) as T
    }
}