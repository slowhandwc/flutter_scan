package com.wuchao.flutter_scan.permission

/**
 * 权限对象的一个封装类
 */
data class PermissionModule(var name: String = "", var granted: Boolean = true, var shouldShowRequestPermissionRationale: Boolean = true)
