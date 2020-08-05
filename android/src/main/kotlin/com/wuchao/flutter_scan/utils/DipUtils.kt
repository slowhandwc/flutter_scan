package com.wuchao.flutter_scan.utils

import android.content.res.Resources
import android.util.TypedValue

/**
 * dp 工具类
 * @Author wuchao
 * @Date 2020/6/26-3:25 PM
 * @see
 * @description
 * @email 329187218@qq.com
 */
fun dp2px(value:Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value, Resources.getSystem().displayMetrics)