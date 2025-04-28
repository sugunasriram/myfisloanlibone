package com.github.sugunasriram.myfisloanlibone.fis_code.views.webview

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.WebViewTopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToFormRejectedScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanDisbursementScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiPaths
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.SSEData
import com.github.sugunasriram.myfisloanlibone.fis_code.network.sse.SSEViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val json1 = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

var redirectionSetForAgreement = false

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoanAgreementWebScreen(
    navController: NavHostController, transactionId: String,
    id: String, loanAgreementFormUrl:
    String, fromFlow: String
) {

    var lateNavigate = false
    val sseViewModel: SSEViewModel = viewModel()
    val sseEvents by sseViewModel.events.collectAsState()
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val errorTitle = stringResource(id = R.string.loan_agreement_failed)



    sseViewModel.startListening(ApiPaths().sse)
    Log.d("transactionId: ", transactionId)

    val handler = remember {
        Handler(Looper.getMainLooper()).apply {
            postDelayed(
                {
                    if (sseEvents.isEmpty()) {
                        if (!lateNavigate) {
                            navigateToLoanDisbursementScreen(
                                navController = navController, transactionId = transactionId,
                                id = id, fromFlow = fromFlow
                            )
                        }
                    }
                }, 3 * 60 * 1000L
            )
        }
    }

    LaunchedEffect(sseEvents) {
        if (sseEvents.isNotEmpty()) {
            handler.removeCallbacksAndMessages(null)
            try {
                val sseData = json1.decodeFromString<SSEData>(sseEvents)
                val sseTransactionId = sseData.data?.data?.txnId ;
                Log.d("LoanAgreement:", "transactionId :["+transactionId + "] " +
                        "sseTransactionId:["+ sseTransactionId)
                sseData.data?.data?.type.let { type ->
                    if (transactionId == sseTransactionId && type == "INFO") {
                        lateNavigate = true

                        //Check if Form Rejected or Pending
                        if (sseData.data?.data?.data?.error != null){
                            Log.d("LoanAgree", "Error :"+sseData.data?.data?.data?.error?.message)
                            errorMsg = sseData.data?.data?.data?.error?.message

                            navigateToFormRejectedScreen(
                                navController = navController,
                                errorTitle = errorTitle,
                                fromFlow = fromFlow, errorMsg=errorMsg
                            )
                        }else {
                            navigateToLoanDisbursementScreen(
                                navController = navController, transactionId = transactionId,
                                id = id, fromFlow = fromFlow
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SSEParsingError", "Error parsing SSE data", e)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WebViewTopBar(navController, title = "Loan Agreement")
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
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = object : WebViewClient() {
                        @Deprecated("Deprecated in Java")
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            url: String?
                        ): Boolean {
                            url?.let {
                                val delayDuration = 5 * 60 * 1000L
                                when {
                                    it.contains("https://uat-api.refo.dev/pss/esign/esigndone") || it.contains(
                                        "https://pramaan.ondc.org/beta/staging/mock/seller/toENach"
                                    ) -> {
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
                            evaluateJavascript("notifyAppFinished();", null)
                        }
                    }

                }
            },
            update = { webView ->
                webView.layoutParams = ViewGroup.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                webView.webChromeClient = object : WebChromeClient() {
                    override fun onCreateWindow(
                        view: WebView?, isDialog: Boolean,
                        isUserGesture: Boolean, resultMsg: Message?
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
                                        newWebView.visibility = View.GONE
                                        // Optionally, remove the newWebView from its parent and clean up
                                        (newWebView.parent as? ViewGroup)?.removeView(newWebView)
                                        newWebView.destroy()
                                        return true
                                    } else if (url == "https://yourredirecturl.com/failure") {
                                        newWebView.visibility = View.GONE
                                        (newWebView.parent as? ViewGroup)?.removeView(newWebView)
                                        newWebView.destroy()
                                        return true
                                    }
                                    return super.shouldOverrideUrlLoading(view, request)
                                }
                            }
                            newWebView.onFocusChangeListener =
                                View.OnFocusChangeListener { v, hasFocus ->
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
                webView.loadUrl(loanAgreementFormUrl)
            })
    }
}





