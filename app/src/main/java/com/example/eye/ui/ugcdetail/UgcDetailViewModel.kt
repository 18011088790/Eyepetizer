package com.example.eye.ui.ugcdetail

import androidx.lifecycle.ViewModel
import com.example.eye.logic.model.CommunityRecommend

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 21:15
 *
 * @Description:
 *
 */
class UgcDetailViewModel : ViewModel() {

    var dataList: List<CommunityRecommend.Item>? = null

    var itemPosition: Int = -1
}