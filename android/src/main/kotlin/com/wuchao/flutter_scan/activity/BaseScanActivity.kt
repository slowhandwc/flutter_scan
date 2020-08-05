package com.wuchao.flutter_scan.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import com.wuchao.flutter_scan.permission.PermissionUtils
import com.wuchao.flutter_scan.utils.dp2px
import com.wuchao.flutter_scan.R
import kotlinx.android.synthetic.main.activity_niimot_scan.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

abstract class BaseScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    lateinit var mScannerView: ZXingScannerView

    companion object {
        const val RESULT_QRCODE_STRING = "RESULT_QRCODE_STRING"
        const val TAG_TITLE = "title"
        const val TAG_CONTENT = "content"
    }

    open fun initData() {}
    open fun initEvent() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(javaClass.name, "onCreate")
        setContentView(R.layout.activity_niimot_scan)
        init()

    }

    fun init() {
        intent?.let {
            val title = it.getStringExtra(TAG_TITLE)
            val content = it.getStringExtra(TAG_CONTENT)
            tvTitle.text = title
            tv_text.text = content
        }
        mScannerView = findViewById(R.id.mScannerView)
        mScannerView.setAutoFocus(true)
        mScannerView.setAspectTolerance(0.2f)
        mScannerView.setLaserColor(resources.getColor(android.R.color.holo_red_dark))
        mScannerView.setBorderColor(resources.getColor(android.R.color.white))
        mScannerView.setBorderLineLength(dp2px( 30f).toInt())
        mScannerView.setBorderStrokeWidth(dp2px( 4f).toInt())
        mScannerView.setBorderCornerRadius(10)
        mScannerView.setIsBorderCornerRounded(true)
        mScannerView.setSquareViewFinder(true)
        setListener()
    }

    fun start() {
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    private fun setListener() {
        ll_open.setOnClickListener {
            try {
                mScannerView.toggleFlash()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        ll_close.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        PermissionUtils.create(this).request(android.Manifest.permission.CAMERA) { isGrand ->
            if (!isGrand) {
                AlertDialog.Builder(this)
                        .setTitle("授权提示")
                        .setMessage("请授权使用相机权限，否则无法使用此功能!")
                        .setNeutralButton("确定") { dialog, which ->
                            val intent = Intent()
                            intent.putExtra("error","请授权使用相机权限，否则无法使用此功能!")
                            setResult(Activity.RESULT_CANCELED,intent)
                            finish()
                        }

            } else {
                start()
                initData()
                initEvent()
            }
        }

    }

    override fun onStop() {
        super.onStop()
        Log.e("onPase","onPause")
        mScannerView.stopCamera()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    fun reStartScan() {
        mScannerView.resumeCameraPreview(this)
    }

    override fun handleResult(result: Result?) {
        result?.let {
            scanResult(it)
        }
    }

    abstract fun scanResult(result: Result)
}