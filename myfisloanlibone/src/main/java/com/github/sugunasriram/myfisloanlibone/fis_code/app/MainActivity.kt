package com.github.sugunasriram.myfisloanlibone.fis_code.app

import FsTheme
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.WindowManager
import android.webkit.PermissionRequest
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.LoanLib.PersonalDetails
import com.github.sugunasriram.myfisloanlibone.LoanLib.ProductDetails
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.AppScreens
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.LaunchScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToReviewDetailsScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.personalLoan.mGeoLocationCallback
import com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.personalLoan.mGeoLocationRequestOrigin


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        // Retrieve intent data
        val intent = intent
        val personalDetailsData = intent.getSerializableExtra("personalDetails") as? PersonalDetails
        val productDetailsData = intent.getSerializableExtra("productDetails") as? ProductDetails

        // Process the intent data
        if (personalDetailsData != null) {
            Log.d("MainActivity", "Received personalDetailsData: $personalDetailsData")
            // Add your processing logic here
        }
        if (productDetailsData != null) {
            Log.d("MainActivity", "Received productDetailsData: $productDetailsData")
            // Add your processing logic here
        }


        setContent {
            FsTheme {
                if (personalDetailsData != null && productDetailsData != null) {
                    val message = SpannableStringBuilder()
                        .append(SpannableString("Personal Details - \n\n").apply {
                            setSpan(StyleSpan(Typeface.BOLD), 0, length, Spanned
                                .SPAN_EXCLUSIVE_EXCLUSIVE)})
                        .append(personalDetailsData.toKeyValueSpannable())
                        .append(SpannableString("\n\nProduct Details - \n\n").apply {
                            setSpan(StyleSpan(Typeface.BOLD), 0, length, Spanned
                                .SPAN_EXCLUSIVE_EXCLUSIVE)})
                        .append(productDetailsData.toKeyValueSpannable())

                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Details Received")
                        .setMessage(message)  // Use SpannableStringBuilder here
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            setContent {
                                FsTheme {
                                    LaunchScreen(AppScreens.SplashScreen.route)
                                }
                            }
                        }
                        .show()
                } else {
                    LaunchScreen(AppScreens.SplashScreen.route)
                }
            }
        }

    }
    fun PersonalDetails.toKeyValueSpannable(): SpannableStringBuilder {
        val spannableBuilder = SpannableStringBuilder()
        this::class.java.declaredFields.forEach { field ->
            field.isAccessible = true
            if (!field.name.startsWith("$")) {
                val value = field.get(this)?.toString() ?: "N/A"

                val key = "${field.name}: "
                val boldValue = SpannableString(value).apply {
                    setSpan(StyleSpan(Typeface.BOLD), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                spannableBuilder.append(key).append(boldValue).append("\n")
            }
        }
        return spannableBuilder
    }



    fun ProductDetails.toKeyValueSpannable(): SpannableStringBuilder {
        val spannableBuilder = SpannableStringBuilder()
        this::class.java.declaredFields.forEach { field ->
            field.isAccessible = true
            if (!field.name.startsWith("$")) {
                val value = field.get(this)?.toString() ?: "N/A"

                val key = "${field.name}: "
                val boldValue = SpannableString(value).apply {
                    setSpan(StyleSpan(Typeface.BOLD), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                spannableBuilder.append(key).append(boldValue).append("\n")
            }
        }
        return spannableBuilder
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
    }
}
