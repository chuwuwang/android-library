package com.sea.android.library.use

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sea.android.library.R
import com.sea.android.library.databinding.ActivityLibraryCropBinding
import com.sea.library.common.logger.Logger
import com.yalantis.ucrop.UCrop
import java.io.File

class UseUCropActivity : AppCompatActivity() {

    private val binding: ActivityLibraryCropBinding by lazy { ActivityLibraryCropBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        pickFromGallery()
    }

    private fun initView() {

    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).setType("image/*").addCategory(Intent.CATEGORY_OPENABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        val createChooser = Intent.createChooser(intent, "Select Picture")
        startActivityForResult(createChooser, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent ? ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK || data == null) return
        if (requestCode == 100) {
            val uri = data.data
            if (uri != null) {
                Logger.e("ktx", "uri: $uri")
                startCrop(uri)
            }
        }
        if (requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data)
            Logger.e("ktx", "resultUri: $resultUri")
        }
        if (requestCode == UCrop.RESULT_ERROR) {
            Logger.e("ktx", "RESULT ERROR")
        }
    }

    private fun startCrop(uri: Uri) {
        val file = File(cacheDir, System.currentTimeMillis().toString() + ".png")
        val destinationUri = Uri.fromFile(file)
        val uCrop = UCrop.of(uri, destinationUri)

        val options = UCrop.Options()
        options.setCompressionQuality(100)  // 设置裁剪图片的质量（0到100）
        options.setCompressionFormat(Bitmap.CompressFormat.PNG) // 设置裁剪出来图片的格式 WEBP PNG JPEG

        // options.setHideBottomControls(true)
        // options.setAllowedGestures(UCropActivity.NONE, UCropActivity.NONE, UCropActivity.ALL) // 设置裁剪图片的手势操作开关 NONE 表示关闭了其手势操作

        options.setShowCropFrame(false) // 设置是否展示矩形裁剪框
        options.setCircleDimmedLayer(true)
        // options.setCropGridRowCount(1)          // 设置横线的数量
        // options.setCropGridStrokeWidth(20)      // 设置裁剪框横竖线的宽度
        // options.setCropGridColumnCount(2)       // 设置竖线的数量
        // options.setCropGridColor(Color.GREEN)   // 设置裁剪框横竖线的颜色

        // Color palette
        val white = ContextCompat.getColor(this, R.color.white)
        val black = ContextCompat.getColor(this, R.color.black)
        val firstColor = ContextCompat.getColor(this, R.color.firstColor)
        options.setToolbarColor(black)
        options.setStatusBarColor(black)
        options.setToolbarWidgetColor(white)
        options.setRootViewBackgroundColor(white)
        options.setActiveControlsWidgetColor(firstColor)
        options.setToolbarCancelDrawable(R.drawable.ic_x_white_72)
        options.setToolbarCropDrawable(R.drawable.ic_right_white_72)

        uCrop.withOptions(options)

        uCrop.start(this)
    }

}