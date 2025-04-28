package com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.gstLoan

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository.handleAuthGetAccessTokenApi
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstOfferConfirmResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.BaseViewModel
import io.ktor.client.features.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GstBankDetailViewModel : BaseViewModel() {

    private val _showInternetScreen = MutableLiveData(false)
    val showInternetScreen: LiveData<Boolean> = _showInternetScreen

    private val _showTimeOutScreen = MutableLiveData(false)
    val showTimeOutScreen: LiveData<Boolean> = _showTimeOutScreen

    private val _showServerIssueScreen = MutableLiveData(false)
    val showServerIssueScreen: LiveData<Boolean> = _showServerIssueScreen

    private val _unexpectedError = MutableLiveData(false)
    val unexpectedError: LiveData<Boolean> = _unexpectedError

    private val _unAuthorizedUser = MutableLiveData(false)
    val unAuthorizedUser: LiveData<Boolean> = _unAuthorizedUser

    private val _middleLoan = MutableLiveData(false)
    val middleLoan: LiveData<Boolean> = _middleLoan

    private val _showLoader = MutableLiveData(false)
    val showLoader: LiveData<Boolean> = _showLoader

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun updateErrorMessage(message: String) {
        _errorMessage.value = message
    }

    private val _generalError: MutableLiveData<String?> = MutableLiveData("")
    val generalError: LiveData<String?> = _generalError

    fun updateGeneralError(errorMsg: String?) {
        _generalError.value = errorMsg
    }

    fun clearMessage(newData: String? = null) {
        updateGeneralError(newData)
    }

    private val _bankDetailCollecting = MutableStateFlow(false)
    val bankDetailCollecting: StateFlow<Boolean> = _bankDetailCollecting

    private val _bankDetailCollected = MutableStateFlow(false)
    val bankDetailCollected: StateFlow<Boolean> = _bankDetailCollected

    private val _bankDetailResponse = MutableStateFlow<GstOfferConfirmResponse?>(null)
    val bankDetailResponse: StateFlow<GstOfferConfirmResponse?> = _bankDetailResponse

    private val _navigationToSignIn = MutableStateFlow(false)
    val navigationToSignIn: StateFlow<Boolean> = _navigationToSignIn

    fun gstLoanApproved(id: String, loanType: String, context: Context) {
        _bankDetailCollecting.value = true
        viewModelScope.launch(Dispatchers.Main) {
            handleGstLoanApproved(id, loanType, context)
        }
    }

    private suspend fun handleGstLoanApproved(
        id: String, loanType: String, context: Context, checkForAccessToken: Boolean = true
    ) {
        kotlin.runCatching {
            ApiRepository.gstLoanApproved(id, loanType)
        }.onSuccess { response ->
            response?.let {
                handleGstLoanApprovedSuccess(response)
            }
        }.onFailure { error ->
            if (checkForAccessToken &&
                error is ResponseException &&
                error.response.status.value == 401
            ) {
                if (handleAuthGetAccessTokenApi()) {
                    handleGstLoanApproved(
                        id = id, loanType = loanType, context = context, checkForAccessToken = false
                    )
                } else {
                    _navigationToSignIn.value = true
                }
            } else {
                handleGstLoanApprovedFailure(error, context)
            }
        }
    }

    private suspend fun handleGstLoanApprovedSuccess(response: GstOfferConfirmResponse) {
        withContext(Dispatchers.Main) {
            _bankDetailCollecting.value = false
            _bankDetailCollected.value = true
            _bankDetailResponse.value = response
        }
    }

    private suspend fun handleGstLoanApprovedFailure(error: Throwable, context: Context) {
        withContext(Dispatchers.Main) {
            if (error is ResponseException) {
                CommonMethods().handleResponseException(
                    error = error, context = context, updateErrorMessage = ::updateErrorMessage,
                    _showServerIssueScreen = _showServerIssueScreen, _middleLoan = _middleLoan,
                    _unAuthorizedUser = _unAuthorizedUser, _unexpectedError = _unexpectedError,
                    _showLoader = _showLoader
                )
            } else {
                CommonMethods().handleGeneralException(
                    error = error, _showInternetScreen = _showInternetScreen,
                    _showTimeOutScreen = _showTimeOutScreen, _unexpectedError = _unexpectedError
                )
            }
            _bankDetailCollecting.value = false
        }
    }
}