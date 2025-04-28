package com.github.sugunasriram.myfisloanlibone.fis_code.views.webview.personalLoan

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.Message
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.fis_code.components.WebViewTopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToPrePaymentStatusScreen
import kotlinx.serialization.json.Json

private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PrePartPaymentWebView(
    navController: NavHostController, url: String, orderId: String, status: String,
    fromFlow: String
) {
    Column(modifier = Modifier.fillMaxSize()) {
        WebViewTopBar(navController, title = "PrePart Payment")
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    configureWebViewSettings()

                    Handler(Looper.getMainLooper()).postDelayed({
                        navigateToPrePaymentStatusScreen(
                            navController = navController, orderId = orderId, headerText = status,
                            fromFlow = fromFlow
                        )
                    }, 5000)
                    // Enable hardware acceleration for better performance
                    setLayerType(View.LAYER_TYPE_HARDWARE, null)

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = createWebViewClient(
                        navController = navController, orderId = orderId,
                        status = status, fromFlow = fromFlow
                    )
                    webChromeClient = createWebChromeClient()
                }
            },
            update = { webView ->
                webView.layoutParams = ViewGroup.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                webView.loadUrl(url)
            }
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun WebView.configureWebViewSettings() {
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
        setSupportMultipleWindows(true)
    }
}

private fun createWebViewClient(
    navController: NavHostController, orderId: String, status: String, fromFlow: String
) = object : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url?.let {
            handleUrlLoading(navController, orderId, status, url, fromFlow = fromFlow)
            view?.loadUrl(it)
            return true
        }
        return false
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.evaluateJavascript("notifyAppFinished();", null)
    }
}

private fun handleUrlLoading(
    navController: NavHostController, orderId: String, status: String, url: String,
    fromFlow: String
) {
    val delay = 5000L
    if (url.contains("https://pramaan.ondc.org/beta/staging/mock/seller/toPaymentUtility?")) {
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToPrePaymentStatusScreen(
                navController = navController, orderId = orderId, headerText = status,
                fromFlow = fromFlow
            )
        }, delay)
    }

    if (url.contains("https://pramaan.ondc.org/beta/staging/mock/seller/toPaymentUtility?amount=451300&order_id=f695e959-3ae2-4053-b5a3-7df64c577e50&label=FORECLOSURE")) {
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToPrePaymentStatusScreen(
                navController = navController, orderId = orderId, headerText = status,
                fromFlow = fromFlow
            )
        }, delay)
    }
}

private fun createWebChromeClient() = object : WebChromeClient() {
    override fun onCreateWindow(
        view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?
    ): Boolean {
        val newWebView = view?.context?.let { WebView(it) }
        newWebView?.apply {
            configureWebViewSettings()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView, request: WebResourceRequest
                ): Boolean {
                    val url = request.url.toString()
                    if (url == "https://yourredirecturl.com/success" || url == "https://yourredirecturl.com/failure") {
                        visibility = View.GONE
                        (parent as? ViewGroup)?.removeView(this@apply)
                        destroy()
                        return true
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }
        }

        view?.addView(newWebView)
        (resultMsg?.obj as? WebView.WebViewTransport)?.webView = newWebView
        resultMsg?.sendToTarget()
        return true
    }

    override fun onCloseWindow(window: WebView?) {
        super.onCloseWindow(window)
        (window?.parent as? ViewGroup)?.removeView(window)
        window?.destroy()
    }
}
