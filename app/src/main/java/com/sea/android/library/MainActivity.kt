package com.sea.android.library

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.guolindev.PermissionX
import com.sea.android.library.databinding.ActivityMainBinding
import com.sea.android.library.ui.indicator.IndicatorActivity
import com.sea.android.library.use.LibraryActivity
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
        requestPermission()
    }

    private fun initView() {
        binding.textBtn.setOnClickListener { openActivity<TextActivity>() }
        binding.editTextBtn.setOnClickListener { openActivity<EditActivity>() }
        binding.libraryBtn.setOnClickListener { openActivity<LibraryActivity>() }
        binding.progressBtn.setOnClickListener { openActivity<ProgressActivity>() }
        binding.indicatorBtn.setOnClickListener { openActivity<IndicatorActivity>() }
    }

    private fun requestPermission() {
        PermissionX.init(this)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .request { _, _, _ -> }
    }

}