package com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth

import android.content.Context
import android.util.Log
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository
import com.github.sugunasriram.myfisloanlibone.fis_code.network.core.ApiRepository.handleAuthGetAccessTokenApi
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.Data
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.Logout
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.PincodeModel
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.Profile
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.ProfileResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.Signup
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.UpdateProfile
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.storage.TokenManager
import io.ktor.client.features.ResponseException
import io.ktor.client.statement.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Pattern

class RegisterViewModel : ViewModel() {

    val allowedNumbers = Regex("^[0-9]*$")
    val allowedCharacters = Regex("^[a-zA-Z]*$")

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

    private fun updateErrorMessage(message: String) {
        _errorMessage.value = message
    }

    private val _firstName: MutableLiveData<String?> = MutableLiveData("")
    val firstName: LiveData<String?> = _firstName

    fun onFirstNameChanged(firstName: String) {
        val sanitizedInput = firstName.replace(Regex("[^a-zA-Z]\\s"), "")
        if (firstName.length <= 30) {
            _firstName.value = sanitizedInput
            updateGeneralError(null)
        }
    }

    private val _firstNameError: MutableLiveData<String?> = MutableLiveData("")
    val firstNameError: LiveData<String?> = _firstNameError

    fun updateFirstNameError(firstNameError: String?) {
        _firstNameError.value = firstNameError
    }

    private val _lastName: MutableLiveData<String?> = MutableLiveData("")
    val lastName: LiveData<String?> = _lastName

    fun onLastNameChanged(lastName: String) {
        val sanitizedInput = lastName.replace(Regex("[^a-zA-Z]\\s"), "")
        if (lastName.length <= 30) {
            _lastName.value = sanitizedInput
            updateGeneralError(null)
        }
    }

    private val _lastNameError: MutableLiveData<String?> = MutableLiveData("")
    val lastNameError: LiveData<String?> = _lastNameError

    fun updateLastNameError(lastNameError: String?) {
        _lastNameError.value = lastNameError
    }

    private val _personalEmailId: MutableLiveData<String?> = MutableLiveData("")
    val personalEmailId: LiveData<String?> = _personalEmailId

    fun onPersonalEmailIdChanged(personalEmailId: String?) {
        _personalEmailId.value = personalEmailId
        updateGeneralError(null)
    }

    private val _personalEmailIdError: MutableLiveData<String?> = MutableLiveData("")
    val personalEmailIdError: LiveData<String?> = _personalEmailIdError

    fun updatePersonalEmailError(personalEmailIdError: String?) {
        _personalEmailIdError.value = personalEmailIdError
    }

    private val _officialEmailId: MutableLiveData<String?> = MutableLiveData("")
    val officialEmailId: LiveData<String?> = _officialEmailId


    fun onOfficialEmailIdChanged(officialEmailId: String?) {
        _officialEmailId.value = officialEmailId
        updateGeneralError(null)
    }

    private val _officialEmailIdError: MutableLiveData<String?> = MutableLiveData("")
    val officialEmailIdError: LiveData<String?> = _officialEmailIdError

    fun updateOfficialEmailError(officialEmailIdError: String?) {
        _officialEmailIdError.value = officialEmailIdError
    }

    private val _password: MutableLiveData<String?> = MutableLiveData("")
    val password: LiveData<String?> = _password

    fun onPasswordChanged(password: String) {
        if (password.length <= 64) {
            _password.value = password
            updateGeneralError(null)
        }
    }

    private val _passwordError: MutableLiveData<String?> = MutableLiveData("")
    val passwordError: LiveData<String?> = _passwordError

    fun updatePasswordError(passwordError: String?) {
        _passwordError.value = passwordError
    }

    private val _mobileNumber: MutableLiveData<String?> = MutableLiveData()
    val mobileNumber: LiveData<String?> = _mobileNumber

    fun onMobileNumberChanged(mobileNumber: String) {
        val extractedNumbers = mobileNumber.replace(Regex("[^0-9]"), "")
        if (mobileNumber.length <= 10) {
            _mobileNumber.value = extractedNumbers
            updateGeneralError(null)
        }
    }

    private val _mobileNumberError: MutableLiveData<String?> = MutableLiveData("")
    val mobileNumberError: LiveData<String?> = _mobileNumberError

    fun updateMobileNumberError(mobileNumberError: String?) {
        _mobileNumberError.value = mobileNumberError
    }

    private val _dob: MutableLiveData<String?> = MutableLiveData("")
    val dob: LiveData<String?> = _dob

    fun onDobChanged(dob: String) {
        _dob.value = dob
        _dobError.value = ""
        updateGeneralError(null)
    }

    private val _dobError: MutableLiveData<String?> = MutableLiveData("")
    val dobError: LiveData<String?> = _dobError

    fun updateDobError(dobError: String?) {
        _dobError.value = dobError
    }

    private val _genderError: MutableLiveData<String?> = MutableLiveData("")
    val genderError: LiveData<String?> = _genderError

    private val _gender: MutableLiveData<String?> = MutableLiveData("")
    val gender: LiveData<String?> = _gender

    private fun updateGenderError(genderError: String?) {
        _genderError.value = genderError

    }

    fun onGenderChanged(gender: String) {
        _gender.value = gender
        _genderError.value = null
        updateGeneralError(null)
    }


    private val _companyNameError: MutableLiveData<String?> = MutableLiveData("")
    val companyNameError: LiveData<String?> = _companyNameError

    fun updateCompanyNameError(companyNameError: String?) {
        _companyNameError.value = companyNameError
    }

    private val _panNumber: MutableLiveData<String?> = MutableLiveData("")
    val panNumber: LiveData<String?> = _panNumber

    fun onPanNumberChanged(panNumber: String) {
        val sanitizedInput = panNumber.replace(Regex("[^a-zA-Z0-9]"), "")
        if (panNumber.length <= 10) {
            _panNumber.value = sanitizedInput
            updateGeneralError(null)
        } else {
            _panNumber.value = sanitizedInput.take(10)
        }
    }

    private val _panError: MutableLiveData<String?> = MutableLiveData("")
    val panError: LiveData<String?> = _panError

    fun updatePanError(panError: String?) {
        _panError.value = panError
    }

    private val _udyamError: MutableLiveData<String?> = MutableLiveData("")
    val udyamError: LiveData<String?> = _udyamError

    fun updateUdyamError(udyamError: String?) {
        _udyamError.value = udyamError
    }

    private val _employment: MutableLiveData<String?> = MutableLiveData("")
    val employment: LiveData<String?> = _employment

    fun onEmploymentChanged(employment: String) {
        _employment.value = employment
        _employmentError.value = null
        updateGeneralError(null)
    }

    private val _employmentError: MutableLiveData<String?> = MutableLiveData("")
    val employmentError: LiveData<String?> = _employmentError
    fun updateEmploymentError(employmentError: String?) {
        _employmentError.value = employmentError
    }

    private val _companyName: MutableLiveData<String?> = MutableLiveData("")
    val companyName: LiveData<String?> = _companyName

    fun onCompanyNameChanged(companyName: String) {
        val sanitizedInput = companyName.replace(Regex("[^a-zA-Z0-9 ]"), "")
        if (companyName.length <= 50) {
            _companyName.value = sanitizedInput
            _companyNameError.value = null
            updateGeneralError(null)
        }
    }

    private val _udyamNumber: MutableLiveData<String> = MutableLiveData("")
    val udyamNumber: LiveData<String> = _udyamNumber

    fun onUdyamNumberChanged(udyamNumber: String) {
        if (udyamNumber.length <= 19) {
            _udyamNumber.value = udyamNumber
            updateGeneralError(null)
        }
    }

    private val _pinCode1: MutableLiveData<String?> = MutableLiveData("")
    val pinCode1: LiveData<String?> = _pinCode1

    fun onPinCodeChanged1(pinCode1: String, context: Context) {
        val sanitizedInput = pinCode1.replace(Regex("[^0-9]"), "")
        if (pinCode1.length <= 6) {
            _pinCode1.value = sanitizedInput
            if (pinCode1.trim().length == 6) {
                getCity(sanitizedInput, context = context)
                updateGeneralError(null)
            }
        }
    }

    private val _pinCode2: MutableLiveData<String?> = MutableLiveData("")
    val pinCode2: LiveData<String?> = _pinCode2

    fun onPinCodeChanged2(pinCode2: String, context: Context) {
        val sanitizedInput = pinCode2.replace(Regex("[^0-9]"), "")
        if (pinCode2.length <= 6) {
            _pinCode2.value = sanitizedInput
            if (pinCode2.trim().length == 6) {
                getNearCity(sanitizedInput, context = context)
                updateGeneralError(null)
            }
        }
    }

    private val _city1: MutableLiveData<String?> = MutableLiveData("")
    val city1: LiveData<String?> = _city1

    fun onCityChanged(city: String) {
        val sanitizedInput = city.replace(Regex("[^a-zA-Z]"), "")
        _city1.value = sanitizedInput
        updateGeneralError(null)
    }

    private val _city2: MutableLiveData<String?> = MutableLiveData("")
    val city2: LiveData<String?> = _city2

    fun onCityChanged2(city2: String) {
        val sanitizedInput = city2.replace(Regex("[^a-zA-Z]"), "")
        _city2.value = sanitizedInput
        updateGeneralError(null)
    }

    private val _checkBox: MutableLiveData<Boolean> = MutableLiveData(false)
    val checkBox: LiveData<Boolean> = _checkBox

    fun onCheckChanged(checkBox: Boolean) {
        _checkBox.value = checkBox
        updateGeneralError(null)
    }

    private val _officialAddress: MutableLiveData<String?> = MutableLiveData("")
    val officialAddress: LiveData<String?> = _officialAddress

    fun onAddressChanged(address: String) {
        _officialAddress.value = address
        updateGeneralError(null)
    }

    private val _permanentAddress: MutableLiveData<String?> = MutableLiveData("")
    val permanentAddress: LiveData<String?> = _permanentAddress

    fun onAddressTwoChanged(address2: String) {
        _permanentAddress.value = address2
        updateGeneralError(null)
    }

    private val _officialAddressError: MutableLiveData<String?> = MutableLiveData("")
    val officialAddressError: LiveData<String?> = _officialAddressError

    fun updateAddressError(addressError: String?) {
        _officialAddressError.value = addressError
    }

    private val _permanentAddressError: MutableLiveData<String?> = MutableLiveData("")
    val permanentAddressError: LiveData<String?> = _permanentAddressError
    fun updateAddress2Error(address2Error: String?) {
        _permanentAddressError.value = address2Error
    }

    private val _cityError1: MutableLiveData<String> = MutableLiveData("")
    val cityError1: LiveData<String> = _cityError1

    fun updateCityError(cityError: String) {
        _cityError1.value = cityError
    }

    private val _cityError2: MutableLiveData<String> = MutableLiveData("")
    val cityError2: LiveData<String> = _cityError2

    fun updateCityErrorOne(cityError: String) {
        _cityError2.value = cityError
    }

    private val _pinCodeError1: MutableLiveData<String?> = MutableLiveData("")
    val pinCodeError1: LiveData<String?> = _pinCodeError1

    fun updatePinCodeError1(pinCodeError1: String?) {
        _pinCodeError1.value = pinCodeError1
    }

    private val _pinCodeError2: MutableLiveData<String?> = MutableLiveData("")
    val pinCodeError2: LiveData<String?> = _pinCodeError2

    fun updatePinCodeError2(pinCodeError2: String?) {
        _pinCodeError2.value = pinCodeError2
    }

    private val _state1: MutableLiveData<String?> = MutableLiveData("")
    val state1: LiveData<String?> = _state1

    fun onStateChanged(state: String) {
        val sanitizedInput = state.replace(Regex("[^a-zA-Z]"), "")
        _state1.value = sanitizedInput
        updateGeneralError(null)
    }

    private val _state2: MutableLiveData<String?> = MutableLiveData("")
    val state2: LiveData<String?> = _state2

    fun onStateChanged2(state: String) {
        val sanitizedInput = state.replace(Regex("[^a-zA-Z]"), "")
        _state2.value = sanitizedInput
        updateGeneralError(null)
    }

    private val _income: MutableLiveData<String> = MutableLiveData("")
    val income: LiveData<String> = _income

    fun onIncomeChanged(income: String) {
        _income.value = income
        updateGeneralError(null)
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

    private val _successResponse = MutableStateFlow<Signup?>(null)
    val successResponse: StateFlow<Signup?> = _successResponse

    private fun doSignUpApi(profile: Profile, context: Context) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleDoSignUpApi(profile, context)
        }
    }

    private suspend fun handleDoSignUpApi(profile: Profile, context: Context) {
        kotlin.runCatching {
            ApiRepository.signup(profile)
        }.onSuccess { response ->
            response?.let {
                handleSignUpSuccess(response)
            }
        }.onFailure { error ->
            handleSignUpFailure(error, context)
        }
    }

    private suspend fun handleSignUpSuccess(response: Signup) {
        withContext(Dispatchers.Main) {
            _successResponse.value = response
            _isLoading.value = false
            _isLoadingSucess.value = true
        }
    }

    private suspend fun handleSignUpFailure(error: Throwable, context: Context) {
        withContext(Dispatchers.Main) {
            if (error is ResponseException) {
                handleSignUpResponseException(error, context)
            } else {
                CommonMethods().handleGeneralException(
                    error = error, _showInternetScreen = _showInternetScreen,
                    _showTimeOutScreen = _showTimeOutScreen, _unexpectedError = _unexpectedError
                )
            }
            _isLoading.value = false
            _isLoadingSucess.value = false
        }
    }

    private suspend fun handleSignUpResponseException(error: ResponseException, context: Context) {
        val statusCode = error.response.status.value
        val responseBody = withContext(Dispatchers.IO) { error.response.readText() }

        when (statusCode) {
            400 -> {
                try {
                    val jsonObject = JSONObject(responseBody)
                    val dataArray = jsonObject.optJSONArray("data")
                    val message = if (dataArray != null && dataArray.length() > 0) {
                        val firstError = dataArray.getJSONObject(0)
                        firstError.optString("message", "Invalid data format")
                    } else {
                        jsonObject.optString("message", "Invalid data format")
                    }
                    updateErrorMessage(message)
                    CommonMethods().toastMessage(context, message)
                } catch (e: JSONException) {
                    Log.e("Error", "Error parsing response body", e)
                }
            }
            500 -> _showServerIssueScreen.value = true
            501 -> CommonMethods().toastMessage(
                context,
                context.getString(R.string.user_already_exists)
            )
            else -> {
                _unexpectedError.value = true
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

    fun otpButtonValidation(
        firstName: String, lastName: String, emailId: String, officialEmailId: String,
        password: String, mobileNumber: String, dob: String, gender: String, employmentType: String,
        companyName: String, panNumber: String, checkboxValue: Boolean, context: Context,
        firstNameFocus: FocusRequester, lastNameFocus: FocusRequester, udyamNumber: String,
        emailIdFocus: FocusRequester, officialEmailIdFocus: FocusRequester,
        focusPassword: FocusRequester, mobileNumberFocus: FocusRequester, dobFocus: FocusRequester,
        genderFocus: FocusRequester, employmentFocus: FocusRequester, pinCode: String,
        companyNameFocus: FocusRequester, panNumberFocus: FocusRequester, pinCode1: String,
        udyamNumberFocus: FocusRequester, pinCodeFocus: FocusRequester, address: String,
        pincodeFocus1: FocusRequester, adressFocus: FocusRequester, address2: String,
        adress2Focus: FocusRequester, profile: Profile,
    ) {
        clearMessage()
        if (firstName.trim().isEmpty()) {
            updateFirstNameError(context.getString(R.string.please_enter_first_name))
            firstNameFocus.requestFocus()
            requestKeyboard()
        } else if (!Pattern.compile("^[a-zA-Z ]+\$").matcher(firstName).find()) {
            updateFirstNameError(context.getString(R.string.character_special_validation))
            firstNameFocus.requestFocus()
            requestKeyboard()
        } else if (firstName.trim().length < 1) {
            updateFirstNameError(context.getString(R.string
                .first_name_should_contain_minimum_1_letters))
            firstNameFocus.requestFocus()
            requestKeyboard()
        } else if (lastName.trim().isEmpty()) {
            updateLastNameError(context.getString(R.string.please_enter_last_name))
            lastNameFocus.requestFocus()
            requestKeyboard()
        } else if (!Pattern.compile("^[a-zA-Z ]+\$").matcher(lastName).find()) {
            updateLastNameError(context.getString(R.string.character_special_validation))
            lastNameFocus.requestFocus()
            requestKeyboard()
        } else if (emailId.trim().isEmpty()) {
            updatePersonalEmailError(context.getString(R.string.please_enter_email))
            emailIdFocus.requestFocus()
            requestKeyboard()
        } else if (CommonMethods().isValidEmail(emailId) != true) {
            updatePersonalEmailError(context.getString(R.string.please_valid_email_))
            emailIdFocus.requestFocus()
            requestKeyboard()
        } else if (officialEmailId.trim().isEmpty()) {
            updateOfficialEmailError(context.getString(R.string.please_enter_email))
            officialEmailIdFocus.requestFocus()
            requestKeyboard()
        } else if (CommonMethods().isValidEmail(officialEmailId) != true) {
            updateOfficialEmailError(context.getString(R.string.please_valid_email_))
            officialEmailIdFocus.requestFocus()
            requestKeyboard()
        } else if (emailId.equals(officialEmailId)){
            updateOfficialEmailError(context.getString(R.string.id_must_be_different))
            officialEmailIdFocus.requestFocus()
            requestKeyboard()
        } else if (password.trim().isEmpty()) {
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
            focusPassword.requestFocus()
            requestKeyboard()
        } else if (mobileNumber.trim().isEmpty()) {
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
        } else if (dob.trim().isEmpty()) {
            updateDobError(context.getString(R.string.please_enter_dob))
            dobFocus.requestFocus()
            requestKeyboard()
        } else if (gender.trim().isEmpty()) {
            updateGenderError(context.getString(R.string.please_enter_gender))
            genderFocus.requestFocus()
            requestKeyboard()
        }
        else if (panNumber.trim().isEmpty()) {
            updatePanError(context.getString(R.string.please_enter_pan_number))
            panNumberFocus.requestFocus()
            requestKeyboard()
        } else if (CommonMethods().isValidPanNumber(panNumber) != true) {
            updatePanError(context.getString(R.string.please_enter_valid_pan_number))
            panNumberFocus.requestFocus()
            requestKeyboard()
        } else if (employmentType.trim().isEmpty()) {
            updateEmploymentError(context.getString(R.string.please_enter_employment_type))
            employmentFocus.requestFocus()
            requestKeyboard()
        } else if (companyName.trim().isEmpty()) {
            updateCompanyNameError(context.getString(R.string.please_enter_company_name))
            companyNameFocus.requestFocus()
            requestKeyboard()
        }
            //Sugu - as its Optional
//        else if (udyamNumber.trim().isEmpty()) {
//            updateUdyamError(context.getString(R.string.please_enter_udyam_number))
//            udyamNumberFocus.requestFocus()
//            requestKeyboard()
//        }
        else if (!udyamNumber.trim().isEmpty() && CommonMethods().isValidUdyamNumber(udyamNumber)
            != true) {
            updateUdyamError(context.getString(R.string.please_enter_valid_udyam_number))
            udyamNumberFocus.requestFocus()
            requestKeyboard()
        } else if (pinCode.trim().isEmpty()) {
            updatePinCodeError1(context.getString(R.string.please_enter_pincode))
            pinCodeFocus.requestFocus()
            requestKeyboard()
        } else if (pinCode.trim().length < 6) {
            updatePinCodeError1(context.getString(R.string.please_enter_valid_pincode))
            pinCodeFocus.requestFocus()
            requestKeyboard()
        } else if (address.trim().isEmpty()) {
            updateAddressError(context.getString(R.string.please_enter_official_address))
            adressFocus.requestFocus()
            requestKeyboard()
        } else if (address.trim().length < 10) {
            updateAddressError(context.getString(R.string.please_use_min_10_characters))
            adressFocus.requestFocus()
            requestKeyboard()
        }else if (address.trim().length > 255) {
            updateAddressError(context.getString(R.string.please_use_two_fifity_five_characters))
            adressFocus.requestFocus()
            requestKeyboard()
        }else if (pinCode1.trim().isEmpty()) {
            updatePinCodeError2(context.getString(R.string.please_enter_pincode))
            pincodeFocus1.requestFocus()
            requestKeyboard()
        } else if (pinCode1.trim().length < 6) {
            updatePinCodeError2(context.getString(R.string.please_enter_valid_pincode))
            pincodeFocus1.requestFocus()
            requestKeyboard()
        }  else if (address2.trim().isEmpty()) {
            updateAddress2Error(context.getString(R.string.please_enter_the_permanent_address))
            adress2Focus.requestFocus()
            requestKeyboard()
        } else if (address2.trim().length < 10) {
            updateAddress2Error(context.getString(R.string.please_use_min_10_characters))
            adress2Focus.requestFocus()
            requestKeyboard()
        } else if (address2.trim().length > 255) {
            updateAddress2Error(context.getString(R.string.please_use_two_fifity_five_characters))
            adress2Focus.requestFocus()
            requestKeyboard()
        } else if (!checkboxValue) {
            CommonMethods().toastMessage(context = context, toastMsg =  context.getString(R.string.please_accept_our_agent_may_call_u))
        } else {
            doSignUpApi(profile, context)
        }
    }

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating

    private val _upDated = MutableStateFlow(false)
    val upDated: StateFlow<Boolean> = _upDated


    private val _shownMsg = MutableStateFlow(false)
    val shownMsg: StateFlow<Boolean> = _shownMsg

    private val _updateResponse = MutableStateFlow<UpdateProfile?>(null)
    val updateResponse: StateFlow<UpdateProfile?> = _updateResponse

    fun updateShownMsg(boolean: Boolean) {
        _shownMsg.value = boolean
    }

    private fun updateUserDetails(
        profile: Profile, context: Context, navController: NavHostController
    ) {
        _isUpdating.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleUpdateUserDetails(profile, context, navController)
        }
    }

    private suspend fun handleUpdateUserDetails(
        profile: Profile, context: Context, navController: NavHostController,
        checkForAccessToken: Boolean = true
    ) {
        kotlin.runCatching {
            ApiRepository.updateUserDetails(profile)
        }.onSuccess { response ->
            response?.let {
                handleUpdateUserDetailsSuccess(response, profile)
            }
        }.onFailure { error ->
            //Session Management
            if (error is ResponseException &&
                error.response.status.value == 401
            ) {
                //Get Access Token using RefreshToken
                if (checkForAccessToken && handleAuthGetAccessTokenApi()) {
                    handleUpdateUserDetails(profile, context, navController, false)
                } else {
                    // If unable to refresh the token, navigate to the sign-in page
                    withContext(Dispatchers.Main) {
                        navigateSignInPage(navController)
                    }
                }
            } else {
                handleFailure(context = context, error = error)
            }
        }
    }

    private suspend fun handleUpdateUserDetailsSuccess(response: UpdateProfile, profile: Profile) {
        withContext(Dispatchers.Main) {
            _isUpdating.value = false
            _upDated.value = true
            _shownMsg.value = false
            _updateResponse.value = response
        }
    }

    private suspend fun handleFailure(context: Context, error: Throwable) {
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
        }
        _isUpdating.value = false
    }

    fun updateValidation(
        navController: NavHostController, checkboxValue: Boolean, context: Context,
        firstNameFocus: FocusRequester,
        lastNameFocus: FocusRequester,
        personalEmailIdFocus: FocusRequester,
        officialEmailIdFocus: FocusRequester,
        dobFocus: FocusRequester,
        genderFocus: FocusRequester,
        panNumberFocus: FocusRequester,
        employmentTypeFocus: FocusRequester,
        companyNameFocus: FocusRequester,
        udyamNumberFocus: FocusRequester,
        pinCodeFocus1: FocusRequester,
        officialAddressFocus : FocusRequester,
        pinCodeFocus2: FocusRequester,
        permanentAddressFocus : FocusRequester,
        profile: Profile,isGST:Boolean
    ) {
        clearMessage()
        val pinCode1 = profile.pincode1 ?: ""
        val pinCode2 = profile.pincode2 ?: ""
        val officialAddress = profile.address1 ?: ""
        val permanentAddress = profile.address2 ?: ""

        if (profile.firstName.isNullOrEmpty()) {
            updateFirstNameError(context.getString(R.string.please_enter_first_name))
            firstNameFocus.requestFocus()
            requestKeyboard()
        } else if (!Pattern.compile("^[a-zA-Z ]+\$").matcher(profile.firstName).find()) {
            updateFirstNameError(context.getString(R.string.character_special_validation))
            firstNameFocus.requestFocus()
            requestKeyboard()
        } else if (profile.firstName.trim().length < 1) {
            updateFirstNameError(context.getString(R.string
                .first_name_should_contain_minimum_1_letters))
            firstNameFocus.requestFocus()
            requestKeyboard()
        } else if (profile.lastName.isNullOrEmpty()) {
            updateLastNameError(context.getString(R.string.please_enter_last_name))
            lastNameFocus.requestFocus()
            requestKeyboard()
        } else if (!Pattern.compile("^[a-zA-Z ]+\$").matcher(profile.lastName).find()) {
            updateLastNameError(context.getString(R.string.character_special_validation))
            lastNameFocus.requestFocus()
            requestKeyboard()
        } else if (profile.email.isNullOrEmpty()) {
            updatePersonalEmailError(context.getString(R.string.please_enter_email))
            personalEmailIdFocus.requestFocus()
            requestKeyboard()
        } else if (CommonMethods().isValidEmail(profile.email) != true) {
            updatePersonalEmailError(context.getString(R.string.please_enter_valid_email_id))
            personalEmailIdFocus.requestFocus()
            requestKeyboard()
        } else if (profile.officialEmail.isNullOrEmpty()) {
            updateOfficialEmailError(context.getString(R.string.please_enter_email))
            officialEmailIdFocus.requestFocus()
            requestKeyboard()
        } else if (CommonMethods().isValidEmail(profile.officialEmail) != true) {
            updateOfficialEmailError(context.getString(R.string.please_enter_valid_email_id))
            officialEmailIdFocus.requestFocus()
            requestKeyboard()
        } else if (profile.officialEmail == profile.email) {
            updateOfficialEmailError(context.getString(R.string.email_id_and_official_email_id_must_be_different))
            updatePersonalEmailError(context.getString(R.string.email_id_and_official_email_id_must_be_different))
            officialEmailIdFocus.requestFocus()
            personalEmailIdFocus.requestFocus()
            requestKeyboard()
        } else if (profile.dob.isNullOrEmpty()) {
            updateDobError(context.getString(R.string.please_enter_dob))
            dobFocus.requestFocus()
            requestKeyboard()
        }  else if (!CommonMethods().isValidDob(profile.dob)) {
            updateDobError(context.getString(R.string.please_enter_valid_dob))
            dobFocus.requestFocus()
            requestKeyboard()
        } else if (profile.gender.isNullOrEmpty()) {
            updateGenderError(context.getString(R.string.please_select_gender))
            genderFocus.requestFocus()
            requestKeyboard()
        }else if (profile.panNumber.isNullOrEmpty()) {
            updatePanError(context.getString(R.string.please_enter_pan_number))
            panNumberFocus.requestFocus()
            requestKeyboard()
        } else if (CommonMethods().isValidPanNumber(profile.panNumber) != true) {
            updatePanError(context.getString(R.string.please_enter_valid_pan_number))
            panNumberFocus.requestFocus()
            requestKeyboard()
        } else if (profile.employmentType.isNullOrEmpty()) {
            updateEmploymentError(context.getString(R.string.please_enter_employment_type))
            employmentTypeFocus.requestFocus()
            requestKeyboard()
        } else if (profile.companyName.isNullOrEmpty()) {
            updateCompanyNameError(context.getString(R.string.please_enter_company_name))
            companyNameFocus.requestFocus()
            requestKeyboard()
        } else if(isGST && profile.udyamNumber.isNullOrEmpty()){
//            if (profile.udyamNumber.isNullOrEmpty()) {
                updateUdyamError(context.getString(R.string.please_enter_udyam_number))
                udyamNumberFocus.requestFocus()
                requestKeyboard()

            }
           else  if (!profile.udyamNumber.isNullOrEmpty() && CommonMethods().isValidUdyamNumber(profile.udyamNumber) != true ) {
                updateUdyamError(context.getString(R.string.please_enter_valid_udyam_number))
                udyamNumberFocus.requestFocus()
                requestKeyboard()

            }
        else if (officialAddress.trim().isEmpty()) {
            updateAddressError(context.getString(R.string.please_enter_official_address))
            officialAddressFocus.requestFocus()
            requestKeyboard()
        }  else if (isNotValidAddress(officialAddress.trim())) {
            updateAddressError(context.getString(R.string.please_enter_proper_official_address))
            officialAddressFocus.requestFocus()
            requestKeyboard()
        } else if (officialAddress.trim().length < 10) {
            updateAddressError(context.getString(R.string.please_use_min_10_characters))
            officialAddressFocus.requestFocus()
            requestKeyboard()
        }else if (officialAddress.trim().length > 255) {
            updateAddressError(context.getString(R.string.please_use_two_fifity_five_characters))
            officialAddressFocus.requestFocus()
            requestKeyboard()
        } else if (pinCode1.trim().isEmpty()) {
            updatePinCodeError1(context.getString(R.string.please_enter_pincode))
            pinCodeFocus1.requestFocus()
            requestKeyboard()
        } else if (pinCode1.trim().length < 6) {
            updatePinCodeError1(context.getString(R.string.please_enter_valid_pincode))
            pinCodeFocus1.requestFocus()
            requestKeyboard()
        } else if (pinCode1.trim().isEmpty()) {
            updatePinCodeError1(context.getString(R.string.please_enter_pincode))
            pinCodeFocus1.requestFocus()
            requestKeyboard()
        } else if (pinCode1.trim().length < 6) {
            updatePinCodeError1(context.getString(R.string.please_enter_valid_pincode))
            pinCodeFocus1.requestFocus()
            requestKeyboard()
        } else if (permanentAddress.trim().isEmpty()) {
            updateAddress2Error(context.getString(R.string.please_enter_the_permanent_address))
            permanentAddressFocus.requestFocus()
            requestKeyboard()
        } else if (isNotValidAddress(permanentAddress.trim())) {
            updateAddress2Error(context.getString(R.string.please_enter_proper_permanent_address))
            permanentAddressFocus.requestFocus()
            requestKeyboard()
        } else if (permanentAddress.trim().length < 10) {
            updateAddress2Error(context.getString(R.string.please_use_min_10_characters))
            permanentAddressFocus.requestFocus()
            requestKeyboard()
        }else if (permanentAddress.trim().length > 255) {
            updateAddress2Error(context.getString(R.string.please_use_two_fifity_five_characters))
            permanentAddressFocus.requestFocus()
            requestKeyboard()
        } else if (pinCode2.trim().isEmpty()) {
            updatePinCodeError2(context.getString(R.string.please_enter_pincode))
            pinCodeFocus2.requestFocus()
            requestKeyboard()
        } else if (pinCode2.trim().length < 6) {
            updatePinCodeError2(context.getString(R.string.please_enter_valid_pincode))
            pinCodeFocus2.requestFocus()
            requestKeyboard()
        } else if (pinCode2.trim().isEmpty()) {
            updatePinCodeError2(context.getString(R.string.please_enter_pincode))
            pinCodeFocus2.requestFocus()
            requestKeyboard()
        } else if (pinCode2.trim().length < 6) {
            updatePinCodeError2(context.getString(R.string.please_enter_valid_pincode))
            pinCodeFocus2.requestFocus()
            requestKeyboard()
        }  else if (!checkboxValue) {
            CommonMethods().toastMessage(context,context.getString(R.string.please_accept_our_agent_may_call_u))
        } else {
            updateUserDetails(profile, context, navController)
        }
    }

    private val _checkBoxDetail: MutableLiveData<Boolean> = MutableLiveData(false)
    val checkBoxDetail: LiveData<Boolean> = _checkBoxDetail

    fun onCheckBoxDetailChanged(checkBoxDetail: Boolean) {
        _checkBoxDetail.value = checkBoxDetail
        if (checkBoxDetail) {
            updateGeneralError(null)
            _isCompleted.value = true //Sugu

        }else{
            _isCompleted.value = false
        }
    }

    fun onCheckBoxDetailReset() {
        _checkBoxDetail.value = false
    }

    private val _inProgress = MutableStateFlow(false)
    val inProgress: StateFlow<Boolean> = _inProgress

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted

    private val _getUserResponse = MutableStateFlow<ProfileResponse?>(null)
    val getUserResponse: StateFlow<ProfileResponse?> = _getUserResponse

    fun getUserDetail(context: Context, navController: NavHostController) {
        _inProgress.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleUserDetail(context, navController)
        }
    }

    private suspend fun handleUserDetail(
        context: Context, navController: NavHostController, checkForAccessToken: Boolean = true
    ) {
        kotlin.runCatching {
            ApiRepository.getUserDetail()
        }.onSuccess { response ->
            response?.let {
                handleSuccessUserDetail(response)
            }
        }.onFailure { error ->
            if (error is ResponseException &&
                error.response.status.value == 401
            ) {
                //Get Access Token using RefreshToken
                if (checkForAccessToken && handleAuthGetAccessTokenApi()) {
                    // Retry getting user details with the new access token
                    handleUserDetail(context, navController, false)
                } else {
                    // If unable to refresh the token, navigate to the sign-in page
                    withContext(Dispatchers.Main) {
                        navigateSignInPage(navController)
                    }
                }
            } else {
                // Handle other types of failures
                handleFailure(error = error, context =  context)
            }
        }
    }

    private suspend fun handleSuccessUserDetail(response: ProfileResponse?) {
        withContext(Dispatchers.Main) {
            response?.let {
                _firstName.value = it.data?.firstName
                _lastName.value = it.data?.lastName
                _personalEmailId.value = it.data?.email
                _officialEmailId.value = it.data?.officialEmail
                _mobileNumber.value = it.data?.mobileNumber
                _dob.value = it.data?.dob
                _gender.value = it.data?.gender
                _panNumber.value = it.data?.panNumber
                _employment.value = it.data?.employmentType
                _companyName.value = it.data?.companyName
                _udyamNumber.value = it.data?.udyamNumber ?: ""
                _officialAddress.value = it.data?.address1
                _permanentAddress.value = it.data?.address2
                _city1.value = it.data?.city1
                _city2.value = it.data?.city2
                _pinCode1.value = it.data?.pincode1
                _pinCode2.value = it.data?.pincode2
                _state1.value = it.data?.state1
                _state2.value = it.data?.state2
                _inProgress.value = false
                _isCompleted.value = true
                _getUserResponse.value = it
                TokenManager.save("userName", it.data?.firstName.toString())
                TokenManager.save("mobileNumber", it.data?.mobileNumber.toString())
                TokenManager.save("address", it.data?.pincode1.toString())
            }
        }
    }

    private val _pinCodeResponse = MutableStateFlow<PincodeModel?>(null)
    val pinCodeResponse: StateFlow<PincodeModel?> = _pinCodeResponse

    private fun getCity(pinCode: String,context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                ApiRepository.getCity(pinCode)
            }.onSuccess { response ->
                response?.let {
                    _pinCodeResponse.value = it
                    withContext(Dispatchers.Main) {
                        if (it.cities.isNullOrEmpty() || it.state.isNullOrEmpty()) {
                            Log.d("Null Or Empty", "City or state is null or empty")
                        } else {
                            _city1.value = it.cities[0]
                            _state1.value = it.state
                        }
                    }
                } ?: withContext(Dispatchers.Main) {
                    _pinCode1.value = ""
                    updatePinCodeError1("Invalid Pincode")
                   CommonMethods().toastMessage(
                       context = context, toastMsg = "Please Enter Valid PinCode"
                   )
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    Log.d("PinCodeError", it.toString())
                }
            }
        }
    }

    private val _nearCityResponse = MutableStateFlow<PincodeModel?>(null)
    val nearCityResponse: StateFlow<PincodeModel?> = _nearCityResponse

    private fun getNearCity(pinCode1: String,context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                ApiRepository.getCity(pinCode1)
            }.onSuccess { response ->
                response?.let {
                    _nearCityResponse.value = it
                    withContext(Dispatchers.Main) {
                        _city2.value = it.cities?.get(0)
                        _state2.value = it.state
                    }
                } ?: withContext(Dispatchers.Main) {
                    _pinCode2.value = ""
                    updatePinCodeError2("Invalid Pincode")
                    CommonMethods().toastMessage(
                        context = context, toastMsg = "Please Enter Valid PinCode"
                    )
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    Log.d("PinCodeError", it.toString())
                }
            }
        }
    }

    private val _resendingOtp = MutableStateFlow(false)
    val resendingOtp: StateFlow<Boolean> = _resendingOtp

    private val _reSendedOtp = MutableStateFlow(false)
    val reSendedOtp: StateFlow<Boolean> = _reSendedOtp

    private val _resendOtpResponse = MutableStateFlow<Data?>(null)
    val resendOtpResponse: StateFlow<Data?> = _resendOtpResponse

    fun authResendOTP(orderId: String, context: Context, navController: NavHostController) {
        _resendingOtp.value = true
        viewModelScope.launch(Dispatchers.IO) {
            handleReSendOtp(orderId, context, navController)
        }
    }

    private suspend fun handleReSendOtp(
        orderId: String, context: Context, navController: NavHostController,
        checkForAccessToken: Boolean = true
    ) {
        kotlin.runCatching {
            ApiRepository.authResendOTP(orderId)
        }.onSuccess { response ->
            response?.let {
                handleReSendOtpSuccess(response)
            }
        }.onFailure { error ->
            if (error is ResponseException &&
                error.response.status.value == 401
            ) {
                //Get Access Token using RefreshToken
                if (checkForAccessToken && handleAuthGetAccessTokenApi()) {
                    handleReSendOtp(orderId, context, navController, false)
                } else {
                    // If unable to refresh the token, navigate to the sign-in page
                    withContext(Dispatchers.Main) {
                        navigateSignInPage(navController)
                    }
                }
            } else {
                handleReSendOtpFailure(error, context)
            }
        }
    }

    private suspend fun handleReSendOtpSuccess(response: Data) {
        withContext(Dispatchers.Main) {
            _reSendedOtp.value = true
            _resendOtpResponse.value = response
            _resendingOtp.value = false
        }
    }

    private suspend fun handleReSendOtpFailure(error: Throwable, context: Context) {
        withContext(Dispatchers.Main) {
            if (error is ResponseException) {
                handleReSendOtpResponseException(error, context)
            } else {
                CommonMethods().handleGeneralException(
                    error = error, _showInternetScreen = _showInternetScreen,
                    _showTimeOutScreen = _showTimeOutScreen, _unexpectedError = _unexpectedError
                )
            }
            _resendingOtp.value = false
        }
    }

    private fun handleReSendOtpResponseException(error: ResponseException, context: Context) {
        val statusCode = error.response.status.value
        when (statusCode) {
            400 -> {
                CommonMethods().toastMessage(context,"Please Enter Valid Otp")
             }

            401 -> {
                _unAuthorizedUser.value = true
            }

            500 -> {
                _showServerIssueScreen.value = true
            }

            else -> {
                _unexpectedError.value = true
            }
        }
    }

    private val _logoutResponse = MutableStateFlow<Logout?>(null)
    val logoutResponse: StateFlow<Logout?> = _logoutResponse

    fun logout(refreshToken: String, navController: NavHostController,checkForAccessToken: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                ApiRepository.logout(refreshToken)
            }.onSuccess { response ->
                withContext(Dispatchers.Main) {
                    response?.let {
                        _logoutResponse.value = it
                        TokenManager.save("accessToken", "")
                        navigateSignInPage(navController, true)
                    }
                }
            }.onFailure { error ->
                if (error is ResponseException &&
                    error.response.status.value == 401
                ) {
                    if (checkForAccessToken && handleAuthGetAccessTokenApi()) {
                        logout(refreshToken, navController,false)
                    } else {
                        withContext(Dispatchers.Main) {
                            _logoutResponse.value = null
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _logoutResponse.value = null
                    }
                }
            }
        }
    }


    private fun isNotValidAddress(input: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9,./#'\\-\\s]*$")
        return !regex.matches(input)
    }
}