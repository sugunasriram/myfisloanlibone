package com.github.sugunasriram.myfisloanlibone.fis_code.views.webview

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.LoaderAnimation
import com.github.sugunasriram.myfisloanlibone.fis_code.components.WebViewTopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToAAConsentApprovalScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.finance.FinanceSearchModel
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstSearchBody
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstSearchResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.SearchBodyModel
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.SearchModel
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.WebViewModel

@SuppressLint("ResourceType")
@Composable
fun SearchWebViewScreen(
    navController: NavHostController, purpose: String, fromFlow: String
) {
    val context = LocalContext.current
    val webViewModel: WebViewModel = viewModel()

    val webScreenLoading = webViewModel.webProgress.collectAsState()
    val webScreenLoaded = webViewModel.webViewLoaded.collectAsState()
    val searchResponse by webViewModel.searchResponse.collectAsState()
    val gstSearchResponse by webViewModel.gstSearchResponse.collectAsState()
    val navigationToSignIn by webViewModel.navigationToSignIn.collectAsState()

    val showInternetScreen by webViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by webViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by webViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by webViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by webViewModel.unAuthorizedUser.observeAsState(false)
    val middleLoan by webViewModel.middleLoan.observeAsState(false)
    val errorMessage by webViewModel.errorMessage.collectAsState()

    when {
        navigationToSignIn -> navigateSignInPage (navController)
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        middleLoan -> CommonMethods().ShowMiddleLoanErrorScreen(navController, errorMessage, false)
        else -> {
            if (webScreenLoading.value) {
                LoaderAnimation(
                    image = R.raw.we_are_currently_processing,
                    updatedImage = R.raw.verify_monitoring_consent_success
                )
            } else {
                val endUse = if (purpose.equals("Other Consumption Purpose", ignoreCase = true))
                    "other"
                else if (purpose.equals("Consumer Durable Purchase", ignoreCase = true))
                    "consumerDurablePurchase"
                else purpose.lowercase()

                if (!webScreenLoaded.value) {
                    loadWebScreen(
                        fromFlow = fromFlow, webViewModel = webViewModel, context = context,
                        endUse = endUse, purpose = purpose
                    )
                } else {
                    NavigateToWebView(
                        searchResponse = searchResponse, gstSearchResponse = gstSearchResponse,
                        fromFlow = fromFlow, navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun NavigateToWebView(
    fromFlow: String, navController: NavHostController, searchResponse: SearchModel?,
    gstSearchResponse: GstSearchResponse?,
) {
    if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
        searchResponse?.let { search ->
            search.data?.transactionId?.let { transactionId ->
            search.data?.id?.let { id ->
                search.data.url?.let { url ->
                    SearchWebView(
                        navController = navController, transactionId = transactionId, urlToOpen =
                        url,
                        searchId = id, fromFlow = fromFlow, pageContent = {}
                    )
                }
            }
            }
        }
    } else if (fromFlow.equals("Invoice Loan", ignoreCase = true)) {
        gstSearchResponse.let { search ->
            search?.data?.transactionId?.let { transactionId ->
                search?.data?.id?.let { id ->
                    search.data.url?.let { url ->
                        SearchWebView(
                            navController = navController, transactionId = transactionId,
                            urlToOpen = url,
                            searchId = id, fromFlow = fromFlow, pageContent = {}
                        )
                    }
                }
            }
        }
    } else if (fromFlow.equals("Purchase Finance", ignoreCase = true)) {
        searchResponse?.let { search ->
            search.data?.transactionId?.let { transactionId ->
                search.data?.id?.let { id ->
                    search.data.url?.let { url ->
                        SearchWebView(
                            navController = navController, transactionId = transactionId,
                            urlToOpen = url,
                            searchId = id, fromFlow = fromFlow, pageContent = {}
                        )
                    }
                }
            }
        }
    }
}

fun loadWebScreen(
    fromFlow: String, webViewModel: WebViewModel, context: Context, endUse: String, purpose: String,
) {
    if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
        webViewModel.searchApi(
            context = context, searchBodyModel = SearchBodyModel(
                loanType = "PERSONAL_LOAN", endUse = endUse, bureauConsent = "on"
            )
        )
    } else if (fromFlow.equals("Invoice Loan", ignoreCase = true)) {
        // Here when it in the Gst Invoice Loan purpose equals to Invoice ID
        webViewModel.searchGst(
            gstSearchBody = GstSearchBody(
                loanType = "INVOICE_BASED_LOAN", bureauConsent = "on", tnc = "on",
                id = purpose
            ),
            context = context
        )
    } else if (fromFlow.equals("Purchase Finance", ignoreCase = true)) {
        webViewModel.financeSearch(
            financeSearchModel = FinanceSearchModel(
                loanType = "PURCHASE_FINANCE", bureauConsent = "on", tnc = "on", endUse = "travel",
                downpayment = "200", merchantGst = "24AAHFC3011G1Z4", merchantPan = "EGBQA2212D",
                isFinancing = "on", merchantBankAccountNumber = "639695357641006",
                merchantIfscCode = "XRSY0YPV5SW", merchantBankAccountHolderName = "mohan",
                productCategory = "fashion", productBrand = "style", productSKUID = "12345678",
                productPrice = "1000"
            ),
            context = context,
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SearchWebView(
    isSelfScrollable: Boolean = false, transactionId: String, navController: NavHostController,
    urlToOpen:
    String,
    searchId: String, fromFlow: String, pageContent: () -> Unit
) {
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
                            setLayerType(View.LAYER_TYPE_HARDWARE, null)

                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )

                            webViewClient = object : WebViewClient() {
                                @SuppressLint("SuspiciousIndentation")
                                @Deprecated("Deprecated in Java")
                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    url: String?
                                ): Boolean {
                                    url?.let {
                                        if (it.startsWith("http://www.example.com/external")) {
                                            val intent =
                                                Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                            context.startActivity(intent)
                                            return true
                                        }
                                        if (it.startsWith("https://stagingondcfs.jtechnoparks.in/ondc/buyer-finance/consent-callback/")) {
                                            navigateToAAConsentApprovalScreen(
                                                navController = navController, searchId = searchId,
                                                url = url, fromFlow = fromFlow
                                            )
                                            return true
                                        }
                                        view?.loadUrl(it)
                                    }
                                    return true
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                }
                            }
                        }
                    },
                    update = { webView ->
                        webView.webChromeClient = object : WebChromeClient() {
                            override fun onCreateWindow(
                                view: WebView?, isDialog: Boolean, isUserGesture: Boolean,
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
                        urlToOpen.let { webView.loadUrl(it) }
                    }
                )
            }
        }
    }
}



