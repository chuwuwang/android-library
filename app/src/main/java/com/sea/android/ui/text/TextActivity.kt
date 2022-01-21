package com.sea.android.ui.text

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sea.android.library.databinding.ActivityTextBinding
import com.sea.android.ui.edit.GradientTextViewActivity
import com.sea.library.common.extension.openActivity
import com.yalantis.ucrop.UCrop

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