package com.github.sugunasriram.myfisloanlibone.fis_code.views.webview

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.app.MainActivity
import com.github.sugunasriram.myfisloanlibone.fis_code.components.WebViewTopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateTOUnexpectedErrorScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToFormRejectedScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiPaths
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.SSEData
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.SSEViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val json1 = Json { prettyPrint = true; ignoreUnknownKeys = true }
private var filePathCallbackFn: ValueCallback<Array<Uri>>? = null
private var uploadMessage: ValueCallback<Uri>? = null
var mGeoLocationRequestOriginRepayment: String? = null
var mGeoLocationCallbackRepayment: GeolocationPermissions.Callback? = null

//@Preview
//@Composable
//fun RepaymentWebScreenPreview() {
////    val url="https://uat-api.refo.dev/pss/enach/form/55d2808c-3a64-4ac4-a045-097893076e0c?sessionId=60Gj02NyiY8319fdEmebiYU2psHWz8uBLfnzQshiQDlVI5uEkEjkKlREnRpRqbnNf2xzIjcltFDaMXm3f374%2BYSyvZOSiuy3w950uc7BjBcAfIbfPigeCgtWHwyRzrEZX6%2B7Vcep%2BgD5%2B6cMrgw%2BJiREkEbNvw%3D%3D&redirectUrl=self"
////    val url="https://pramaan.ondc.org/beta/preprod/mock/seller/form/0fc30239-e533-48c8-ba15-4f6ce879c353"
////    val url = "https://pramaan.ondc.org/beta/preprod/mock/seller/toENach/3bd84358-c1f6-407f-91f3-97a3c794744f"
//    val url="https://pramaan.ondc.org/beta/preprod/mock/seller/form/4583615d-7937-4857-8342-5bd303c47250"
//
//    RepaymentWebScreen(
//        navController = rememberNavController(), transactionId="transactionId",
//        url = url, id = "id", fromFlow = "Personal Loan"
//    ){}
//}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RepaymentWebScreen(
    navController: NavHostController, transactionId: String, url: String, id: String,
    fromFlow: String,
    isSelfScrollable: Boolean = false, pageContent: () -> Unit
) {

    val sseViewModel: SSEViewModel = viewModel()
    val sseEvents by sseViewModel.events.collectAsState(initial = "")
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val errorTitle = stringResource(id = R.string.repayment_failed)
    var lateNavigate = false

    val context = LocalContext.current
    val activity = context as Activity

    LaunchedEffect(Unit) {
        sseViewModel.startListening(ApiPaths().sse)
    }

    val handler = remember {
        Handler(Looper.getMainLooper()).apply {
            postDelayed(
                {
                    if (sseEvents.isEmpty()) {
                        if (!lateNavigate) {
                            navigateTOUnexpectedErrorScreen(
                                navController = navController, closeCurrent = true
                            )
                        }
                    }
                }, 5 * 60 * 1000
            )
        }
    }

    LaunchedEffect(sseEvents) {
        if (sseEvents.isNotEmpty()) {

            Log.d("RepaymentScreen", "SSE entered ")

                handler.removeCallbacksAndMessages(null)

                val sseData: SSEData? = try {
                    json1.decodeFromString<SSEData>(sseEvents)
                } catch (e: Exception) {
                    Log.e("SSEParsingError", "Error parsing SSE data", e)
                    null
                }

                if (sseData != null) {

                    sseData.data?.data?.type.let { type ->
                        val sseTransactionId =
                            sseData.data?.data?.txnId ?: sseData.data?.data?.catalog?.txn_id
                        Log.d(
                            "Repayment:", "transactionId :[" + transactionId + "] " +
                                    "sseTransactionId:[" + sseTransactionId
                        )
                        if (transactionId == sseTransactionId && type == "ACTION") {
                            sseData.data?.data?.catalog?.from_url?.let { formUrl ->
                                lateNavigate = true
                                if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
                                    //Check if Form Rejected or Pending
                                    if (sseData.data?.data?.data?.error != null) {
                                        Log.d(
                                            "RepaymentScreen",
                                            "Error :" + sseData.data?.data?.data?.error?.message
                                        )
                                        errorMsg = sseData.data?.data?.data?.error?.message

                                        navigateToFormRejectedScreen(
                                            navController = navController,
                                            fromFlow = fromFlow,
                                            errorTitle = errorTitle,
                                            errorMsg = errorMsg
                                        )
                                    } else {
                                        navigateToLoanProcessScreen(
                                            navController = navController,
                                            transactionId = transactionId,
                                            statusId = 6,
                                            offerId = id,
                                            responseItem = formUrl,
                                            fromFlow = fromFlow
                                        )
                                    }
                                } else {
                                    //Check if Form Rejected or Pending
                                    if (sseData.data?.data?.data?.error != null) {
                                        Log.d(
                                            "Repayment 2",
                                            "Error :" + sseData.data?.data?.data?.error?.message
                                        )
                                        errorMsg = sseData.data?.data?.data?.error?.message

                                        navigateToFormRejectedScreen(
                                            navController = navController,
                                            fromFlow = fromFlow, errorTitle = errorTitle,
                                            errorMsg = errorMsg
                                        )
                                    } else {
                                        navigateToLoanProcessScreen(
                                            navController = navController,
                                            transactionId = transactionId,
                                            statusId = if (fromFlow == "Purchase Finance") 22 else 16,
                                            offerId = id,
                                            responseItem = formUrl,
                                            fromFlow = fromFlow
                                        )
                                    }
                                }
                            }
                        }
                        if (transactionId == sseTransactionId && type == "INFO") {
                            //Check if Form Rejected or Pending
                            if (sseData.data?.data?.data?.error != null) {
                                Log.d(
                                    "RepaymentScreen 3",
                                    "Error :" + sseData.data?.data?.data?.error?.message
                                )
                                errorMsg = sseData.data?.data?.data?.error?.message

                                navigateToFormRejectedScreen(
                                    navController = navController,
                                    fromFlow = fromFlow,
                                    errorTitle = errorTitle,
                                    errorMsg = errorMsg
                                )
                            }
                        }
                }
            }


        }
    }
    var onAllActionsCompleted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        WebViewTopBar(navController, title = "Setup Repayment")

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
                ){
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
                                settings.safeBrowsingEnabled = true
//                            WebView.setWebContentsDebuggingEnabled(true);
                                settings.cacheMode = WebSettings.LOAD_DEFAULT

                                // Enable hardware acceleration for better performance
                                setLayerType(View.LAYER_TYPE_HARDWARE, null)
                                setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_BOUND, false)

                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )

                                isFocusable = true
                                isFocusableInTouchMode = true

                                webViewClient = object : WebViewClient() {
                                    override fun shouldOverrideUrlLoading(
                                        view: WebView?,
                                        url: String?
                                    ): Boolean {
                                        if (view != null && url != null) {
                                                    Log.d("WebView", "4 Sugu URL: $url")
                                                    view?.loadUrl(url)

                                                    return false
                                        }
                                        Log.d("WebView", "5 Sugu URL: $url")

                                        return true
                                    }

                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
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
                                val webSettings = webViewClient?.let { it1 -> settings }
                                if (webSettings != null) {
                                    webSettings.javaScriptEnabled = true

                                    webSettings.cacheMode = WebSettings.LOAD_DEFAULT
                                    webSettings.setDomStorageEnabled(true)
                                    webSettings.setAllowFileAccess(true)
                                    webSettings.setAllowContentAccess(true)
                                    webSettings.setAllowFileAccessFromFileURLs(true)
                                    webSettings.setAllowUniversalAccessFromFileURLs(true)
                                    webSettings.setJavaScriptEnabled(true)
                                    webSettings.setSupportMultipleWindows(true)
                                    webSettings.useWideViewPort = true
                                    webSettings.setJavaScriptCanOpenWindowsAutomatically(true)
                                    webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                    webSettings.safeBrowsingEnabled = true
                                }
                            }
                        },
                        update = { webView ->


                            webView.settings.setGeolocationEnabled(true)
                            webView.settings.setJavaScriptEnabled(true)
                            //Sugu - need to test with other lender, commented for Lint
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

                            // Apply layout parameters to the WebView
                            webView.layoutParams = ViewGroup.MarginLayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
                            webView.isFocusable = true
                            webView.isFocusableInTouchMode = true

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
                                                        mGeoLocationCallbackRepayment = callback
                                                        mGeoLocationRequestOriginRepayment = origin
                                                        ActivityCompat.requestPermissions(
                                                            activity,
                                                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                                            1001
                                                        )
                                                    })
                                                .show()

                                        } else {
                                            //no explanation need we can request the location
                                            mGeoLocationCallbackRepayment = callback
                                            mGeoLocationRequestOriginRepayment = origin
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

                                override fun onCreateWindow(
                                    view: WebView?,
                                    isDialog: Boolean,
                                    isUserGesture: Boolean,
                                    resultMsg: Message?
                                ): Boolean {
                                    val newWebView = view?.context?.let { WebView(it) }
                                    if (newWebView != null) {

                                        newWebView.webViewClient = object : WebViewClient() {
                                            override fun shouldOverrideUrlLoading(
                                                view: WebView,
                                                request: WebResourceRequest
                                            ): Boolean {
                                                val url = request.url.toString()
                                                if (view != null && url != null) {
                                                    Log.d("WebView", "1 Sugu URL: $url")
                                                    view.loadUrl(url)

                                                    return false
                                                } else {
                                                    Log.d("WebView", "0 Sugu request: $request")

                                                    return super.shouldOverrideUrlLoading(view, request)
                                                }
                                            }

                                            override fun shouldOverrideUrlLoading(
                                                view: WebView?,
                                                url: String?
                                            ): Boolean {
                                                if (view != null && url != null) {
                                                    Log.d("WebView", "2 Sugu URL: $url")

                                                    view.loadUrl(url)
                                                    return false
                                                }
                                                Log.d("WebView", "2a Sugu URL: $url")

                                                return true
                                            }

                                        }
                                    }


                                    val webSettings = newWebView?.settings
                                    if (webSettings != null) {
                                        webSettings.javaScriptEnabled = true
                                        webSettings.domStorageEnabled = true
                                        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                        webSettings.setAllowFileAccess(true)
                                        webSettings.setAllowContentAccess(true)
                                        webSettings.setAllowFileAccessFromFileURLs(true)
                                        webSettings.setAllowUniversalAccessFromFileURLs(true)
                                        webSettings.setSupportMultipleWindows(true)
                                        webSettings.setJavaScriptCanOpenWindowsAutomatically(true)
                                        webSettings.setGeolocationEnabled(true)
                                        webSettings.loadsImagesAutomatically = true

                                        webSettings.setSupportZoom(true)
                                        webSettings.builtInZoomControls = true
                                        webSettings.displayZoomControls = false

                                        webSettings.loadWithOverviewMode = true
                                        webSettings.javaScriptCanOpenWindowsAutomatically = true
                                        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
                                        webSettings.useWideViewPort = true
                                    }

                                    val webViewSettings = view?.settings
                                    if (webViewSettings != null) {
                                        webViewSettings.javaScriptEnabled = true
                                        webViewSettings.domStorageEnabled = true
                                        webViewSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                        webViewSettings.setAllowFileAccess(true)
                                        webViewSettings.setAllowContentAccess(true)
                                        webViewSettings.setAllowFileAccessFromFileURLs(true)
                                        webViewSettings.setAllowUniversalAccessFromFileURLs(true)
                                        webViewSettings.setSupportMultipleWindows(true)
                                        webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true)
                                        webViewSettings.setGeolocationEnabled(true)
                                        webViewSettings.loadsImagesAutomatically = true

                                        webViewSettings.setSupportZoom(true)
                                        webViewSettings.builtInZoomControls = true
                                        webViewSettings.displayZoomControls = false

                                        webViewSettings.loadWithOverviewMode = true
                                        webViewSettings.useWideViewPort = true
                                        webViewSettings.setSupportZoom(true)
                                        webViewSettings.builtInZoomControls = true

                                        webViewSettings.javaScriptCanOpenWindowsAutomatically = true
                                        webViewSettings.cacheMode = WebSettings.LOAD_DEFAULT
                                    }
//                                WebView.setWebContentsDebuggingEnabled(true)
                                    view?.layoutParams = ViewGroup.MarginLayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )



                                    // Apply layout parameters to the WebView
                                    newWebView?.layoutParams = ViewGroup.MarginLayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )

                                    newWebView?.isFocusable = true
                                    newWebView?.isFocusableInTouchMode = true

                                    val cookieManager = CookieManager.getInstance()

                                    // Enable cookies
                                    cookieManager.setAcceptCookie(true)

                                    // If you want to allow third-party cookies (optional)
                                    // Pass the WebView instance directly
                                    cookieManager.setAcceptThirdPartyCookies(newWebView, true)
                                    cookieManager.setAcceptThirdPartyCookies(view, true)
                                    newWebView?.setWebChromeClient(this);
                                    view?.addView(newWebView)
                                    val transport = resultMsg?.obj as? WebView.WebViewTransport
                                    transport?.webView = newWebView
                                    resultMsg?.sendToTarget()

                                    return true
                                }


                                override fun onCloseWindow(window: WebView?) {
                                    super.onCloseWindow(window)

                                    (window?.parent as? ViewGroup)?.removeView(window)
                                    window?.destroy()
                                }
                            }

                            val cookieManager = CookieManager.getInstance()

                            // Enable cookies
                            cookieManager.setAcceptCookie(true)

                            // If you want to allow third-party cookies (optional)
                            // Pass the WebView instance directly
                            cookieManager.setAcceptThirdPartyCookies(webView, true)
                            Log.d("WebView", "3 Sugu URL: $url")

                            webView.loadUrl(url)
                        }
                    )
                }
            }

    }
}
