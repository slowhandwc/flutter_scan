package com.wuchao.flutter_scan

import android.app.Activity
import android.content.Intent
import androidx.annotation.NonNull
import com.wuchao.flutter_scan.activity.BaseScanActivity
import com.wuchao.flutter_scan.activity.BaseScanActivity.Companion.TAG_CONTENT
import com.wuchao.flutter_scan.activity.BaseScanActivity.Companion.TAG_TITLE
import com.wuchao.flutter_scan.activity.ScanActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** FlutterScanPlugin */
public class FlutterScanPlugin : FlutterPlugin, ActivityAware, MethodCallHandler {
    private lateinit var result: MethodChannel.Result

    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var flutterPluginBinding: FlutterPlugin.FlutterPluginBinding
    private lateinit var activityPluginBinding: ActivityPluginBinding
    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        this.flutterPluginBinding = flutterPluginBinding
        channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), CHANNEL)
        channel.setMethodCallHandler(this);
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    companion object {
        const val CHANNEL = "com.niimbot.flutter.scan"
        const val METHOD_SCAN = "niimbot_scan"
        const val ACTIVITY_REQUEST_CODE = 1234

    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        this.result = result
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == METHOD_SCAN) {
            val title = call.argument<String>("title")
            val content = call.argument<String>("content")
            val intent = Intent(activityPluginBinding.activity,ScanActivity::class.java)
            intent.putExtra(TAG_TITLE,title)
            intent.putExtra(TAG_CONTENT,content)
            activityPluginBinding.activity.startActivityForResult(intent, ACTIVITY_REQUEST_CODE)
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onDetachedFromActivity() {
        channel.setMethodCallHandler(null)
    }

    override fun onReattachedToActivityForConfigChanges(activityPluginBinding: ActivityPluginBinding) {
        this.activityPluginBinding = activityPluginBinding
    }

    override fun onAttachedToActivity(activityPluginBinding: ActivityPluginBinding) {
        this.activityPluginBinding = activityPluginBinding
        this.activityPluginBinding.addActivityResultListener{requestCode,resultCode,data->
            if(requestCode== ACTIVITY_REQUEST_CODE){
                if(resultCode== Activity.RESULT_OK){
                    val reply = HashMap<String,Any>()
                    val code = data.getStringExtra(BaseScanActivity.RESULT_QRCODE_STRING)
                    reply["code"] = code
                    result.success(reply)
                } else {
                    var error = "cancel"
                    data?.let {
                        error = it.getStringExtra("error")
                    }
                    val reply = HashMap<String,Any>()
                    reply["error"] = error
                    result.error("error",null,reply)
                }
            }
            return@addActivityResultListener true
        }
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }
}
