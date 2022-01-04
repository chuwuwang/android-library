package com.sea.android.ui.progress

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sea.android.library.databinding.ActivityProgressViewBinding

class ProgressViewActivity : AppCompatActivity() {

    private val binding: ActivityProgressViewBinding by lazy { ActivityProgressViewBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.progressView.progress = 0.2f
    }

}