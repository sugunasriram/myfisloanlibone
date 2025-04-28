package com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth

import android.content.Context
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.util.Patterns
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.ForgotPassword
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.GenerateAuthOtp
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.LogIn
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.ResetPassword
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.UserRole
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.BaseViewModel
import io.ktor.client.features.ResponseException
import io.ktor.client.statement.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.concurrent.TimeoutException
import java.util.regex.Pattern

class SignInViewModel : BaseViewModel() {
    val allowedNumbers = Regex("^[0-9]*$")
    val allowedCharacters = Regex("^[a-zA-Z]*$")

    private val _showInternetScreen = MutableLiveData<Boolean>(false)
    val showInternetScreen: LiveData<Boolean> = _showInternetScreen

    private val _showTimeOutScreen = MutableLiveData<Boolean>(false)
    val showTimeOutScreen: LiveData<Boolean> = _showTimeOutScreen

    private val _showServerIssueScreen = MutableLiveData<Boolean>(false)
    val showServerIssueScreen: LiveData<Boolean> = _showServerIssueScreen

    private val _unexpectedError = MutableLiveData<Boolean>(false)
    val unexpectedError: LiveData<Boolean> = _unexpectedError

    private val _upDated = MutableStateFlow(true)
    val upDated: StateFlow<Boolean> = _upDated

    private val _shownMsg = MutableStateFlow(false)
    val shownMsg: StateFlow<Boolean> = _shownMsg

    fun updateShownMsg(boolean: Boolean) {
        _shownMsg.value = boolean
    }

    private val _mobileNumber: MutableLiveData<String> = MutableLiveData("")
    val mobileNumber: LiveData<String> = _mobileNumber

    fun onMobileNumberChanged(mobileNumber: String) {
        val extractedNumbers = mobileNumber.replace(Regex("(^\\+91|[^0-9])"), "")
        if (extractedNumbers.length <= 10) {
            _mobileNumber.value = extractedNumbers
            updateGeneralError(null)
        } else {
            _mobileNumber.value = extractedNumbers.take(10)
            updateGeneralError("Mobile number must be 10 digits long")
        }
    }

    private val _password: MutableLiveData<String> = MutableLiveData("")
    val password: LiveData<String> = _password

    fun onPasswordChanged(password: String) {
        if (password.length <= 64) {
            updateGeneralError(null)
            _password.value = password
        }
    }

    private val _confirmPassword: MutableLiveData<String> = MutableLiveData("")
    val confirmPassword: LiveData<String> = _confirmPassword

    fun onConfirmPasswordChanged(confirmPassword: String) {
        if (confirmPassword.length <= 64) {
            updateGeneralError(null)
            _confirmPassword.value = confirmPassword
        }
    }

    private val _passewordError: MutableLiveData<String?> = MutableLiveData("")
    val passwordError: LiveData<String?> = _passewordError

    fun updatePasswordError(errorMsg: String?) {
        _passewordError.value = errorMsg
    }

    private val _confirmPasswordError: MutableLiveData<String?> = MutableLiveData("")
    val confirmPasswordError: LiveData<String?> = _confirmPasswordError

    fun updateConfirmPasswordError(errorMsg: String?) {
        _confirmPasswordError.value = errorMsg
    }

    private val _generalError: MutableLiveData<String?> = MutableLiveData("")
    val generalError: LiveData<String?> = _generalError

    fun updateGeneralError(errorMsg: String?) {
        _generalError.value = errorMsg
    }

    private fun clearMessage(newData: String? = null) {
        updateGeneralError(newData)
    }

    private val _emailError: MutableLiveData<String?> = MutableLiveData("")
    val emailError: LiveData<String?> = _emailError

    fun updateEmailError(errorMsg: String?) {
        _emailError.value = errorMsg
    }

    private val _mobileNumberError: MutableLiveData<String?> = MutableLiveData("")
    val mobileNumberError: LiveData<String?> = _mobileNumberError
    fun updateMobileNumberError(errorMsg: String?) {
        _mobileNumberError.value = errorMsg
    }

    private val _isLoginInProgress = MutableStateFlow(false)
    val isLoginInProgress: StateFlow<Boolean> = _isLoginInProgress

    private val _isLoginSuccess = MutableStateFlow(false)
    val isLoginSuccess: StateFlow<Boolean> = _isLoginSuccess

    private val _loginSuccessData = MutableStateFlow<LogIn?>(null)
    val loginSuccessData: StateFlow<LogIn?> = _loginSuccessData

    private val _generatedOtpData = MutableStateFlow<GenerateAuthOtp?>(null)
    val generatedOtpData: StateFlow<GenerateAuthOtp?> = _generatedOtpData

    private val _userRoleSuccessData = MutableStateFlow<UserRole?>(null)
    val userRoleSuccessData: StateFlow<UserRole?> = _userRoleSuccessData

    fun getUserRole(
        mobileNumber: String,
        countryCode: String,
        context: Context
    ) {
        _isLoginInProgress.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleGetUserRole(mobileNumber, countryCode, context)
        }
    }
    private suspend fun handleGetUserRole(
        mobileNumber: String,
        countryCode: String,
        context: Context
    ) {
        kotlin.runCatching {
            ApiRepository.userRole()
        }.onSuccess { response ->
            handleGetUserRoleSuccess(response,mobileNumber,countryCode,context)
        }.onFailure { error ->
            handleGenerateLoginOtpFailure(error, context)
//            handleLoginFailure(error, context)
        }
    }
    private suspend fun handleGetUserRoleSuccess(response: UserRole?, mobileNumber: String,
                                                 countryCode: String,context: Context) {
        withContext(Dispatchers.Main) {
            if (response != null) {
                response.data?.find { it.role == "USER" }?.let { userRole ->
                    val userId = userRole._id
                    _userRoleSuccessData.value = response
                    generateLoginOtp(mobileNumber,countryCode,userId,context)
                }
            }
        }
    }

    private fun generateLoginOtp(
        mobileNumber: String,
        countryCode: String,userRole:String,
        context: Context
    ) {
        _isLoginInProgress.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleGenerateLoginOtp(mobileNumber, countryCode,userRole, context)
        }
    }

    private suspend fun handleGenerateLoginOtp(
        mobileNumber: String,
        countryCode: String, userRole:String,
        context: Context
    ) {
        kotlin.runCatching {
            ApiRepository.generateAuthOtp(mobileNumber, countryCode,userRole)
        }.onSuccess { response ->
            handleGenerateLoginOtpSuccess(response)
        }.onFailure { error ->
            Log.d("Sugu : ",error.message.toString())
            handleGenerateLoginOtpFailure(error, context)
        }
    }

//    private suspend fun handleLoginSuccess(response: LogIn?) {
    private suspend fun handleGenerateLoginOtpSuccess(response: GenerateAuthOtp?) {
        withContext(Dispatchers.Main) {
            if (response != null) {
                _generatedOtpData.value = response
                _isLoginInProgress.value = false
                _isLoginSuccess.value = true
            }
        }
    }

    private suspend fun handleGenerateLoginOtpFailure(error: Throwable, context: Context) {
        withContext(Dispatchers.Main) {
            if (error is ResponseException) {
                handleLogInResponseException(error, context)
            } else {
                handleLogInGeneralException(error)
            }
            _isLoginInProgress.value = false
        }
    }

    private fun handleLogInResponseException(error: ResponseException, context: Context) {
        val statusCode = error.response.status.value
        when (statusCode) {
//            400 -> {
//                CommonMethods().toastMessage(context,context.getString(R.string.user_name_password_wrong))
//            }

            500 -> {
                _showServerIssueScreen.value = true
            }

            else -> {
                _unexpectedError.value = true
            }
        }
    }

    private fun handleLogInGeneralException(error: Throwable) {
        when (error) {
            is IOException -> _showInternetScreen.value = true
            is TimeoutCancellationException -> _showTimeOutScreen.value = true
            else -> _unexpectedError.value = true
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

    fun signInValidation(navController: NavHostController,
                         mobileNumber: String,
                         mobileNumberFocus: FocusRequester,
                         context: Context
    ){
        clearMessage()
        if (mobileNumber.trim().isEmpty()) {
            updateMobileNumberError(context.getString(R.string.please_enter_phone_number))
            mobileNumberFocus.requestFocus()
            requestKeyboard()
        } else if (!Pattern.compile("^[0-9]*\$").matcher(mobileNumber).find()) {
            updateMobileNumberError(context.getString(R.string.should_not_contain_characters_alphabets))
            mobileNumberFocus.requestFocus()
            requestKeyboard()
        } else if (mobileNumber.trim().length < 10) {
            updateMobileNumberError(context.getString(R.string.please_enter_valid_mobile_number))
            mobileNumberFocus.requestFocus()
            requestKeyboard()
        }  else if (!verifyPhoneNumber(mobileNumber,context)) {
            updateMobileNumberError(context.getString(R.string.please_enter_valid_mobile_number))
            mobileNumberFocus.requestFocus()
        }else{
            getUserRole(mobileNumber, context.getString(R.string.country_code), context)
        }

    }

    private fun verifyPhoneNumber(phoneNumber: String,context: Context): Boolean {
        val phoneUtil = PhoneNumberUtil.getInstance();
        val number = phoneUtil.parse(context.getString(R.string.country_code) + phoneNumber, "IN");
        if(phoneUtil.isValidNumber(number)){
            return phoneUtil.getNumberType(number) == PhoneNumberUtil.PhoneNumberType.MOBILE
        }
        return false

    }

//    fun signInButtonValidation(
//        mobileNumber: String,
//        password: String,
//        focusPassword: FocusRequester,
//        focusPhNumber: FocusRequester,
//        context: Context
//    ) {
//        clearMessage()
//        when {
//            mobileNumber.trim().isEmpty() -> {
//                updateEmailError(context.getString(R.string.please_enter_mobile_number))
//                focusPhNumber.requestFocus()
//                requestKeyboard()
//            }
//            !Pattern.compile("^[0-9]*\$").matcher(mobileNumber).find() -> {
//                updateEmailError(context.getString(R.string.should_not_contain_characters_alphabets))
//            }
//            mobileNumber.trim().length < 10 -> {
//                updateEmailError(context.getString(R.string.enter_valid_phone_number))
//                focusPhNumber.requestFocus()
//                requestKeyboard()
//            }
//            CommonMethods().isValidPassword(password) != true -> {
//                if (password.trim().length < 8) {
//                    updatePasswordError(context.getString(R.string.please_valid_password))
//                    /*updatePasswordError(context.getString(R.string.password_contain_eight_characters));*/
//                } else if (!Pattern.compile("[A-Z]").matcher(password).find()) {
//                    updatePasswordError(context.getString(R.string.please_valid_password))
//                    /*updatePasswordError(context.getString(R.string.password_contain_upper_case_characters));*/
//                } else if (!Pattern.compile("[a-z]").matcher(password).find()) {
//                    updatePasswordError(context.getString(R.string.please_valid_password))
//                   /* updatePasswordError(context.getString(R.string.password_contain_lower_case_characters));*/
//                } else if (!Pattern.compile("\\d").matcher(password).find()) {
//                    updatePasswordError(context.getString(R.string.please_valid_password))
//                    /*updatePasswordError(context.getString(R.string.password_contain_digits));*/
//                } else if (!Pattern.compile("[^A-Za-z0-9]").matcher(password).find()) {
//                    updatePasswordError(context.getString(R.string.please_valid_password))
//                    /*updatePasswordError(context.getString(R.string.password_contain_special_characters));*/
//                }else{
//                    updatePasswordError(context.getString(R.string.please_enter_valid_password));
//                }
//                focusPassword.requestFocus();
//                requestKeyboard()
//            }
//
//            else -> {
//                apiLogin(mobileNumber, context.getString(R.string.country_code), password, context)
//            }
//        }
//    }


    private val _passwordLoading = MutableStateFlow(false)
    val passwordLoading: StateFlow<Boolean> = _passwordLoading

    private val _passwordLoaded = MutableStateFlow(false)
    val passwordLoaded: StateFlow<Boolean> = _passwordLoaded

    private val _forgotResponse = MutableStateFlow<ForgotPassword?>(null)
    val forgotResponse: StateFlow<ForgotPassword?> = _forgotResponse

    fun forgotPassword(mobileNumber: String, countryCode: String, context: Context) {
        _passwordLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleForgotPassword(mobileNumber, countryCode, context)
        }
    }

    private suspend fun handleForgotPassword(mobileNumber: String, countryCode: String, context:
    Context) {
        kotlin.runCatching {
            ApiRepository.forgotPasswordApi(mobileNumber, countryCode)
        }.onSuccess { response ->
            handleForgotPasswordSuccess(response)
        }.onFailure { error ->
            handleForgotPasswordFailure(error, context)
        }
    }

    private suspend fun handleForgotPasswordSuccess(response: ForgotPassword?) {
        withContext(Dispatchers.Main) {
            _passwordLoading.value = false
            _passwordLoaded.value = true
            _forgotResponse.value = response
        }
    }

    private suspend fun handleForgotPasswordFailure(error: Throwable, context: Context) {
        withContext(Dispatchers.Main) {
            if (error is ResponseException) {
                handleForgotPasswordResponseException(error, context)
            } else {
                handleForgotPasswordGeneralException(error)
            }
            _passwordLoading.value = false
        }
    }

    private suspend fun handleForgotPasswordResponseException(error: ResponseException,
                                                              context: Context) {
        val statusCode = error.response.status.value
        when (statusCode) {
            400 -> {
                val errorContent = error.response.readText()
                val errorMessage = CommonMethods().parseErrorMessage(errorContent)
                updateGeneralError(errorMessage)
            }
            404 -> {
               val errorMessage = "Mobile Number Does Not Exist"
                CommonMethods().toastMessage(context,errorMessage)
            }

            500 -> {
                _showServerIssueScreen.value = true
            }

            else -> {
                _unexpectedError.value = true
            }
        }
    }

    private fun handleForgotPasswordGeneralException(error: Throwable) {
        when (error) {
            is IOException -> _showInternetScreen.value = true
            is TimeoutCancellationException -> _showTimeOutScreen.value = true
            else -> _unexpectedError.value = true
        }
    }

    fun forgotButtonValidation(
        mobileNumber: String,
        focusPhNumber: FocusRequester,
        context: Context
    ) {
        clearMessage()
        when {
            mobileNumber.trim().isEmpty() -> {
                updateMobileNumberError(context.getString(R.string.please_enter_phone_number))
                focusPhNumber.requestFocus()
                requestKeyboard()
            }

            !Pattern.compile("^[0-9]*\$").matcher(mobileNumber).find() -> {
                updateMobileNumberError(context.getString(R.string.should_not_contain_characters_alphabets))
                focusPhNumber.requestFocus()
                requestKeyboard()
            }

            mobileNumber.trim().length < 10 -> {
                updateMobileNumberError(context.getString(R.string.enter_registered_phone_number))
                focusPhNumber.requestFocus()
                requestKeyboard()
            }
            else -> {
                forgotPassword(mobileNumber, context.getString(R.string.country_code), context)
            }
        }
    }


    private val _resetting = MutableStateFlow(false)
    val resetting: StateFlow<Boolean> = _resetting

    private val _reSetted = MutableStateFlow(false)
    val reSetted: StateFlow<Boolean> = _reSetted

    private val _resetResponse = MutableStateFlow<ResetPassword?>(null)
    val resetResponse: StateFlow<ResetPassword?> = _resetResponse

    private fun resetPassword(newPassword: String, confirmPassword: String, mobileNumber: String,countryCode: String) {
        _resetting.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleResetPassword(newPassword, confirmPassword, mobileNumber,countryCode)
        }
    }

    private suspend fun handleResetPassword(
        newPassword: String,
        confirmPassword: String,
        mobileNumber: String,
        countryCode: String
    ) {
        kotlin.runCatching {
            ApiRepository.resetPasswordApi(newPassword, confirmPassword, mobileNumber,countryCode)
        }.onSuccess { response ->
            handleResetPasswordSuccess(response)
        }.onFailure { error ->
            handleResetPasswordFailure(error)
        }
    }

    private suspend fun handleResetPasswordSuccess(response: ResetPassword?) {
        withContext(Dispatchers.Main) {
            _resetting.value = false
            _reSetted.value = true
            _resetResponse.value = response
        }
    }

    private suspend fun handleResetPasswordFailure(error: Throwable) {
        withContext(Dispatchers.Main) {
            if (error is ResponseException) {
                handleResetPasswordResponseException(error)
            } else {
                handleResetPasswordNetworkException(error)
            }
            _resetting.value = false
        }
    }

    private suspend fun handleResetPasswordResponseException(error: ResponseException) {
        val statusCode = error.response.status.value
        when (statusCode) {
            400 -> {
                val errorMsg = error.response.readText()
                val errorMessage = CommonMethods().parseErrorMessage(errorMsg)
                updateGeneralError(errorMessage)
            }

            500 -> {
                _showServerIssueScreen.value = true
            }

            else -> {
                _unexpectedError.value = true
            }
        }
    }

    private fun handleResetPasswordNetworkException(error: Throwable) {
        when (error) {
            is IOException -> {
                _showInternetScreen.value = true
            }

            is TimeoutCancellationException -> {
                _showTimeOutScreen.value = true
            }

            is TimeoutException -> {
                _showTimeOutScreen.value = true
            }

            else -> {
                _unexpectedError.value = true
            }
        }
    }




    fun resetButtonValidation(
        password: String,
        confirmPassword: String,
        focusPassword: FocusRequester,
        focusConfirmPassword: FocusRequester,
        context: Context,
        mobileNumber: String
    ) {
        clearMessage()
        if (password.trim().isEmpty()) {
            updatePasswordError(context.getString(R.string.please_enter_password))
            focusPassword.requestFocus()
            requestKeyboard()
        } else if (CommonMethods().isValidPassword(password) != true) {
            if (password.trim().length < 8) {
                updatePasswordError(context.getString(R.string.password_contain_eight_characters))
            } else if (!Pattern.compile("[A-Z]").matcher(password).find()) {
                updatePasswordError(context.getString(R.string.password_contain_upper_case_characters))
            } else if (!Pattern.compile("[a-z]").matcher(password).find()) {
                updatePasswordError(context.getString(R.string.password_contain_lower_case_characters))
            } else if (!Pattern.compile("\\d").matcher(password).find()) {
                updatePasswordError(context.getString(R.string.password_contain_digits))
            } else if (!Pattern.compile("[^A-Za-z0-9]").matcher(password).find()) {
                updatePasswordError(context.getString(R.string.password_contain_special_characters))
            }
            focusPassword.requestFocus();
            requestKeyboard()
        } else if (confirmPassword.trim().isEmpty()) {
            updateConfirmPasswordError(context.getString(R.string.please_confirm_password))
            focusConfirmPassword.requestFocus()
            requestKeyboard()
        } else if (!confirmPassword.equals(password)) {
            updateConfirmPasswordError(context.getString(R.string.password_not_matching))
            focusConfirmPassword.requestFocus()
            requestKeyboard()
        } else {
            resetPassword(password, confirmPassword, mobileNumber,"+91")
        }
    }
}
