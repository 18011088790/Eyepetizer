package com.example.eye.logic.dao

/**
 * @Author:gxs
 *
 * @Date 2022/2/10 21:09
 *
 * @Description:应用程序所有Dao操作管理类
 *
 */
object EyepetizerDatabase {

    private var mainPageDao: MainPageDao? = null

    private var videoDao: VideoDao? = null

    fun getMainPageDao(): MainPageDao {
        if (mainPageDao == null) {
            mainPageDao = MainPageDao()
        }
        return mainPageDao!!
    }

    fun getVideoDao(): VideoDao {
        if (videoDao == null) {
            videoDao = VideoDao()
        }
        return videoDao!!
    }
}