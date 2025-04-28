package com.github.sugunasriram.myfisloanlibone.fis_code.views.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.WebViewTopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateTOUnexpectedErrorScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToFormRejectedScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiPaths
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.SSEData
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.SSEViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val json1 = Json { prettyPrint = true; ignoreUnknownKeys = true }
var redirectionSetForMandate = false


@Preview
@Composable
fun RepaymentWebScreenPreview() {
//    val url="https://uat-api.refo.dev/pss/enach/form/55d2808c-3a64-4ac4-a045-097893076e0c?sessionId=60Gj02NyiY8319fdEmebiYU2psHWz8uBLfnzQshiQDlVI5uEkEjkKlREnRpRqbnNf2xzIjcltFDaMXm3f374%2BYSyvZOSiuy3w950uc7BjBcAfIbfPigeCgtWHwyRzrEZX6%2B7Vcep%2BgD5%2B6cMrgw%2BJiREkEbNvw%3D%3D&redirectUrl=self"
//    val url="https://pramaan.ondc.org/beta/preprod/mock/seller/form/0fc30239-e533-48c8-ba15-4f6ce879c353"
//    val url = "https://pramaan.ondc.org/beta/preprod/mock/seller/toENach/3bd84358-c1f6-407f-91f3-97a3c794744f"
    val url="https://pramaan.ondc.org/beta/preprod/mock/seller/form/4583615d-7937-4857-8342-5bd303c47250"

    RepaymentWebScreen(
        navController = rememberNavController(), transactionId="transactionId",
        url = url, id = "id", fromFlow = "Personal Loan"
    ){}
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RepaymentWebScreen(
    navController: NavHostController, transactionId: String, url: String, id: String,
    fromFlow: String,
    isSelfScrollable: Boolean = false, pageContent: () -> Unit
) {

    val sseViewModel: SSEViewModel = viewModel()
    val sseEvents by sseViewModel.events.collectAsState()
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val errorTitle = stringResource(id = R.string.repayment_failed)
    var lateNavigate = false

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
                }, 3 * 60 * 1000
            )
        }
    }

    LaunchedEffect(sseEvents) {
        if (sseEvents.isNotEmpty()) {
            try {
                handler.removeCallbacksAndMessages(null)
                val sseData = json1.decodeFromString<SSEData>(sseEvents)

                sseData.data?.data?.type.let { type ->
                    val sseTransactionId = sseData.data?.data?.txnId
                    Log.d("Repayment:", "transactionId :["+transactionId + "] " +
                            "sseTransactionId:["+ sseTransactionId)
                    if (transactionId == sseTransactionId && type == "ACTION") {
                        sseData.data?.data?.catalog?.from_url?.let { formUrl ->
                            lateNavigate = true
                            if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
                                //Check if Form Rejected or Pending
                                if (sseData.data?.data?.data?.error != null){
                                    Log.d("RepaymentScreen", "Error :"+sseData.data?.data?.data?.error?.message)
                                    errorMsg = sseData.data?.data?.data?.error?.message

                                    navigateToFormRejectedScreen(
                                        navController = navController,
                                        fromFlow = fromFlow,
                                        errorTitle = errorTitle,
                                        errorMsg=errorMsg
                                    )
                                }else {
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
                                if (sseData.data?.data?.data?.error != null){
                                    Log.d("Repayment 2", "Error :"+sseData.data?.data?.data?.error?.message)
                                    errorMsg = sseData.data?.data?.data?.error?.message

                                    navigateToFormRejectedScreen(
                                        navController = navController,
                                        fromFlow = fromFlow, errorTitle = errorTitle,
                                        errorMsg=errorMsg
                                    )
                                }else {
                                    navigateToLoanProcessScreen(
                                        navController = navController,
                                        transactionId = transactionId,
                                        statusId = 16,
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
                        if (sseData.data?.data?.data?.error != null){
                            Log.d("RepaymentScreen 3", "Error :"+sseData.data?.data?.data?.error?.message)
                            errorMsg = sseData.data?.data?.data?.error?.message

                            navigateToFormRejectedScreen(
                                navController = navController,
                                fromFlow = fromFlow, errorTitle = errorTitle, errorMsg=errorMsg
                            )
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("SSEParsingError", "Error parsing SSE data", e)
            }
        }
    }
    var onAllActionsCompleted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        WebViewTopBar(navController, title = "Setup Repayment")
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (isSelfScrollable) {
                Column(modifier = Modifier.fillMaxSize()) {
                    pageContent()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                settings.apply {
                                    javaScriptEnabled = true
                                        safeBrowsingEnabled = true
                                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                    cacheMode = WebSettings.LOAD_DEFAULT
                                    domStorageEnabled = true
                                    allowFileAccess = true
                                    allowContentAccess = true
                                    allowFileAccessFromFileURLs = true
                                    allowUniversalAccessFromFileURLs = true
                                    javaScriptCanOpenWindowsAutomatically = true
                                }

                                settings.setSupportMultipleWindows(true)
                                // Enable hardware acceleration for better performance
                                setLayerType(View.LAYER_TYPE_HARDWARE, null)

                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )

                                addJavascriptInterface(object {
                                    @JavascriptInterface
                                    fun onFormSubmitted() {
                                        // This method will be called when the form is submitted
                                        Log.d("WebView", "Form submitted")
                                        // Handle the form submission event here
                                    }
                                }, "Android")

                                addJavascriptInterface(object {
                                    @JavascriptInterface
                                    fun onActionsCompleted() {
                                        // Notify the app that all actions are completed
                                        onAllActionsCompleted = true
                                        Log.d("WebView", "onActionsCompleted")

                                    }
                                }, "Android")


                                webViewClient = object : WebViewClient() {
                                    @Deprecated("Deprecated in Java")
                                    override fun shouldOverrideUrlLoading(
                                        view: WebView?, url: String?
                                    ): Boolean {
                                        url?.let {
                                            val delayDuration = 5 * 60 * 1000L
                                            when {
                                                it.contains("self") || it.contains("https://pramaan.ondc.org/beta/staging/mock/seller/toENach") -> {
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
//                                webViewClient = object : WebViewClient() {
//                                    @Deprecated("Deprecated in Java")
//                                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                                        url?.let {
//
//                                            if (url == "https://yourredirecturl.com/success") {
//                                                // Handle the success case - close the WebView and perform necessary actions
//
//                                                return true
//                                            } else if (url == "https://yourredirecturl.com/failure") {
//                                                // Handle the failure case
//
//                                                return true
//                                            }
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                                if (view != null) {
//                                                    view.webChromeClient?.onCreateWindow(
//                                                        view,
//                                                        false,
//                                                        false,
//                                                        null
//                                                    )
//                                                }
//                                            } else {
//                                                view?.loadUrl(it)
//                                            }
//                                            return true
//                                        }
//                                        return false
//                                    }
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        evaluateJavascript("notifyAppFinished();", null)
                                        view?.evaluateJavascript(
                                            "(function() { return document.body.scrollHeight; })();"
                                        ) { height ->
                                            val params = view.layoutParams
                                            if (height.toInt() < 1920)
                                                params.height = 2500
                                            else
                                                params.height = height.toInt()

                                            view.layoutParams = params
                                        }

                                    }

                                }

                            }
                        },
                        update = { webView ->
                            webView.webChromeClient = object : WebChromeClient() {
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
                                                if (url == "https://yourredirecturl.com/success") {
                                                    // Handle the success case - close the WebView and perform necessary actions
                                                    newWebView.visibility = View.GONE
                                                    // Optionally, remove the newWebView from its parent and clean up
                                                    (newWebView.parent as? ViewGroup)?.removeView(
                                                        newWebView
                                                    )
                                                    newWebView.destroy()
                                                    return true
                                                } else if (url == "https://yourredirecturl.com/failure") {
                                                    newWebView.visibility = View.GONE
                                                    (newWebView.parent as? ViewGroup)?.removeView(
                                                        newWebView
                                                    )
                                                    newWebView.destroy()
                                                    return true
                                                }

                                                return super.shouldOverrideUrlLoading(view, request)
                                            }


                                        }
                                        newWebView.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                                            if (hasFocus) {
                                                webView.visibility = View.GONE
                                            } else {
                                                webView.visibility = View.VISIBLE
                                            }
                                        }


                                    }


                                    val webSettings = newWebView?.settings
                                    if (webSettings != null) {
                                        webSettings.javaScriptEnabled = true

                                        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
                                        webSettings.setDomStorageEnabled(true)
                                        webSettings.setAllowFileAccess(true)
                                        webSettings.setAllowContentAccess(true)
                                        webSettings.setAllowFileAccessFromFileURLs(true)
                                        webSettings.setAllowUniversalAccessFromFileURLs(true)
                                        webSettings.setJavaScriptEnabled(true)
                                        webSettings.setSupportMultipleWindows(true)
                                        webSettings.setJavaScriptCanOpenWindowsAutomatically(true)
                                    }
                                    webSettings?.supportMultipleWindows()

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
                            webView.loadUrl(url)
                        })
                }
            }
        }
    }
}
