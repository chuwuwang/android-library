package com.sea.android.library.ui.indicator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sea.android.library.databinding.ActivityIndicatorBinding
import com.sea.library.common.extension.openActivity

class IndicatorActivity : AppCompatActivity() {

    private val binding: ActivityIndicatorBinding by lazy { ActivityIndicatorBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.circleStickIndicator.setOnClickListener { openActivity<CircleStickIndicatorActivity>() }
    }

}