package com.sea.android.library.use

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sea.android.library.databinding.ActivityLibraryBinding
import com.sea.library.common.extension.openActivity

class LibraryActivity : AppCompatActivity() {

    private val binding: ActivityLibraryBinding by lazy { ActivityLibraryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.cropBtn.setOnClickListener { openActivity<UseUCropActivity>() }
    }

}