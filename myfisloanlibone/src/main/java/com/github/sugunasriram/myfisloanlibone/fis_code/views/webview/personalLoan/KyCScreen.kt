package com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.personalLoan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.PermissionRequest
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.RENDERER_PRIORITY_BOUND
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.app.MainActivity
import com.github.sugunasriram.myfisloanlibone.fis_code.components.WebViewTopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToAnimationLoader
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToFormRejectedScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiPaths
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.SSEData
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.SSEViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


private val json1 = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}


private var filePathCallbackFn: ValueCallback<Array<Uri>>? = null
private var uploadMessage: ValueCallback<Uri>? = null
var redirectionSet = false
var mGeoLocationRequestOrigin: String? = null
var mGeoLocationCallback: GeolocationPermissions.Callback? = null


@Preview
@Composable
fun WebKycScreenPreview() {
//    val url="https://uat-api.refo.dev/pss/enach/form/9277121d-8413-4dba-ad74-efc3b8c531b2?sessionId=fiD32d21ip%2BqtbOyGMerSFzIKPBDpzwqXBZfch%2Fd%2Fr%2BiGQnrKBNfM22PZGZi8901NxQKC4qlU8wVqC3I8j76gQyBmtiy8iUzYIIIpagvfg7bPhy7RAtZvMp5WYh2jG0j%2F3rVkpgii3RAXwPhTLB4MT%2FtaJgf&redirectUrl=self"
//    var url = "https://ilpuat.finfotech.co.in/lms/ondc/kycuri?transactionId=d3d1c832-2848-51cf-bafa-9f53ca8c7985&status=1"
    var url = "https://ilpuat.finfotech.co.in/lms/ondc/kycuri?transactionId=1329fefc-86e5-5f4b-b60e-6d32a7674ef0&status=2"
    WebKycScreen(
        navController = rememberNavController(), transactionId="transactionId",
        url = url, id = "id", fromFlow = "Personal Loan"
    ){}
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebKycScreen(
    navController: NavHostController, transactionId: String, url: String, id: String, fromFlow:
    String,
    isSelfScrollable: Boolean = false, pageContent: () -> Unit
) {
    var lateNavigate = false
    val sseViewModel: SSEViewModel = viewModel()
    val sseEvents by sseViewModel.events.collectAsState()
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val errorTitle = stringResource(id = R.string.kyc_failed)



    sseViewModel.startListening(ApiPaths().sse)

    val handler = remember {
        Handler(Looper.getMainLooper()).apply {
            postDelayed({
                if (sseEvents.isEmpty()) {
                    if (!lateNavigate) {
                        Log.d("KyCScreen", "At looper - Sugu")

                        navigateToAnimationLoader(
                            navController = navController, transactionId= transactionId, id = id,
                            fromFlow = fromFlow
                        )
                    }
                }
            }, 15 * 60 * 1000) /* Changing to 15 mins, as P2P KYC is taking time, reduce back to 5
             mins later */
        }
    }

    ProceedWithKYCProcess(
        navController = navController, context = LocalContext.current, url = url, id = id,
        pageContent = pageContent, isSelfScrollable = isSelfScrollable,
    )

    if (sseEvents.isNotEmpty()) {
        handler.removeCallbacksAndMessages(null)
        try {
            val sseData = json1.decodeFromString<SSEData>(sseEvents)
            val sseTransactionId = sseData.data?.data?.txnId ;//tested
            Log.d("Kyc:", "transactionId :["+transactionId + "] " +
                    "sseTransactionId:["+ sseTransactionId)

            sseData.data?.data?.type.let { type ->
                if ( transactionId == sseTransactionId && (type == "ACTION" || type == "INFO")) {
                    Log.d("KyCScreen", "At SSE not empty - Sugu")
                    lateNavigate = true

                    //Check if Form Rejected or Pending
                    if (sseData.data?.data?.data?.error != null){
                        Log.d("KyCScreen", "Error :"+sseData.data?.data?.data?.error?.message)
                        errorMsg = sseData.data?.data?.data?.error?.message

                        navigateToFormRejectedScreen(
                            navController = navController,
                            errorTitle = errorTitle,
                            fromFlow = fromFlow, errorMsg=errorMsg
                        )
                    }else {
                        navigateToAnimationLoader(
                            navController = navController, transactionId = transactionId, id = id,
                            fromFlow = fromFlow
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SSEParsingError", "Error parsing SSE data", e)
        }
    }
}


@Composable
fun ProceedWithKYCProcess(
    navController: NavHostController, context: Context, url: String, id: String,
    pageContent: () -> Unit, isSelfScrollable: Boolean = false
) {
    val activity = context as Activity
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        WebViewTopBar(navController, title = "Kyc Verification")
        if (isSelfScrollable) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 0.dp)
            ) {
                pageContent()
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 0.dp)
            ) {

                val fileChooserLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri: Uri? ->
                    if (filePathCallbackFn != null) {
                        filePathCallbackFn?.onReceiveValue(uri?.let { arrayOf(it) })
                        filePathCallbackFn = null
                    } else if (uploadMessage != null) {
                        uploadMessage?.onReceiveValue(uri)
                        uploadMessage = null
                    }
                }

                AndroidView(


                    factory = { ctx ->
                        WebView(ctx).apply {
                            settings.setGeolocationEnabled(true)
                            //Sugu - need to test with other lender, commented for Lint
                            settings.javaScriptEnabled = true
                            settings.loadsImagesAutomatically = true
                            settings.domStorageEnabled = true
                            settings.allowFileAccess = true
                            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            settings.allowContentAccess = true
                            settings.allowFileAccessFromFileURLs = true
                            settings.allowUniversalAccessFromFileURLs = true

                            settings.setSupportZoom(true)
                            settings.builtInZoomControls = true
                            settings.displayZoomControls = false

                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            settings.setSupportMultipleWindows(true)
                            settings.javaScriptCanOpenWindowsAutomatically = true
                            settings.mediaPlaybackRequiresUserGesture = false



//                            settings.setNeedInitialFocus(true)

                            WebView.setWebContentsDebuggingEnabled(true);

                            settings.cacheMode = WebSettings.LOAD_DEFAULT

                            // Enable hardware acceleration for better performance
                            setLayerType(View.LAYER_TYPE_HARDWARE, null)
                            setRendererPriorityPolicy(RENDERER_PRIORITY_BOUND, false)


                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )


                            webViewClient = object : WebViewClient() {
                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    url: String?
                                ): Boolean {
                                    url?.let {
                                        when {
                                            it.contains("ekycdone") || it.contains("https://pramaan.ondc.org/beta/staging/mock/seller/toENach") -> {
                                                view?.loadUrl(it)
                                                return false
                                            }

                                            else -> {
                                                view?.loadUrl(it)
                                                return true
                                            }
                                        }
                                    }
                                    return false
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
//                                    evaluateJavascript("notifyAppFinished();", null)
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?
                                ) {
                                    super.onReceivedError(view, request, error)
                                }

                                override fun onReceivedHttpError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    errorResponse: WebResourceResponse?
                                ) {
                                    super.onReceivedHttpError(view, request, errorResponse)
                                }

                                override fun onReceivedSslError(
                                    view: WebView?, handler: SslErrorHandler?, error: SslError?
                                ) {
                                    super.onReceivedSslError(view, handler, error)
                                }

                                override fun onPageStarted(
                                    view: WebView?, url: String?, favicon: Bitmap?
                                ) {
                                    super.onPageStarted(view, url, favicon)
                                }

                                // For Android 4.1+
                                fun openFileChooser(
                                    uploadMsg: ValueCallback<Uri>,
                                    acceptType: String,
                                    capture: String
                                ) {
                                    uploadMessage = uploadMsg
                                    fileChooserLauncher.launch("*/*")
                                }

                                // For Android 3.0+
                                fun openFileChooser(
                                    uploadMsg: ValueCallback<Uri>,
                                    acceptType: String
                                ) {
                                    openFileChooser(uploadMsg, acceptType, "")
                                }

                                // For Android < 3.0
                                fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
                                    openFileChooser(uploadMsg, "*/*")
                                }
                            }


                        }
                    },
                    update = { webView ->
                        webView.settings.setGeolocationEnabled(true)
                        webView.settings.setJavaScriptEnabled(true)
                        //Sugu - need to test with other lender, commented for Lint
                        webView.settings.javaScriptEnabled = true
                        webView.settings.loadsImagesAutomatically = true
                        webView.settings.domStorageEnabled = true
                        webView.settings.allowFileAccess = true
                        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        webView.settings.allowContentAccess = true
                        webView.settings.allowFileAccessFromFileURLs = true
                        webView.settings.allowUniversalAccessFromFileURLs = true
                        webView.settings.setSupportZoom(true)

                        webView.settings.builtInZoomControls = true
                        webView.settings.displayZoomControls = false

                        webView.settings.loadWithOverviewMode = true
                        webView.settings.useWideViewPort = true
                        webView.settings.setSupportMultipleWindows(true)
                        webView.settings.javaScriptCanOpenWindowsAutomatically = true
                            webView.settings.safeBrowsingEnabled = true


                        // Apply layout parameters to the WebView
                        webView.layoutParams = ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

//                        webView.settings.setNeedInitialFocus(true)


                        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT


                        webView.webChromeClient = object : WebChromeClient() {
                            override fun onPermissionRequest(request: PermissionRequest) {
                                activity.runOnUiThread {
                                    when {
                                        request.resources.contains(PermissionRequest.RESOURCE_VIDEO_CAPTURE) -> {
                                            MainActivity.webPermissionRequest = request
                                            ActivityCompat.requestPermissions(
                                                activity,
                                                arrayOf(Manifest.permission.CAMERA),
                                                CommonMethods().CAMERA_PERMISSION_REQUEST_CODE
                                            )
                                        }

                                        request.resources.contains(PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID) -> {
                                            MainActivity.webPermissionRequest = request
//                                            ActivityCompat.requestPermissions(
//                                                activity,
//                                                arrayOf(
//                                                    Manifest.permission.ACCESS_FINE_LOCATION,
//                                                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                                                ),
//                                                1001
//                                            )

                                            val permissions = mutableListOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )

                                            // Add ACCESS_BACKGROUND_LOCATION only for API 29 and above
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                            }

                                            ActivityCompat.requestPermissions(
                                                activity,
                                                permissions.toTypedArray(),
                                                1002
                                            )
                                        }

                                        else -> {
                                            request.deny()
                                        }
                                    }
                                }
                            }

                            override fun onGeolocationPermissionsShowPrompt(
                                origin: String?,
                                callback: GeolocationPermissions.Callback?
                            ) {

                                if (ContextCompat.checkSelfPermission(
                                        activity,
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                    != PackageManager.PERMISSION_GRANTED
                                ) {

                                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                                            activity,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        )
                                    ) {
                                        AlertDialog.Builder(activity)
                                            .setMessage("Please turn ON the GPS to make app work smoothly")
                                            .setNeutralButton(
                                                android.R.string.ok,
                                                DialogInterface.OnClickListener { dialogInterface, i ->
                                                    mGeoLocationCallback = callback
                                                    mGeoLocationRequestOrigin = origin
                                                    ActivityCompat.requestPermissions(
                                                        activity,
                                                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                                        1001
                                                    )
                                                })
                                            .show()

                                    } else {
                                        //no explanation need we can request the location
                                        mGeoLocationCallback = callback
                                        mGeoLocationRequestOrigin = origin
                                        ActivityCompat.requestPermissions(
                                            activity,
                                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1002
                                        )
                                    }
                                } else {
                                    //tell the webview that permission has granted
                                    callback!!.invoke(origin, true, true)
                                }

                            }

                            private fun showPermissionDialog(request: PermissionRequest) {
                                AlertDialog.Builder(context)
                                    .setTitle("Camera Permission Request")
                                    .setMessage("This site wants to access your camera. Do you allow it?")
                                    .setPositiveButton("Allow") { _, _ ->
                                        ActivityCompat.requestPermissions(
                                            context, arrayOf
                                                (Manifest.permission.CAMERA),
                                            CommonMethods().CAMERA_PERMISSION_REQUEST_CODE
                                        )
                                    }
                                    .setNegativeButton("Deny") { _, _ ->
                                        request.deny()
                                    }
                                    .setCancelable(false)
                                    .show()
                            }

                            override fun onJsAlert(
                                view: WebView?,
                                url: String?,
                                message: String?,
                                result: JsResult?
                            ): Boolean {
                                return super.onJsAlert(view, url, message, result)
                            }

                            override fun onJsPrompt(
                                view: WebView?,
                                url: String?,
                                message: String?,
                                defaultValue: String?,
                                result: JsPromptResult?
                            ): Boolean {
                                return super.onJsPrompt(view, url, message, defaultValue, result)
                            }

                            override fun onJsConfirm(
                                view: WebView?,
                                url: String?,
                                message: String?,
                                result: JsResult?
                            ): Boolean {
                                return super.onJsConfirm(view, url, message, result)
                            }

                            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                                if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR) {
                                    Log.e("WebView JS Error", consoleMessage.message())
                                }
                                return super.onConsoleMessage(consoleMessage)
                            }


                            // For Android 5.0+
                            override fun onShowFileChooser(
                                webView: WebView,
                                filePathCallback: ValueCallback<Array<Uri>>,
                                fileChooserParams: FileChooserParams
                            ): Boolean {
                                filePathCallbackFn = filePathCallback
                                fileChooserLauncher.launch("*/*")
                                return true
                            }

                            // For Android 4.1+
                            fun openFileChooser(
                                uploadMsg: ValueCallback<Uri>,
                                acceptType: String,
                                capture: String
                            ) {
                                uploadMessage = uploadMsg
                                fileChooserLauncher.launch("*/*")
                            }

                            // For Android 3.0+
                            fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String) {
                                openFileChooser(uploadMsg, acceptType, "")
                            }

                            // For Android < 3.0
                            fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
                                openFileChooser(uploadMsg, "*/*")
                            }

                        }

                        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
                        //New
                        webView.setRendererPriorityPolicy(RENDERER_PRIORITY_BOUND, false)
                        webView.settings.mediaPlaybackRequiresUserGesture = false

//                        webView.clearCache(true)
                        val cookieManager = CookieManager.getInstance()

                        // Enable cookies
                        cookieManager.setAcceptCookie(true)

                        // If you want to allow third-party cookies (optional)
                            // Pass the WebView instance directly
                            cookieManager.setAcceptThirdPartyCookies(webView, true)


                        webView.loadUrl(url)
                    }
                )
            }
        }
    }
}


