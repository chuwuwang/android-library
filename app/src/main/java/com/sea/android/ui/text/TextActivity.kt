package com.sea.android.ui.text

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sea.android.library.R
import com.sea.android.library.databinding.ActivityTextBinding

class TextActivity : AppCompatActivity() {

    private val binding: ActivityTextBinding by lazy { ActivityTextBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val decodeResource = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        binding.logoImage.setImageBitmap(decodeResource)
    }

}