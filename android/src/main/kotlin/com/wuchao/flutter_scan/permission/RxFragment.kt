package com.wuchao.flutter_scan.permission

import android.content.Intent
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.util.*

/**
 *  fragment
 */
class RxFragment : Fragment() {

    companion object {
         fun getFragment(context: FragmentActivity): RxFragment {
            var fragment = context.supportFragmentManager.findFragmentByTag("RxFragment") as? RxFragment
            if (fragment == null) {
                fragment = RxFragment()
                context.supportFragmentManager.beginTransaction().add(fragment, "RxFragment").commitAllowingStateLoss()
                context.supportFragmentManager.executePendingTransactions()
            }
            return fragment
        }
    }
    /**
     * 权限回调函数集合
     */
    private val  requestPermissions= hashMapOf<Int,(ArrayList<PermissionModule>)->Unit>()
    private val  requestIntents= hashMapOf<Int,( Int,Intent?)->Unit>()

    /**
     * 请求权限
     */
    fun requestPermission( requestCode: Int,permissions: List<String>,listener :(ArrayList<PermissionModule>)->Unit){
        requestPermissions[requestCode]=listener
        this.requestPermissions(permissions.toTypedArray(),requestCode)
    }
    /**
     * 请求intent
     */
          fun requestIntent(requestCode: Int,intent:Intent,listener :(Int,Intent?)->Unit){
            requestIntents[requestCode]=listener
            this.startActivityForResult(intent,requestCode)
    }

    /**
     * intent回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        requestIntents[requestCode]?.invoke(resultCode,data)
        requestIntents.remove(requestCode)
    }

    /**
     * 权限回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val permissionModules= arrayListOf<PermissionModule>()
        permissions.withIndex().forEach { ( index,permission)->
                val   result = if(grantResults[index]==PackageManager.PERMISSION_GRANTED){
                PermissionModule(permission, granted = true, shouldShowRequestPermissionRationale = false)
            }else{
                PermissionModule(permission, granted = false, shouldShowRequestPermissionRationale=!shouldShowRequestPermissionRationale(permission))
            }
            permissionModules.add(result)
        }
        requestPermissions[requestCode]?.invoke(permissionModules)
        requestPermissions.remove(requestCode)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
}