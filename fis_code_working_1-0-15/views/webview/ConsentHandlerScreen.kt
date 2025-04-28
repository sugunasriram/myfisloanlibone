package com.github.sugunasriram.myfisloanlibone.fis_code.views.webview

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.WebViewTopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToDashboardScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.azureBlue

@Composable
fun ConsentHandlerScreen(
    isSelfScrollable: Boolean = false, showBottom: Boolean = false, id: String,
    navController: NavHostController, urlToOpen: String?, fromFlow: String, pageContent: () -> Unit
) {
    val formSubmitJavaScript = """
        console.log("Adding form submit listeners");
        var forms = document.getElementsByTagName('form');
        console.log("Number of forms found: " + forms.length);
        for (var i = 0; i < forms.length; i++) {
            console.log("Adding submit listener to form " + i);
            forms[i].addEventListener('submit', function(event) {
                console.log("Form " + i + " submitted");
                Android.onFormSubmitted();
            });
        }
    """.trimIndent()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val title = if (fromFlow.equals("Personal Loan")) {
            fromFlow
        } else if (fromFlow.equals("Purchase Finance")) {
            "Purchase Finance"
        } else {
            "GST Invoice Loan"
        }
        WebViewTopBar(navController, title = title)
        if (isSelfScrollable) {
            Column(modifier = Modifier.weight(1f)) {
                pageContent()
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            //Sugu - need to test with other lender, commented for Lint
                            settings.javaScriptEnabled = true
                            settings.setSupportMultipleWindows(true)

                            addJavascriptInterface(object {
                                @JavascriptInterface
                                fun onFormSubmitted() {
                                    Log.d("WebView", "Form submitted")
                                }
                            }, "Android")

                            // Enable hardware acceleration for better performance
                            setLayerType(View.LAYER_TYPE_HARDWARE, null)

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
                                        if (it.startsWith("http://www.example.com/external")) {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                            context.startActivity(intent)
                                            return true
                                        }
                                        if (it.startsWith("https://stagingondcfs.jtechnoparks.in/ondc/buyer-finance/consent-callback/")) {
                                            Log.v("Redirect URL ==>", it)
                                            val encodedUrl = Uri.encode(url)
                                            navigateToDashboardScreen(
                                                navController = navController, id = id,
                                                consentHandler = "2", fromFlow
                                            )
                                            return true
                                        }
                                        view?.loadUrl(it)
                                    }
                                    return true
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    view?.evaluateJavascript(formSubmitJavaScript) { result ->
                                        Log.d("WebView", "Injected JS result: $result")
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
                                newWebView?.webViewClient = object : WebViewClient() {
                                    override fun shouldOverrideUrlLoading(
                                        view: WebView,
                                        request: WebResourceRequest
                                    ): Boolean {
                                        val url = request.url.toString()
                                        if (url == "https://yourredirecturl.com/success") {
                                            newWebView?.visibility = View.GONE
                                            (newWebView?.parent as? ViewGroup)?.removeView(
                                                newWebView
                                            )
                                            newWebView?.destroy()
                                            return true
                                        } else if (url == "https://yourredirecturl.com/failure") {
                                            newWebView?.visibility = View.GONE
                                            (newWebView?.parent as? ViewGroup)?.removeView(
                                                newWebView
                                            )
                                            newWebView?.destroy()
                                            return true
                                        }
                                        return super.shouldOverrideUrlLoading(view, request)
                                    }
                                }
                                newWebView?.onFocusChangeListener =
                                    View.OnFocusChangeListener { _, hasFocus ->
                                        webView.visibility =
                                            if (hasFocus) View.GONE else View.VISIBLE
                                    }
                                newWebView?.settings?.apply {
                                    javaScriptEnabled = true
                                    cacheMode = WebSettings.LOAD_NO_CACHE
                                    domStorageEnabled = true
                                    allowFileAccess = true
                                    allowContentAccess = true
                                    allowFileAccessFromFileURLs = true
                                    allowUniversalAccessFromFileURLs = true
                                    supportMultipleWindows()
                                    javaScriptCanOpenWindowsAutomatically = true
                                }
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
                        urlToOpen?.let { webView.loadUrl(it) }
                    }
                )
                if (showBottom) {
                    CurvedPrimaryButtonFull(
                        text = stringResource(id = R.string.accept),
                        modifier = Modifier.padding(
                            start = 30.dp, end = 30.dp, top = 30.dp, bottom = 30.dp
                        ),
                        backgroundColor = azureBlue, textColor = Color.White
                    ) {}
                }
            }
        }
    }
}