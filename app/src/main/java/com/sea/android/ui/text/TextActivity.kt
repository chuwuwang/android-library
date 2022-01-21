package com.sea.android.ui.text

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sea.android.library.databinding.ActivityTextBinding

class TextActivity : AppCompatActivity() {

    private val binding: ActivityTextBinding by lazy { ActivityTextBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {

    }

}