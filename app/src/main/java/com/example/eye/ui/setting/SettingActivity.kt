package com.example.eye.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.eye.R
import com.example.eye.databinding.ActivitySettingBinding
import com.example.eye.ui.common.ui.BaseActivity
import com.example.eye.util.GlobalUtil
import kotlinx.android.synthetic.main.layout_title_bar.*

/**
 * @Author:gxs
 *
 * @Date 2022/2/11 22:08
 *
 * @Description:
 *
 */
class SettingActivity : BaseActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(SettingViewModel::class.java) }

    private val binding by lazy { ActivitySettingBinding.inflate(LayoutInflater.from(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun setupViews() {
        super.setupViews()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initTitle()
    }

    private fun initTitle() {
        tvTitle.text = GlobalUtil.getString(R.string.settings)
        tvRightText.setTextColor(ContextCompat.getColor(this@SettingActivity, R.color.white))
        tvRightText.textSize = 12f
    }


}