package com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.BuildConfig
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository.handleAuthGetAccessTokenApi
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.AuthOtp
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.DeviceInfo
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.ForgotPasswordOtpVerify
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.LoginDetails
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.storage.TokenManager
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.BaseViewModel
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
//import com.github.sugunasriram.myfisloanlibone.BuildConfig.FLAVOR_NAME


enum class DeviceType(val type: String) {
    UNKNOWN("UNKNOWN"),
    PHONE("PHONE"),
    TABLET("TABLET"),
    TV("TV"),
    DESKTOP("DESKTOP")
}

enum class DeviceStatus(val status: String) {
    SIMULATOR("SIMULATOR"),
    REAL("REAL")
}

enum class OSName(val os: String) {
    ANDROID("ANDROID"),
    IOS("IOS"),
    IPADOS("IPADOS"),
    WEB("WEB"),
    WINDOWS("WINDOWS")
}
class OtpViewModel : BaseViewModel() {

    private val _showInternetScreen = MutableLiveData(false)
    val showInternetScreen: LiveData<Boolean> = _showInternetScreen

    private val _showTimeOutScreen = MutableLiveData(false)
    val showTimeOutScreen: LiveData<Boolean> = _showTimeOutScreen

    private val _showServerIssueScreen = MutableLiveData(false)
    val showServerIssueScreen: LiveData<Boolean> = _showServerIssueScreen

    private val _unAuthorizedUser = MutableLiveData(false)
    val unAuthorizedUser: LiveData<Boolean> = _unAuthorizedUser

    private val _unexpectedError = MutableLiveData(false)
    val unexpectedError: LiveData<Boolean> = _unexpectedError

    private val _otp: MutableLiveData<String> = MutableLiveData("")
    val otp: LiveData<String> = _otp

    private val _otpError: MutableLiveData<String?> = MutableLiveData("")
    val otpError: LiveData<String?> = _otpError

    fun updateOtpError(otpError: String?) {
        _otpError.value = otpError
    }

    private val _generalError: MutableLiveData<String?> = MutableLiveData("")
    val generalError: LiveData<String?> = _generalError
    fun updateGeneralError(errorMsg: String?) {
        _generalError.value = errorMsg
    }
    fun clearMessage(newData: String? = null) {
        updateGeneralError(newData)
    }

    private val _isSignUpOtpLoading = MutableStateFlow(false)
    val isSignUpOtpLoading: StateFlow<Boolean> = _isSignUpOtpLoading

    private val _isSignUpOtpLoadingSuccess = MutableStateFlow(false)
    val isSignUpOtpLoadingSuccess: StateFlow<Boolean> = _isSignUpOtpLoadingSuccess

    private val _signUpAuthOtpResponse = MutableStateFlow<AuthOtp?>(null)
    val signUpAuthOtpResponse: StateFlow<AuthOtp?> = _signUpAuthOtpResponse

    private val _isLoginOtpLoading = MutableStateFlow(false)
    val isLoginOtpLoading: StateFlow<Boolean> = _isLoginOtpLoading

    private val _isLoginOtpLoadingSuccess = MutableStateFlow(false)
    val isLoginOtpLoadingSuccess: StateFlow<Boolean> = _isLoginOtpLoadingSuccess

    private val _loginAuthOtpResponse = MutableStateFlow<AuthOtp?>(null)
    val loginAuthOtpResponse: StateFlow<AuthOtp?> = _loginAuthOtpResponse

    private val _navigationToSignup = MutableStateFlow(false)
    val navigationToSignIn: StateFlow<Boolean> = _navigationToSignup

    private fun signUpAuthOtpApi(orderId: String, otp: String, context: Context, navController: NavHostController) {
        _isSignUpOtpLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleSignUpAuthOtpApi(orderId, otp, context, navController)
        }
    }

    private suspend fun handleSignUpAuthOtpApi(orderId: String, otp: String, context: Context,
                                               navController: NavHostController,
                                               checkForAccessToken: Boolean=true) {
        kotlin.runCatching {
            ApiRepository.authOtp(orderId, otp)
        }.onSuccess { response ->
            response?.let {
                handleSignUpAuthOtpSuccessResponse(response)
            }
        }.onFailure { error ->
            // Session Management
            if (error is ResponseException &&
                error.response.status.value == 401) {
                //Get Access Token using RefreshToken
                if (checkForAccessToken && handleAuthGetAccessTokenApi()){
                    handleSignUpAuthOtpApi(orderId, otp, context, navController,false)
                }else{
                    // If unable to refresh the token, navigate to the sign-in page
                    _navigationToSignup.value = true
                }
            }else {
                handleAuthOtpFailure(error, context)
            }
        }
    }

    private suspend fun handleSignUpAuthOtpSuccessResponse(response: AuthOtp) {
        withContext(Dispatchers.Main) {
            _signUpAuthOtpResponse.value = response
            _isSignUpOtpLoading.value = false
            _isSignUpOtpLoadingSuccess.value = true
            response.data?.accessToken?.let { accessToken ->
                TokenManager.save("accessToken", accessToken)
            }
            response.data?.refreshToken?.let { refreshToken ->
                TokenManager.save("refreshToken", refreshToken)
            }
            response.data?.sseId?.let { sseId ->
                TokenManager.save("sseId",sseId)
            }
        }
    }
    @SuppressLint("HardwareIds")
    fun getDeviceInfo(context: Context, configuration: Configuration): DeviceInfo {
        val isEmulator = Build.FINGERPRINT.contains("generic") || Build.PRODUCT.contains("sdk")

        val deviceType = when {
            isTv(context) -> DeviceType.TV.type
            configuration.screenWidthDp >= 600 -> DeviceType.TABLET.type
            else -> DeviceType.PHONE.type
        }

        val deviceName = Settings.Secure.getString(context.contentResolver, "device_name")
            ?: Settings.Global.getString(context.contentResolver, "device_name")
            ?: Settings.System.getString(context.contentResolver, "bluetooth_name")
            ?: Settings.Secure.getString(context.contentResolver, "bluetooth_name")
            ?: Build.MODEL

        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        return DeviceInfo(
            brand = Build.BRAND,
            deviceName = deviceName,
            deviceType = deviceType,
            isDevice = if (isEmulator) DeviceStatus.SIMULATOR.status else DeviceStatus.REAL.status,
            manufacturer = Build.MANUFACTURER,
            modelId = Build.ID,
            modelName = Build.MODEL,
            osName = OSName.ANDROID.os,
            androidId = androidId?:"UNKNOWN",
            osVersion = Build.VERSION.RELEASE,
            platformApiLevel = Build.VERSION.SDK_INT.toString()
        )
    }

    private fun isTv(context: Context): Boolean {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        return uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
    }
    private fun loginAuthOtpApi(orderId: String, otp: String, context: Context, navController: NavHostController,deviceInfo: DeviceInfo) {
        //PreProd
        val loginDetails= LoginDetails(orderId,otp,deviceInfo)
        //Prod
//        val loginDetails= LoginDetails(orderId,otp)
//        val flavour = "${BuildConfig.FLAVOR_NAME}"

        _isLoginOtpLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleLoginAuthOtpApi(context, navController,loginDetails)
        }
    }

    private suspend fun handleLoginAuthOtpApi( context: Context,
                                               navController: NavHostController,loginDetails: LoginDetails,
                                               checkForAccessToken: Boolean=true) {
        kotlin.runCatching {
            ApiRepository.login(loginDetails)
        }.onSuccess { response ->
            response?.let {
                handleLoginAuthOtpSuccessResponse(response)
            }
        }.onFailure { error ->
            // Session Management
            if (error is ResponseException &&
                error.response.status.value == 401) {
                //Get Access Token using RefreshToken
                if (checkForAccessToken && handleAuthGetAccessTokenApi()){
                    handleLoginAuthOtpApi( context, navController,loginDetails,false)
                }else{
                    // If unable to refresh the token, navigate to the sign-in page
                    _navigationToSignup.value = true
                }
            }else {
                handleAuthOtpFailure(error, context)
            }
        }
    }

    private suspend fun handleLoginAuthOtpSuccessResponse(response: AuthOtp) {

        withContext(Dispatchers.Main) {
            _loginAuthOtpResponse.value = response
            _isLoginOtpLoading.value = false
            _isLoginOtpLoadingSuccess.value = true

            response.data?.accessToken?.let { accessToken ->
                TokenManager.save("accessToken", accessToken)
            }
            response.data?.refreshToken?.let { refreshToken ->
                TokenManager.save("refreshToken", refreshToken)
            }
            response.data?.sseId?.let { sseId ->
                TokenManager.save("sseId",sseId)
            }
        }
    }

    private suspend fun handleAuthOtpFailure(error: Throwable, context: Context) {
        withContext(Dispatchers.Main) {
            when (error) {
                is ClientRequestException -> CommonMethods().toastMessage(context,"Otp is invalid")
                is ResponseException -> { handleResponseExceptionAuthOtp(error, context) }
                is IOException -> _showInternetScreen.value = true
                is TimeoutCancellationException -> _showTimeOutScreen.value = true
                else -> _unexpectedError.value = true
            }
//            _isSignUpOtpLoadingSuccess.value = false
//            _isSignUpOtpLoading.value = false
            _isLoginOtpLoadingSuccess.value = false
            _isLoginOtpLoading.value = false
        }
    }

    private fun handleResponseExceptionAuthOtp(error: ResponseException, context: Context) {
        val statusCode = error.response.status.value
        when (statusCode) {
            400 -> {
                CommonMethods().toastMessage(context,context.getString(R.string.otp_is_invalid))
            }
            401 -> _unAuthorizedUser.value = true
            500 -> _showServerIssueScreen.value = true
            else -> _unexpectedError.value = true
        }
    }

    fun signUpOtpValidation(enteredOtp: String, orderId: String?, context: Context, navController: NavHostController) {
        clearMessage()
        when {
            enteredOtp.isBlank() -> CommonMethods().toastMessage(context,context.getString(R.string.enter_the_otp))
            enteredOtp.trim().length < 4 -> CommonMethods().toastMessage(context,context.getString(R.string.enter_valid_otp))
            else -> orderId?.let { signUpAuthOtpApi(it, enteredOtp.trim(), context, navController) }
        }
    }

    fun loginOtpValidation(enteredOtp: String, orderId: String?, context: Context, navController: NavHostController,deviceInfo: DeviceInfo) {
        clearMessage()
        when {
            enteredOtp.isBlank() -> CommonMethods().toastMessage(context,context.getString(R.string.enter_the_otp))
            enteredOtp.trim().length < 4 -> CommonMethods().toastMessage(context,context.getString(R.string.enter_valid_otp))
            else ->orderId?.let { loginAuthOtpApi(it, enteredOtp.trim(), context, navController,deviceInfo) }
        }
    }

    private val _forgotPasswordOtpVerifying = MutableStateFlow(false)
    val forgotPasswordOtpVerifying: StateFlow<Boolean> = _forgotPasswordOtpVerifying

    private val _forgotPasswordOtpVerified = MutableStateFlow(false)
    val forgotPasswordOtpVerified: StateFlow<Boolean> = _forgotPasswordOtpVerified

    private val _forgotPasswordOtpResponse = MutableStateFlow<ForgotPasswordOtpVerify?>(null)
    val forgotPasswordOtpResponse: StateFlow<ForgotPasswordOtpVerify?> = _forgotPasswordOtpResponse

    private fun forgotPasswordOtpApi(orderId: String, otp: String, context: Context) {
        _forgotPasswordOtpVerifying.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleForgotPasswordOtpApi(orderId,otp,context)
        }
    }

    private suspend fun handleForgotPasswordOtpApi(orderId: String, otp: String,
                                                   context: Context, checkForAccessToken: Boolean=true){
        kotlin.runCatching {
            ApiRepository.forgotPasswordOtpVerify(orderId, otp)
        }.onSuccess { response ->
            response?.let {
                handleForgotPasswordSuccess(response,context)
            }
        }.onFailure { error ->
            // Session Management
            if (checkForAccessToken &&
                error is ResponseException &&
                error.response.status.value == 401) {
                //Get Access Token using RefreshToken
                if (handleAuthGetAccessTokenApi()){
                    handleForgotPasswordOtpApi(orderId, otp, context, false)
                }else{
                    _navigationToSignup.value = true
                }
            }else {
                handleForgotPasswordFailure(error, context)
            }
        }
    }

    private suspend fun handleForgotPasswordSuccess(response: ForgotPasswordOtpVerify, context: Context) {
        withContext(Dispatchers.Main) {
            if (response.statusCode == 400) {
                _forgotPasswordOtpVerifying.value = false
                updateGeneralError(context.getString(R.string.otp_is_invalid))
            } else {
                _forgotPasswordOtpResponse.value = response
                _forgotPasswordOtpVerified.value = true
                _forgotPasswordOtpVerifying.value = false
            }
        }
    }

    private suspend fun handleForgotPasswordFailure(error: Throwable,context: Context){
        withContext(Dispatchers.Main) {
            when (error) {
                is ClientRequestException ->CommonMethods().toastMessage(context,"Otp is invalid")
                is ResponseException -> handleResponseExceptionForgot(context,error)
                is IOException -> _showInternetScreen.value = true
                is TimeoutCancellationException -> _showTimeOutScreen.value = true
                else -> _unexpectedError.value = true

            }
            _forgotPasswordOtpVerifying.value = false
        }
    }

    private suspend fun handleResponseExceptionForgot(context: Context, error: ResponseException) {
        val statusCode = error.response.status.value
        when (statusCode) {
            400 -> {
                CommonMethods().toastMessage(context,context.getString(R.string.otp_is_invalid))
            }
            401 -> _unAuthorizedUser.value = true
            500 -> _showServerIssueScreen.value = true
            else -> _unexpectedError.value = true
        }
    }

    fun forgotPasswordOtpValidation(otp: String, orderId: String, context: Context) {
        clearMessage()
        when {
            otp.trim().isEmpty() -> CommonMethods().toastMessage(context, context.getString(R.string.enter_the_otp))
            otp.trim().length < 4 -> CommonMethods().toastMessage(context,context.getString(R.string.enter_valid_otp))
            else ->  forgotPasswordOtpApi(orderId, otp, context)
        }
    }
}
