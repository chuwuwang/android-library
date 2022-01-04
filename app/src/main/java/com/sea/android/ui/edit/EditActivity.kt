package com.sea.android.ui.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sea.android.library.databinding.ActivityEditBinding
import com.sea.library.common.extension.openActivity

class EditActivity : AppCompatActivity() {

    private val binding: ActivityEditBinding by lazy { ActivityEditBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.verificationCode.setOnClickListener { openActivity<VerificationCodeActivity>() }
        binding.gradientTextView.setOnClickListener { openActivity<GradientTextViewActivity>() }
    }

}