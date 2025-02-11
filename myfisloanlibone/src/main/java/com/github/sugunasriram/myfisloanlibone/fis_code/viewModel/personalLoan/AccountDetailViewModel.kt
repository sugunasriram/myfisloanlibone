package com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan

import android.content.Context
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository.handleAuthGetAccessTokenApi
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.AddBankDetail
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.AddBankDetailResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.BankAccount
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.BankDetail
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.BankDetailResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.BankList
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.GstBankDetail
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.IFSCResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.GstOfferConfirmResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.BaseViewModel
import io.ktor.client.features.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class AccountDetailViewModel : BaseViewModel() {

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

    private val _accountHolder: MutableLiveData<String> = MutableLiveData("")
    val accountHolder: LiveData<String> = _accountHolder

    private val _navigationToSignIn = MutableStateFlow(false)
    val navigationToSignIn: StateFlow<Boolean> = _navigationToSignIn

    fun onAccountHolderChanged(accountHolder: String) {
        if (accountHolder.length <= 35) {
            _accountHolder.value = accountHolder
            updateGeneralError(null)
        }
    }

    private val _accountNumber: MutableLiveData<String> = MutableLiveData("")
    val accountNumber: LiveData<String> = _accountNumber

    fun onAccountNumberChanged(accountNumber: String) {
        if (accountNumber.length <= 18) {
            _accountNumber.value = accountNumber
            updateGeneralError(null)
        }
    }

    private val _ifscCode: MutableLiveData<String> = MutableLiveData("")
    val ifscCode: LiveData<String> = _ifscCode

    fun onIfscCodeChanged(ifscCode: String) {
        if (ifscCode.length <= 11) {
            _ifscCode.value = ifscCode
            updateGeneralError(null)
        }
    }

    private val _accountType: MutableLiveData<String> = MutableLiveData("")
    val accountType: LiveData<String> = _accountType

    fun onAccountTypeChanged(accountType: String) {
        _accountType.value = accountType
        updateGeneralError(null)
    }

    private val _accountHolderError: MutableLiveData<String?> = MutableLiveData("")
    val accountHolderError: LiveData<String?> = _accountHolderError

    fun updateAccountHolderError(accountHolderError: String?) {
        _accountHolderError.value = accountHolderError
    }

    private val _accountTypeError: MutableLiveData<String?> = MutableLiveData("")
    val accountTypeError: LiveData<String?> = _accountTypeError

    fun updateAccountTypeError(accountTypeError: String?) {
        _accountHolderError.value = accountTypeError
    }

    private val _accountNumberError: MutableLiveData<String?> = MutableLiveData("")
    val accountNumberError: LiveData<String?> = _accountNumberError

    fun updateAccountNumberError(accountNumberError: String?) {
        _accountNumberError.value = accountNumberError
    }

    private val _ifscCodeError: MutableLiveData<String?> = MutableLiveData("")
    val ifscCodeError: LiveData<String?> = _ifscCodeError

    fun updateIfscCodeError(ifscCodeError: String?) {
        _ifscCodeError.value = ifscCodeError
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

    private val _bankDetailResponse = MutableStateFlow<BankDetailResponse?>(null)
    val bankDetailResponse: StateFlow<BankDetailResponse?> = _bankDetailResponse

    private val _addBankDetail = MutableLiveData<AddBankDetailResponse?>(null)
    val addBankDetail: LiveData<AddBankDetailResponse?> = _addBankDetail

    fun addBankDetail(context: Context, bankDetail: BankDetail, navController: NavHostController) {
        _bankDetailCollecting.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleAddBankDetail(context, bankDetail, navController)
        }
    }

    private suspend fun handleAddBankDetail(
        context: Context, bankDetail: BankDetail,
        navController: NavHostController, checkForAccessToken: Boolean = true
    ) {
        kotlin.runCatching {
            ApiRepository.addAccountDetail(bankDetail)
        }.onSuccess { response ->
            response?.let {
                handleAddBankDetailSuccess(response, context)
            }
        }.onFailure { error ->
            if (error is ResponseException &&
                error.response.status.value == 401
            ) {
                //Get Access Token using RefreshToken
                if (checkForAccessToken && handleAuthGetAccessTokenApi()) {
                    handleAddBankDetail(context, bankDetail, navController, false)
                } else {
                    _navigationToSignIn.value = true
                }
            } else {
                handleFailure(error = error, context =  context)
            }
        }
    }

    private suspend fun handleAddBankDetailSuccess(response: BankDetailResponse, context: Context) {
        withContext(Dispatchers.Main) {
            _bankDetailResponse.value = response
            _bankDetailCollected.value = true
            _bankDetailCollecting.value = false
            if (_bankAccount.value == null) {
                addBank(
                    bankDetail = AddBankDetail(
                        account_holder_name = _accountHolder.value.toString(),
                        bank_account_number = _accountNumber.value.toString(),
                        account_type = accountType.value.toString(),
                        bank_ifsc_code = ifscCode.value.toString()
                    ),
                    context
                )
            }
        }
    }

    private val _shouldShowKeyboard: MutableLiveData<Boolean> = MutableLiveData(false)
    val shouldShowKeyboard: LiveData<Boolean> = _shouldShowKeyboard

    fun requestKeyboard() {
        _shouldShowKeyboard.value = true
    }

    fun resetKeyboardRequest() {
        _shouldShowKeyboard.value = false
    }

    fun accountDetailValidation(
        context: Context, accountNumber: String, accountHolder: String, ifscCode: String,
        focusAccountHolder: FocusRequester, focusAccountNumber: FocusRequester,
        focusIfscCode: FocusRequester, accountSelectedText: String,
        focusAccountType: FocusRequester, bankDetail: BankDetail, navController: NavHostController
    ) {
        clearMessage()
        if (accountHolder.trim().isEmpty()) {
            updateAccountHolderError(context.getString(R.string.enter_account_holder))
            focusAccountHolder.requestFocus()
            requestKeyboard()
        } else if (!Pattern.compile("^[a-zA-Z ]*$").matcher(accountHolder).find()) {
            updateAccountHolderError(context.getString(R.string.character_special_validation))
            focusAccountHolder.requestFocus()
            requestKeyboard()
        } else if (accountHolder.trim().length < 4) {
            updateAccountHolderError(context.getString(R.string.please_enter_valid_name))
            focusAccountHolder.requestFocus()
            requestKeyboard()
        } else if (accountSelectedText.trim().isEmpty()) {
            CommonMethods().toastMessage(context,context.getString(R.string.please_select_the_account_type))
            focusAccountType.requestFocus()
            requestKeyboard()
        } else if (accountNumber.trim().isEmpty()) {
            updateAccountNumberError(context.getString(R.string.enter_account_number))
            focusAccountNumber.requestFocus()
            requestKeyboard()
        } else if (accountNumber.trim().length < 9) {
            updateAccountNumberError(context.getString(R.string.enter_valid_account_number))
            focusAccountNumber.requestFocus()
            requestKeyboard()
        } else if (ifscCode.trim().isEmpty()) {
            updateIfscCodeError(context.getString(R.string.enter_ifsc_code))
            focusIfscCode.requestFocus()
            requestKeyboard()
        } else if (CommonMethods().isValidIfscCode(ifscCode) != true) {
            updateIfscCodeError(context.getString(R.string.enter_valid_ifsc_code))
            focusIfscCode.requestFocus()
            requestKeyboard()
        } else {
            addBankDetail(context, bankDetail, navController)
        }
    }


    private val _inProgress = MutableStateFlow(false)
    val inProgress: StateFlow<Boolean> = _inProgress

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted

    private val _getBankList = MutableStateFlow<BankList?>(null)
    val getBankList: StateFlow<BankList?> = _getBankList

    fun getBankList(context: Context, navController: NavHostController) {
        if (_inProgress.value) {
            return
        }
        _inProgress.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleGetBankList(context, navController)
        }
    }

    private suspend fun handleGetBankList(
        context: Context, navController: NavHostController,
        checkForAccessToken: Boolean = true
    ) {
        kotlin.runCatching {
            ApiRepository.getBankList()
        }.onSuccess { response ->
            response?.let {
                handleGetBankListSuccess(response)
            }
        }.onFailure { error ->
            if (error is ResponseException &&
                error.response.status.value == 401
            ) {
                //Get Access Token using RefreshToken
                if (checkForAccessToken && handleAuthGetAccessTokenApi()) {
                    handleGetBankList(context, navController, false)
                } else {
                    _navigationToSignIn.value = true
                }
            } else {
                handleFailure(error = error, context =  context)
            }
        }
    }

    private suspend fun handleGetBankListSuccess(response: BankList) {
        withContext(Dispatchers.Main) {
            _inProgress.value = false
            _isCompleted.value = true
            _getBankList.value = response
        }
    }

    private val _gettingBank = MutableStateFlow(false)
    val gettingBank: StateFlow<Boolean> = _gettingBank

    private val _gotBank = MutableStateFlow(false)
    val gotBank: StateFlow<Boolean> = _gotBank

    private val _bankAccount = MutableStateFlow<BankAccount?>(null)
    val bankAccount: StateFlow<BankAccount?> = _bankAccount

    private var isApiCalled = false
    fun getBankAccount(context: Context) {
        if (isApiCalled) return
        isApiCalled = true
        _gettingBank.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleGetBankAccount(context)
        }
    }

    private suspend fun handleGetBankAccount(
        context: Context,
        checkForAccessToken: Boolean = true
    ) {
        kotlin.runCatching {
            ApiRepository.getBankAccount()
        }.onSuccess { response ->
            response?.let {
                handleGetBankAccountSuccess(response)
            }
        }.onFailure { error ->
            if (checkForAccessToken &&
                error is ResponseException &&
                error.response.status.value == 401
            ) {
                //Get Access Token using RefreshToken
                if (handleAuthGetAccessTokenApi()) {
                    handleGetBankAccount(context, false)
                } else {
                    _navigationToSignIn.value = true
                }
            } else {
                handleFailure(error = error, context =  context)
            }
        }
    }

    private suspend fun handleGetBankAccountSuccess(response: BankAccount) {
        withContext(Dispatchers.Main) {
            _gettingBank.value = false
            _gotBank.value = true
            _bankAccount.value = response
        }
    }


    private val _bankName = MutableStateFlow<IFSCResponse?>(null)
    val bankName: StateFlow<IFSCResponse?> = _bankName

    fun getBankName(ifscCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                ApiRepository.getBankName(ifscCode)
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    _bankName.value = it
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    _bankName.value = null
                }
            }
        }
    }

    private fun addBank(bankDetail: AddBankDetail, context: Context) {
        _bankDetailCollecting.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleAddBank(context, bankDetail)
        }
    }

    private suspend fun handleAddBank(
        context: Context, bankDetail: AddBankDetail, checkForAccessToken: Boolean = true
    ) {
        kotlin.runCatching {
            ApiRepository.addBank(bankDetail)
        }.onSuccess { response ->
            response?.let {
                handleAddBankSuccess(response)
            }
        }.onFailure { error ->
            if (checkForAccessToken &&
                error is ResponseException &&
                error.response.status.value == 401
            ) {
                //Get Access Token using RefreshToken
                if (handleAuthGetAccessTokenApi()) {
                    handleAddBank(context, bankDetail, false)
                } else {
                    _navigationToSignIn.value = true
                }
            } else {
                handleFailure(error = error, context =  context)
            }
        }
    }

    private suspend fun handleAddBankSuccess(response: AddBankDetailResponse) {
        withContext(Dispatchers.Main) {
            _bankDetailCollecting.value = false
            _addBankDetail.value = response
            _bankDetailCollected.value = true
        }
    }

    fun accountDetailValidation(
        context: Context, accountNumber: String, accountHolder: String, ifscCode: String,
        focusAccountHolder: FocusRequester, focusAccountNumber: FocusRequester,
        focusIfscCode: FocusRequester, id: String,
    ) {
        clearMessage()
        if (accountHolder.trim().isEmpty()) {
            updateAccountHolderError(context.getString(R.string.enter_account_holder))
            focusAccountHolder.requestFocus()
            requestKeyboard()
        } else if (!Pattern.compile("^[a-zA-Z ]*$").matcher(accountHolder).find()) {
            updateAccountHolderError(context.getString(R.string.character_special_validation))
            focusAccountHolder.requestFocus()
            requestKeyboard()
        } else if (accountHolder.trim().length < 4) {
            updateAccountHolderError(context.getString(R.string.please_enter_valid_name))
            focusAccountHolder.requestFocus()
            requestKeyboard()
        } else if (accountNumber.trim().isEmpty()) {
            updateAccountNumberError(context.getString(R.string.enter_account_number))
            focusAccountNumber.requestFocus()
            requestKeyboard()
        } else if (accountNumber.trim().length < 9) {
            updateAccountNumberError(context.getString(R.string.enter_valid_account_number))
            focusAccountNumber.requestFocus()
            requestKeyboard()
        } else if (ifscCode.trim().isEmpty()) {
            updateIfscCodeError(context.getString(R.string.enter_ifsc_code))
            focusIfscCode.requestFocus()
            requestKeyboard()
        } else if (CommonMethods().isValidIfscCode(ifscCode) != true) {
            updateIfscCodeError(context.getString(R.string.enter_valid_ifsc_code))
            focusIfscCode.requestFocus()
            requestKeyboard()
        } else {
            gstLoanEntityApproval(
                bankDetail = GstBankDetail(
                    accountNumber = accountNumber, ifscCode = ifscCode,
                    accountHolderName = accountHolder, id = id, loanType = "INVOICE_BASED_LOAN"
                ), context = context
            )
        }
    }

    private val _gstBankDetailResponse = MutableStateFlow<GstOfferConfirmResponse?>(null)
    val gstBankDetailResponse: StateFlow<GstOfferConfirmResponse?> = _gstBankDetailResponse

    fun gstLoanEntityApproval(bankDetail: GstBankDetail, context: Context) {
        _bankDetailCollecting.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleGstLoanEntityApproval(bankDetail, context)
        }
    }

    private suspend fun handleGstLoanEntityApproval(bankDetail: GstBankDetail, context: Context,
                                                    checkForAccessToken: Boolean = true) {
        kotlin.runCatching {
            ApiRepository.gstLoanEntityApproval(bankDetail)
        }.onSuccess { response ->
            response?.let {
                handleGstLoanEntityApprovalSuccess(response, context)
            }
        }.onFailure { error ->
            if (checkForAccessToken &&
                error is ResponseException &&
                error.response.status.value == 401
            ) {
                //Get Access Token using RefreshToken
                if (handleAuthGetAccessTokenApi()) {
                    handleGstLoanEntityApproval(bankDetail, context, false)
                } else {
                    _navigationToSignIn.value = true
                }
            } else {
                handleFailure(error = error, context =  context)
            }
        }
    }

    private suspend fun handleGstLoanEntityApprovalSuccess(
        response: GstOfferConfirmResponse, context: Context
    ) {
        withContext(Dispatchers.Main) {
            _bankDetailCollecting.value = false
            _bankDetailCollected.value = true
            _gstBankDetailResponse.value = response
        }
    }

    private val _loanApprovedResponse = MutableStateFlow<GstOfferConfirmResponse?>(null)
    val loanApprovedResponse: StateFlow<GstOfferConfirmResponse?> = _loanApprovedResponse

    fun gstLoanApproved(id: String,loanType: String,context: Context){
        _bankDetailCollecting.value = true
        viewModelScope.launch(Dispatchers.Main) {
            handleGstLoanApproved(id,loanType,context)
        }
    }

    private suspend fun handleGstLoanApproved(id: String,loanType: String,context: Context, checkForAccessToken: Boolean = true) {
        kotlin.runCatching {
            ApiRepository.gstLoanApproved(id,loanType)
        }.onSuccess { response ->
            response?.let {
                handleGstLoanApprovedSuccess(response,id,context)
            }
        }.onFailure { error ->
            if (checkForAccessToken &&
                error is ResponseException &&
                error.response.status.value == 401
            ) {
                //Get Access Token using RefreshToken
                if (handleAuthGetAccessTokenApi()) {
                    handleGstLoanApproved(id, loanType, context, false)
                } else {
                    _navigationToSignIn.value = true
                }
            } else {
                handleFailure(error = error, context =  context)
            }
        }
    }

    private suspend fun handleGstLoanApprovedSuccess(response: GstOfferConfirmResponse, id: String,context: Context) {
        withContext(Dispatchers.Main){
            _bankDetailCollecting.value = false
            _bankDetailCollected.value = true
            _loanApprovedResponse.value = response
        }
    }

    private suspend fun handleFailure(error: Throwable, context: Context) {
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
            _bankDetailCollecting.value = false
            _inProgress.value = false
            _gettingBank.value = false
        }
    }

}