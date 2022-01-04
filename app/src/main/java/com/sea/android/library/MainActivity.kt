package com.sea.android.library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sea.android.library.databinding.ActivityMainBinding
import com.sea.android.ui.edit.EditActivity
import com.sea.android.ui.progress.ProgressActivity
import com.sea.android.ui.text.TextActivity
import com.sea.library.common.extension.openActivity

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.textBtn.setOnClickListener { openActivity<TextActivity>() }
        binding.editTextBtn.setOnClickListener { openActivity<EditActivity>() }
        binding.progressBtn.setOnClickListener { openActivity<ProgressActivity>() }
    }

}