package com.sea.library.common.helper

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates

class PermissionsHelper {

    private val nextLocalRequestCode = AtomicInteger()

    private val nextKey: String
        get() {
            val increment = nextLocalRequestCode.getAndIncrement()
            return "activity_request#$increment"
        }

    fun ComponentActivity.requestPermission(permission: String, onAllowed: () -> Unit, onDenied: (shouldShowCustomRequest: Boolean) -> Unit) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            onAllowed()
            return
        }
        var launcher by Delegates.notNull< ActivityResultLauncher<String> >()
        val requestPermissionContract = ActivityResultContracts.RequestPermission()
        launcher = activityResultRegistry.register(nextKey, requestPermissionContract) { result ->
            if (result) {
                onAllowed()
            } else {
                val shouldShowRequestPermissionRationale = ! ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                onDenied(shouldShowRequestPermissionRationale)
            }
            launcher.unregister()
        }
        val observer = object : LifecycleEventObserver {

            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    launcher.unregister()
                    lifecycle.removeObserver(this)
                }
            }

        }
        lifecycle.addObserver(observer)
        launcher.launch(permission)
    }

    fun ComponentActivity.requestPermissions(permissions: Array<String>, onAllowed: () -> Unit, onDenied: (shouldShowCustomRequest: Boolean) -> Unit) {
        var hasPermissions = true
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                hasPermissions = false
                break
            }
        }
        if (hasPermissions) {
            onAllowed()
            return
        }
        var launcher by Delegates.notNull< ActivityResultLauncher< Array<String> > >()
        val requestMultiplePermissionsContract = ActivityResultContracts.RequestMultiplePermissions()
        launcher = activityResultRegistry.register(nextKey, requestMultiplePermissionsContract) { result ->
            var allAllow = true
            for (allow in result.values) {
                if (!allow) {
                    allAllow = false
                    break
                }
            }
            if (allAllow) {
                onAllowed()
            } else {
                var shouldShowCustomRequest = false
                for (permission in permissions) {
                    val shouldShowRequestPermissionRationale = ! ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                    if (shouldShowRequestPermissionRationale) {
                        shouldShowCustomRequest = true
                        break
                    }
                }
                onDenied(shouldShowCustomRequest)
            }
            launcher.unregister()
        }
        val observer = object : LifecycleEventObserver {

            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    launcher.unregister()
                    lifecycle.removeObserver(this)
                }
            }

        }
        lifecycle.addObserver(observer)
        launcher.launch(permissions)
    }

}