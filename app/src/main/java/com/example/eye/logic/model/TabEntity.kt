package com.example.eye.logic.model

import com.flyco.tablayout.listener.CustomTabEntity

/**
 * @Author:gxs
 *
 * @Date 2022/2/10 21:19
 *
 * @Description:与CommonTabLayout搭配使用的实体类
 *
 */
class TabEntity(private var title: String, private var selectedIcon: Int = 0, private var unSelectedIcon: Int = 0) :
    CustomTabEntity {

    override fun getTabTitle(): String {
        return title
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon
    }

    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon
    }

}
