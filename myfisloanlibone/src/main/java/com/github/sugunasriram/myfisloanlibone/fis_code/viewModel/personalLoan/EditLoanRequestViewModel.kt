package com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository.handleAuthGetAccessTokenApi
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstOfferConfirm
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstOfferConfirmResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.UpdateLoanAmountBody
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.personaLoan.UpdateResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.BaseViewModel
import com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan.loanAmountValue
import io.ktor.client.features.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class EditLoanRequestViewModel(maxAmount: String, minAmount: String, tenure: String) :
    BaseViewModel() {

    private val _showInternetScreen = MutableLiveData<Boolean>(false)
    val showInternetScreen: LiveData<Boolean> = _showInternetScreen

    private val _showTimeOutScreen = MutableLiveData<Boolean>(false)
    val showTimeOutScreen: LiveData<Boolean> = _showTimeOutScreen

    private val _showServerIssueScreen = MutableLiveData<Boolean>(false)
    val showServerIssueScreen: LiveData<Boolean> = _showServerIssueScreen

    private val _unexpectedError = MutableLiveData<Boolean>(false)
    val unexpectedError: LiveData<Boolean> = _unexpectedError

    private val _unAuthorizedUser = MutableLiveData<Boolean>(false)
    val unAuthorizedUser: LiveData<Boolean> = _unAuthorizedUser

    private val _middleLoan = MutableLiveData<Boolean>(false)
    val middleLoan: LiveData<Boolean> = _middleLoan

    private val _showLoader = MutableLiveData(false)
    val showLoader: LiveData<Boolean> = _showLoader

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _navigationToSignIn = MutableStateFlow(false)
    val navigationToSignIn: StateFlow<Boolean> = _navigationToSignIn

    fun updateErrorMessage(message: String) {
        _errorMessage.value = message
    }

    private val _loanAmount: MutableLiveData<Double> = MutableLiveData(maxAmount.toDoubleOrNull() ?: 0.0)
    var loanAmount: LiveData<Double> = _loanAmount

    val initialLoanBeginAmount = (minAmount.toDoubleOrNull() ?: 0.0)
    val initialLoanEndAmount = (maxAmount.toDoubleOrNull() ?: 0.0)

    private val _loanTenure: MutableLiveData<Int> = MutableLiveData(tenure.toIntOrNull())
    var loanTenure: LiveData<Int> = _loanTenure

    val initialLoanBeginTenure = (tenure.toIntOrNull() ?: 0) - 10
    val initialLoanEndTenure = (tenure.toIntOrNull() ?: 0) + 10

    private val _generalError: MutableLiveData<String?> = MutableLiveData("")
    val generalError: LiveData<String?> = _generalError

    private val _isEditProcess = MutableStateFlow(false)
    val isEditProcess: StateFlow<Boolean> = _isEditProcess

    private val _isEdited = MutableStateFlow(false)
    val isEdited: StateFlow<Boolean> = _isEdited

    private val _editLoanResponse = MutableStateFlow<UpdateResponse?>(null)
    val editLoanResponse: StateFlow<UpdateResponse?> = _editLoanResponse

    // Break down larger functions into smaller functions
    fun updateGeneralError(errorMsg: String?) {
        _generalError.value = errorMsg
    }

    fun clearMessage(newData: String? = null) {
        updateGeneralError(newData)
    }

    fun onLoanAmountChanged(context: Context, loanAmountInput: String) {
        val loanAmountWithoutSymbol = loanAmountInput.replace("₹", "").replace(",", "")
        _loanAmount.value = loanAmountWithoutSymbol.toDoubleOrNull() ?: 0.0
    }

    fun onloanTenureChanged(context: Context, tenureInput: String) {
        val tenureValue = tenureInput.substringBefore(" ").toIntOrNull() ?: 0
        _loanTenure.value = tenureValue
    }

    // Update loan maxAmount
    fun updateLoanAmount(updateLoanAmountBody: UpdateLoanAmountBody, context: Context) {
        _isEditProcess.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleUpdateLoanAmount(updateLoanAmountBody, context)
        }
    }

    private suspend fun handleUpdateLoanAmount(
        updateLoanAmountBody: UpdateLoanAmountBody, context: Context,
        checkForAccessToken: Boolean=true
    ) {
        kotlin.runCatching {
            ApiRepository.updateLoanAmount(updateLoanAmountBody)
        }.onSuccess { response ->
            response?.let {
                handleUpdateSuccess(response)
            }
        }.onFailure { error ->
            //Session Management
            if (checkForAccessToken &&
                error is ResponseException &&
                error.response.status.value == 401) {
                //Get Access Token using RefreshToken
                if (handleAuthGetAccessTokenApi()){
                    handleUpdateLoanAmount(updateLoanAmountBody, context, false)
                }else{
                    _navigationToSignIn.value = true
                }
            }else {
                handleFailure(error = error, context = context)
            }
        }
    }

    private suspend fun handleUpdateSuccess(response: UpdateResponse) {
        withContext(Dispatchers.Main) {
            _editLoanResponse.value = response
            _isEditProcess.value = false
            _isEdited.value = true
        }
    }

    fun checkValid(
        loanAmount: String, loanTenure: String, context: Context,
        updateLoanAmountBody: UpdateLoanAmountBody,
    ) {
        if (isInputValid(loanAmount, loanTenure, context)) {
            updateLoanAmount(updateLoanAmountBody, context)
        }
    }

    private fun isInputValid(loanAmount: String, loanTenure: String, context: Context): Boolean {
        val parsedIncome: Double = loanAmount.toDoubleOrNull() ?: 0.0
        val tenureValue: Int = loanTenure.substringBefore(" ").toIntOrNull() ?: 0

        return when {
            loanAmount.isEmpty() -> {
                CommonMethods().toastMessage(context, context.getString(R.string.loan_amount_message))
                false
            }
            loanTenure.isEmpty() -> {
                CommonMethods().toastMessage(context, context.getString(R.string.loan_tenure_message))
                false
            }
            parsedIncome < initialLoanBeginAmount || parsedIncome > initialLoanEndAmount -> {
                CommonMethods().toastMessage(context,context.getString(R.string.please_enter_valid_income_within_limits))
                false
            }
            parsedIncome <= 0 -> {
                CommonMethods().toastMessage(context,context.getString(R.string.please_enter_valid_income_within_limits))
                false
            }
            tenureValue < initialLoanBeginTenure || tenureValue > initialLoanEndTenure -> {
                CommonMethods().toastMessage(context,context.getString(R.string.please_enter_valid_tenure))
                false
            }
            tenureValue <= 0 -> {
                CommonMethods().toastMessage(context,context.getString(R.string.please_enter_valid_tenure))
                false
            }
            else -> true
        }
    }

    private val _gstOfferConfirmResponse = MutableStateFlow<GstOfferConfirmResponse?>(null)
    val gstOfferConfirmResponse: StateFlow<GstOfferConfirmResponse?> = _gstOfferConfirmResponse

    private fun gstOfferConfirmApi(gstOfferConfirm: GstOfferConfirm, context: Context){
        _isEditProcess.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleGstOfferConfirmApi(gstOfferConfirm, context)
        }
    }

    private suspend fun handleGstOfferConfirmApi(gstOfferConfirm: GstOfferConfirm, context:
    Context, checkForAccessToken: Boolean = true) {
        kotlin.runCatching {
            ApiRepository.gstConfirmOffer(gstOfferConfirm)
        }.onSuccess { response ->
            response?.let {
                handleGstOfferConfirmApiSuccess(response)
            }
        }.onFailure { error ->
            //Session Management
            if (checkForAccessToken &&
                error is ResponseException &&
                error.response.status.value == 401) {
                //Get Access Token using RefreshToken
                if (handleAuthGetAccessTokenApi()){
                    handleGstOfferConfirmApi(gstOfferConfirm, context, false)
                }else{
                    _navigationToSignIn.value = true
                }
            }else {
                handleFailure(error = error, context = context)
            }
        }
    }

    private suspend fun handleGstOfferConfirmApiSuccess(response: GstOfferConfirmResponse) {
        withContext(Dispatchers.Main){
            _isEditProcess.value = false
            _isEdited.value = true
            _gstOfferConfirmResponse.value = response
        }
    }

    fun gstInitiateOffer(id: String,loanType: String,context: Context){
        _isEditProcess.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleGstInitiateOfferApi(id, loanType,context)
        }
    }

    private suspend fun handleGstInitiateOfferApi(id: String,loanType: String,context: Context, checkForAccessToken: Boolean = true) {
        kotlin.runCatching {
            ApiRepository.gstInitiateOffer(id,loanType)
        }.onSuccess { response ->
            response?.let {
                handleGstInitiateOfferApiSuccess(response,context)
            }
        }.onFailure { error ->
            if (checkForAccessToken &&
                error is ResponseException &&
                error.response.status.value == 401
            ) {
                if (handleAuthGetAccessTokenApi()) {
                    handleGstInitiateOfferApi(
                        id = id, loanType = loanType, context = context, checkForAccessToken =
                        false
                    )
                } else {
                    _navigationToSignIn.value = true
                }
            } else {
                handleFailure(error = error, context = context)
            }
        }
    }

    private suspend fun handleGstInitiateOfferApiSuccess(response: GstOfferConfirmResponse, context: Context) {
        withContext(Dispatchers.Main){
            _isEdited.value = true
            _gstOfferConfirmResponse.value = response

            //Get Principal
            var loanAmount = response?.data?.offerResponse?.itemTags
                ?.firstNotNullOfOrNull { it?.tags?.principal }
                ?: response?.data?.catalog?.itemTags
                    ?.firstNotNullOfOrNull { it?.tags?.principal }
                ?: response?.data?.catalog?.itemPrice?.value
                ?: ""

            //Get id
            var id = response?.data?.catalog?.itemID
                ?: response?.data?.offerResponse?.itemID
                ?: ""

            loanAmount.let { loanAmount ->
                    gstOfferConfirmApi(
                        GstOfferConfirm(
                            id = id,
                            requestAmount = loanAmount,
                            loanType = "INVOICE_BASED_LOAN"
                        ),
                        context
                    )
            }


        }
    }

    private suspend fun handleFailure(error: Throwable,context: Context) {
        withContext(Dispatchers.Main) {
            if (error is ResponseException) {
                CommonMethods().handleResponseException(
                    error = error, context = context,updateErrorMessage = ::updateErrorMessage,
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
            _isEditProcess.value = false
        }
    }

    fun gstInitiateOffer(offerId: String,loanType: String,context: Context,loanAmount:String,id:String){
        _isEditProcess.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleGstInitiateOfferApi(offerId, loanType,context,loanAmount,id)
        }
    }

    private suspend fun handleGstInitiateOfferApi(offerId: String,loanType: String,context: Context,
                                                  loanAmount: String,id:String, checkForAccessToken: Boolean = true) {
        kotlin.runCatching {
            ApiRepository.gstInitiateOffer(id,loanType)
        }.onSuccess { response ->
            response?.let {
                handleGstInitiateOfferApiSuccess(response,context,loanAmount,offerId)
            }
        }.onFailure { error ->
            if (checkForAccessToken &&
                error is ResponseException &&
                error.response.status.value == 401
            ) {
                if (handleAuthGetAccessTokenApi()) {
                    handleGstInitiateOfferApi(
                        offerId = offerId, loanType = loanType, context = context, loanAmount =
                        loanAmount, id = id, checkForAccessToken = false
                    )
                } else {
                    _navigationToSignIn.value = true
                }
            } else {
                handleFailure(error = error, context = context)
            }
        }
    }

    private suspend fun handleGstInitiateOfferApiSuccess(
        response: GstOfferConfirmResponse, context: Context, loanAmount: String, offerId: String
    ) {
        withContext(Dispatchers.Main){
            _isEdited.value = true
            _gstOfferConfirmResponse.value = response
            gstOfferConfirmApi(
                GstOfferConfirm(
                    id = offerId, requestAmount = loanAmount,
                    loanType = "INVOICE_BASED_LOAN"), context
            )
        }
    }
}

class EditLoanRequestViewModelFactory(private val amount: String, private val minAmount: String,
                                      private val tenure: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditLoanRequestViewModel::class.java)) {
            return EditLoanRequestViewModel(amount, minAmount, tenure) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
