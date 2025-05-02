package com.github.sugunasriram.myfisloanlibone.fis_code.app

import FsTheme
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.webkit.PermissionRequest
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.github.sugunasriram.myfisloanlibone.fis_code.appBridge.AppBridgeManager
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.personalLoan.mGeoLocationCallback
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.personalLoan.mGeoLocationRequestOrigin

class MainActivity : ComponentActivity() {


    private val updateCompleted = mutableStateOf(false)
    private lateinit var bridgeManager: AppBridgeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        // 👇 Handle intent
        bridgeManager = AppBridgeManager(this)

        setContent {
            FsTheme {
                bridgeManager.RenderContent(intent)
            }
        }
    }

    override fun onRequestPermissionsResult(

        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CommonMethods().CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission granted
                    Log.d("MainActivity", "Camera permission granted")
                    webPermissionRequest?.grant(webPermissionRequest?.resources)
                } else {
                    // Camera permission denied
                    Log.d("MainActivity", "Camera permission denied")
                    webPermissionRequest?.deny()
                }
            }

            1002 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Location permission granted
                    // Check if location services are enabled
                    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                    if (!isLocationEnabled) {
                        AlertDialog.Builder(this)
                            .setTitle("Enable Location Services")
                            .setMessage("Location services are required for this app. Please enable them in settings.")
                            .setPositiveButton("OK") { _, _ ->
                                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                startActivity(intent)
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    } else {
//                        webPermissionRequest?.grant(webPermissionRequest?.resources)
                        mGeoLocationCallback?.invoke(mGeoLocationRequestOrigin, true, false)

                    }
                } else {
                    // Location permission denied
                    Log.d("MainActivity", "Location permission denied")
//                    webPermissionRequest?.deny()
                    mGeoLocationCallback?.invoke(mGeoLocationRequestOrigin, false, false)

                }

            }
        }
        // Clear the webPermissionRequest after handling it
        webPermissionRequest = null
    }
    companion object {
        var webPermissionRequest: PermissionRequest? = null
        const val REQUEST_CODE_UPDATE = 2001
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show()
                updateCompleted.value = false // if canceled
            }else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Update cancelled !",  Toast.LENGTH_SHORT).show()
                updateCompleted.value = false // if canceled
            } else {
                Toast.makeText(this, "Update completed!", Toast.LENGTH_SHORT).show()
                updateCompleted.value = true // Continue to app even if canceled
            }
        }

    }



}
