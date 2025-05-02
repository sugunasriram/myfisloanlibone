package com.github.sugunasriram.myfisloanlibone.fis_code.appBridge

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.sugunasriram.myfisloanlibone.LoanLib
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.AppScreens
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.LaunchScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.views.auth.InAppUpdateScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.appBridge.IncomingIntentViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.OtpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.views.documents.PrivacyPolicyView


class AppBridgeManager(private val activity: ComponentActivity) {

    var updateCompleted = mutableStateOf(false)
    var verifySessionDone = false
    private val context: Context = activity.applicationContext


    @Composable
    fun RenderContent(intent: Intent?) {
        val personalDetails = intent?.getSerializableExtra("personalDetails") as? LoanLib.PersonalDetails
        val productDetails = intent?.getSerializableExtra("productDetails") as? LoanLib.ProductDetails
        val sessionDetails = intent?.getSerializableExtra("sessionDetails") as? LoanLib.SessionDetails

        val viewModel: IncomingIntentViewModel = viewModel()
        val isVerifySessionChecking by viewModel.isVerifySessionChecking.collectAsState()
        val isVerifySessionSuccess by viewModel.isVerifySessionSuccess.collectAsState()

        val showInternetScreen by viewModel.showInternetScreen.observeAsState(false)
        val showTimeOutScreen by viewModel.showTimeOutScreen.observeAsState(false)
        val showServerIssueScreen by viewModel.showServerIssueScreen.observeAsState(false)
        val unexpectedErrorScreen by viewModel.unexpectedError.observeAsState(false)
        val unAuthorizedUser by viewModel.unAuthorizedUser.observeAsState(false)

        // Log received data
        personalDetails?.let { Log.d("AppBridge", "Received personalDetails: $it") }
        productDetails?.let { Log.d("AppBridge", "Received productDetails: $it") }
        sessionDetails?.let { Log.d("AppBridge", "Received sessionDetails: $it") }

        when {
            showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController= rememberNavController())
            showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController= rememberNavController())
            showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController= rememberNavController())
            unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController= rememberNavController())
            unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController= rememberNavController())
            else -> {
                sessionDetails?.sessionId?.let { sessionId ->
                    Log.d("fisloanone", "Session ID: $sessionId")
                    if (!verifySessionDone) {
                        verifySessionDone = true
                        Log.d("fisloanone", "verifySession API : $sessionId")

                        viewModel.verifySessionApi(sessionId, context)
                    }

                    if (isVerifySessionChecking && isVerifySessionSuccess) {
                        Log.d("fisloanone", "Launching FIS Category Screen")

                        LaunchScreen(AppScreens.ApplyBycategoryScreen.route)
                    } else {
                        // Optionally show loading screen or nothing while waiting
                    }
                } ?: run {
                    if (updateCompleted.value) {
                        Log.d("fisloanone", "No Session ID found")

                        if (!verifySessionDone) {
                            Log.d("fisloanone", "Launch Splash screen")

                            LaunchScreen(AppScreens.SplashScreen.route)
                        }
                    } else {
                        Log.d("fisloanone", "InAppUpdateScreen")

                        InAppUpdateScreen(activity) {
                            updateCompleted.value = true
                        }
                    }
                }
            }
        }


    }




}

