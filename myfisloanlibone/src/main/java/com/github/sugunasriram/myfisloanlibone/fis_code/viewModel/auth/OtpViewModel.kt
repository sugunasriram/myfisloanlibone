package com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository.handleAuthGetAccessTokenApi
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.AuthOtp
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.ForgotPasswordOtpVerify
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isLoadingSucess = MutableStateFlow(false)
    val isLoadingSucess: StateFlow<Boolean> = _isLoadingSucess

    private val _authOtpResponse = MutableStateFlow<AuthOtp?>(null)
    val authOtpResponse: StateFlow<AuthOtp?> = _authOtpResponse

    private val _navigationToSignup = MutableStateFlow(false)
    val navigationToSignIn: StateFlow<Boolean> = _navigationToSignup

    private fun authOtpApi(orderId: String, otp: String, context: Context, navController: NavHostController) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleAuthOtpApi(orderId, otp, context, navController)
        }
    }

    private suspend fun handleAuthOtpApi(orderId: String, otp: String, context: Context,
                                         navController: NavHostController,
                                         checkForAccessToken: Boolean=true) {
        kotlin.runCatching {
            ApiRepository.authOtp(orderId, otp)
        }.onSuccess { response ->
            response?.let {
                handleAuthOtpSuccessResponse(response)
            }
        }.onFailure { error ->
            // Session Management
            if (error is ResponseException &&
                error.response.status.value == 401) {
                //Get Access Token using RefreshToken
                if (checkForAccessToken && handleAuthGetAccessTokenApi()){
                    handleAuthOtpApi(orderId, otp, context, navController,false)
                }else{
                    // If unable to refresh the token, navigate to the sign-in page
                    _navigationToSignup.value = true
                }
            }else {
                handleAuthOtpFailure(error, context)
            }
        }
    }

    private suspend fun handleAuthOtpSuccessResponse(response: AuthOtp) {
        withContext(Dispatchers.Main) {
            _authOtpResponse.value = response
            _isLoading.value = false
            _isLoadingSucess.value = true
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
            _isLoadingSucess.value = false
            _isLoading.value = false
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

    fun otpValidation(enteredOtp: String, orderId: String?, context: Context, navController: NavHostController) {
        clearMessage()
        when {
            enteredOtp.isBlank() -> CommonMethods().toastMessage(context,context.getString(R.string.enter_the_otp))
            enteredOtp.trim().length < 4 -> CommonMethods().toastMessage(context,context.getString(R.string.enter_valid_otp))
            else -> orderId?.let { authOtpApi(it, enteredOtp.trim(), context, navController) }
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
