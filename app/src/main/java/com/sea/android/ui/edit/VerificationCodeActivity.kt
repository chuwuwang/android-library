package com.sea.android.ui.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sea.android.library.databinding.ActivityEditVerificationCodeBinding

class VerificationCodeActivity : AppCompatActivity() {

    private val binding: ActivityEditVerificationCodeBinding by lazy { ActivityEditVerificationCodeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {

    }

}