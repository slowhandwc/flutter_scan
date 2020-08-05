package com.wuchao.flutter_scan.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.google.zxing.Result
import com.wuchao.flutter_scan.utils.setStatusBarLightColor

/**
 * @Author wuchao
 * @Date 2020/6/26-3:45 PM
 * @see
 * @description
 * @email 329187218@qq.com
 */
open class ScanActivity : BaseScanActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarLightColor(this,Color.TRANSPARENT)
    }
    override fun scanResult(result: Result) {
        if(result.text.isNotEmpty()){
            val intent = Intent()
            intent.putExtra(RESULT_QRCODE_STRING,result.text)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }
}