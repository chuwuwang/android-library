package com.sea.android.library.use

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.sea.android.library.App
import com.sea.android.library.GlideEngine
import com.sea.android.library.databinding.ActivityLibraryBinding
import com.sea.library.common.extension.openActivity
import com.sea.library.common.logger.Logger

class LibraryActivity : AppCompatActivity() {

    private val binding: ActivityLibraryBinding by lazy { ActivityLibraryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.cropBtn.setOnClickListener { openActivity<UseUCropActivity>() }
        binding.easyPhotoBtn.setOnClickListener { openGallery() }
    }

    private fun openGallery() {
        val imageEngine = GlideEngine.getInstance()
        EasyPhotos.createAlbum(this, true, false, imageEngine)
            .setFileProviderAuthority(App.FILE_AUTHORITIES)
            .setCount(9)
            .start(100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent ? ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && RESULT_OK == resultCode && requestCode == 100) {
            val resultPhotos = data.getParcelableArrayListExtra<Photo>(EasyPhotos.RESULT_PHOTOS)
            val selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false)
            Logger.e(App.TAG, "selectedOriginal: $selectedOriginal resultPhotos: $resultPhotos")
        }
    }

}