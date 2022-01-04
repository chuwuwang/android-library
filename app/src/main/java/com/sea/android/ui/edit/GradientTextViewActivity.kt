package com.sea.android.ui.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sea.android.library.databinding.ActivityTextGradientViewBinding

class GradientTextViewActivity : AppCompatActivity() {

    private val binding: ActivityTextGradientViewBinding by lazy { ActivityTextGradientViewBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.gradientText.slidingPercent = 0.3f
    }

}