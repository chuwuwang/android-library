package com.sea.android.ui.progress

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sea.android.library.databinding.ActivityProgressBinding
import com.sea.library.common.extension.openActivity

class ProgressActivity : AppCompatActivity() {

    private val binding: ActivityProgressBinding by lazy { ActivityProgressBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.progressView.setOnClickListener { openActivity<ProgressViewActivity>() }
    }

}