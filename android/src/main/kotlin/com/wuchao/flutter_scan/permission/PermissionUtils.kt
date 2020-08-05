package com.wuchao.flutter_scan.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity

/**
 * 权限管理工具类
 */
class PermissionUtils private constructor(private var context: FragmentActivity, private val code: Int) {

    companion object {
        fun create(context: FragmentActivity, code: Int = 0x55) = PermissionUtils(context, code)
        fun isGranted(context: Context, permission: String): Boolean =
            ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

        fun isDenidePermanent(context: Activity, permission: String): Boolean =
            !ActivityCompat.shouldShowRequestPermissionRationale(context, permission)

    }

    private val mFragment by lazy { RxFragment.getFragment(context) }
    /**
     * 请求权限
     */
    @JvmOverloads
    fun requestAll(vararg permissions: String, listener: (PermissionModule) -> Unit) {
        requestPermission(*permissions) {
            val permissionModule = PermissionModule()
            val name = StringBuffer()
            var granted = true
            var shouldShowRequestPermissionRationale = false
            it.withIndex().forEach { (index, item) ->
                name.append(item.name).append(if (index == it.size - 1) "" else ",")
                granted = granted && item.granted
                shouldShowRequestPermissionRationale =
                    shouldShowRequestPermissionRationale && item.shouldShowRequestPermissionRationale
            }
            permissionModule.name = name.toString()
            permissionModule.granted = granted
            permissionModule.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale
            listener.invoke(permissionModule)
        }
    }

    fun request(vararg permissions: String, listener: (granted:Boolean) -> Unit) {
        requestPermission(*permissions) {
            it.forEach {
                if (!it.granted) {
                    listener.invoke(false)
                    return@forEach
                }
                listener.invoke(true)
            }
        }
    }

    fun requestEach(vararg permissions: String, listener: (ArrayList<PermissionModule>) -> Unit) {
        requestPermission(*permissions) {
            listener.invoke(it)
        }
    }


    /**
     * 请求权限
     */
    private fun requestPermission(vararg permissions: String, listener: (ArrayList<PermissionModule>) -> Unit) {
        val list = arrayListOf<PermissionModule>()
        val requestPermission = arrayListOf<String>()
        permissions.forEach {
            if (isGranted(context, it)) {
                list.add(
                    PermissionModule(it, granted = true, shouldShowRequestPermissionRationale = false)
                )
            } else {
                requestPermission.add(it)
            }
        }
        if (requestPermission.isNotEmpty()) {
            mFragment.requestPermission(code, requestPermission) {
                list.addAll(it)
                listener.invoke(list)
            }
        } else {
            listener.invoke(list)
        }
    }

}