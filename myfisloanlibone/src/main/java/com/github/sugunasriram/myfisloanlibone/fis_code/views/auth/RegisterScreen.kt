package com.github.sugunasriram.myfisloanlibone.fis_code.views.auth

import android.app.DatePickerDialog
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenteredMoneyImage
import com.github.sugunasriram.myfisloanlibone.fis_code.components.DropDownTextField
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InputField
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToOtpScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.PincodeModel
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.Profile
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.Signup
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appDarkTeal
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.RegisterViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(navController: NavHostController) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val countryCode = stringResource(id = R.string.country_code)
    val income = "500000"
    val role = "USER"

    val registerViewModel: RegisterViewModel = viewModel()
    val firstName: String? by registerViewModel.firstName.observeAsState("")
    val lastName: String? by registerViewModel.lastName.observeAsState("")
    val gender: String? by registerViewModel.gender.observeAsState("")
    val employment: String? by registerViewModel.employment.observeAsState("")
    val emailId: String? by registerViewModel.personalEmailId.observeAsState("")
    val officialEmailId: String? by registerViewModel.officialEmailId.observeAsState("")
    val companyName: String? by registerViewModel.companyName.observeAsState("")
    val mobileNumber: String? by registerViewModel.mobileNumber.observeAsState("")
    val dob: String? by registerViewModel.dob.observeAsState("")
    val udyamNumber: String by registerViewModel.udyamNumber.observeAsState("")
    val panNumber: String? by registerViewModel.panNumber.observeAsState("")
    val checkboxValue: Boolean? by registerViewModel.checkBox.observeAsState(false)
    val password: String? by registerViewModel.password.observeAsState("")
    val address1: String? by registerViewModel.officialAddress.observeAsState("")
    val address2: String? by registerViewModel.permanentAddress.observeAsState("")
    val city1: String? by registerViewModel.city1.observeAsState("")
    val pincode1: String? by registerViewModel.pinCode1.observeAsState("")
    val city2: String? by registerViewModel.city2.observeAsState("")
    val pincode2: String? by registerViewModel.pinCode2.observeAsState("")
    val showInternetScreen by registerViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by registerViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by registerViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by registerViewModel.unexpectedError.observeAsState(false)

    val firstNameError: String? by registerViewModel.firstNameError.observeAsState("")
    val lastNameError: String? by registerViewModel.lastNameError.observeAsState("")
    val emaiIdError: String? by registerViewModel.personalEmailIdError.observeAsState("")
    val officialEmaiIdError: String? by registerViewModel.officialEmailIdError.observeAsState("")
    val passwordError: String? by registerViewModel.passwordError.observeAsState("")
    val mobileNumberError: String? by registerViewModel.mobileNumberError.observeAsState("")
    val dobError: String? by registerViewModel.dobError.observeAsState("")
    val companyNameError: String? by registerViewModel.companyNameError.observeAsState("")
    val genderError: String? by registerViewModel.genderError.observeAsState("")
    val employmentError: String? by registerViewModel.employmentError.observeAsState("")
    val addressError: String? by registerViewModel.officialAddressError.observeAsState("")
    val address2Error: String? by registerViewModel.permanentAddressError.observeAsState("")
    val cityError1: String? by registerViewModel.cityError1.observeAsState("")
    val pincodeError1: String? by registerViewModel.pinCodeError1.observeAsState("")
    val cityError2: String? by registerViewModel.cityError2.observeAsState("")
    val pincodeError2: String? by registerViewModel.pinCodeError2.observeAsState("")
    val panError: String? by registerViewModel.panError.observeAsState("")
    val udyamError: String? by registerViewModel.udyamError.observeAsState("")

    val isLoading by registerViewModel.isLoading.collectAsState()
    val isLoadingSucess by registerViewModel.isLoadingSucess.collectAsState()
    val successResponse by registerViewModel.successResponse.collectAsState()
    val pincodeResponse by registerViewModel.pinCodeResponse.collectAsState()
    val nearCityResponse by registerViewModel.nearCityResponse.collectAsState()
    val shouldShowKeyboard by registerViewModel.shouldShowKeyboard.observeAsState(false)

    LaunchedEffect(shouldShowKeyboard) {
        if (shouldShowKeyboard) {
            keyboardController?.show()
            registerViewModel.resetKeyboardRequest()
        }
    }

    val (firstNameFocus) = FocusRequester.createRefs()
    val (lastNameFocus) = FocusRequester.createRefs()
    val (emailIdFocus) = FocusRequester.createRefs()
    val (officialEmailIdFocus) = FocusRequester.createRefs()
    val (focusPassword) = FocusRequester.createRefs()
    val (mobileNumberFocus) = FocusRequester.createRefs()
    val (dobFocus) = FocusRequester.createRefs()
    val (genderFocus) = FocusRequester.createRefs()
    val (panNumberFocus) = FocusRequester.createRefs()
    val (companyNameFocus) = FocusRequester.createRefs()
    val (employmentFocus) = FocusRequester.createRefs()
    val (udyamNumberFocus) = FocusRequester.createRefs()
    val (addressFocus) = FocusRequester.createRefs()
    val (address2Focus) = FocusRequester.createRefs()
    val (cityFocus) = FocusRequester.createRefs()
    val (pincodeFocus) = FocusRequester.createRefs()
    val (cityFocus1) = FocusRequester.createRefs()
    val (pincodeFocus1) = FocusRequester.createRefs()

    BackHandler { navigateSignInPage(navController) }

    if (!showInternetScreen && !showTimeOutScreen && !showServerIssueScreen && !unexpectedErrorScreen) {
        if (isLoading) {
            CenterProgress()
        } else {
            if (isLoadingSucess) {
                onRegisteredSuccessful(successResponse, navController, mobileNumber)
            } else {
                Register(
                    checkboxValue = checkboxValue, navController = navController, role = role,
                    registerViewModel = registerViewModel, mobileNumber = mobileNumber,
                    firstName = firstName, lastName = lastName, emailId = emailId, gender = gender, employmentType = employment,
                    officialEmailId = officialEmailId, password = password, dob = dob,
                    companyName = companyName, panNumber = panNumber, udyamNumber = udyamNumber,
                    pincode1 = pincode1, pincode2 = pincode2, address1 = address1, income = income,
                    address2 = address2, firstNameFocus = firstNameFocus, context = context,
                    lastNameFocus = lastNameFocus, emailIdFocus = emailIdFocus,
                    officialEmailIdFocus = officialEmailIdFocus, dobFocus = dobFocus,
                    focusPassword = focusPassword, companyNameFocus = companyNameFocus,
                    mobileNumberFocus = mobileNumberFocus, panNumberFocus = panNumberFocus,
                    udyamNumberFocus = udyamNumberFocus, employmentFocus = employmentFocus,
                    genderFocus = genderFocus, countryCode = countryCode, panError = panError,
                    pincodeFocus = pincodeFocus, pincodeFocus1 = pincodeFocus1, city1 = city1,
                    addressFocus = addressFocus, address2Focus = address2Focus,
                    pincodeResponse = pincodeResponse, nearCityResponse = nearCityResponse,
                    firstNameError = firstNameError, lastNameError = lastNameError,
                    dobError = dobError, emaiIdError = emaiIdError, genderError = genderError,
                    officialEmaiIdError = officialEmaiIdError, passwordError = passwordError,
                    mobileNumberError = mobileNumberError, udyamError = udyamError,
                    employmentError = employmentError, cityFocus = cityFocus, city2 = city2,
                    companyNameError = companyNameError, pincodeError1 = pincodeError1,
                    addressError = addressError, pincodeError2 = pincodeError2,
                    cityFocus1 = cityFocus1, cityError2 = cityError2, address2Error = address2Error,
                    cityError1 = cityError1,
                )
            }
        }
    } else {
        CommonMethods().HandleErrorScreens(
            navController = navController, showInternetScreen = showInternetScreen,
            showTimeOutScreen = showTimeOutScreen, showServerIssueScreen = showServerIssueScreen,
            unexpectedErrorScreen = unexpectedErrorScreen
        )
    }
}

@Composable
fun Register(
    checkboxValue: Boolean?, navController: NavHostController, registerViewModel: RegisterViewModel,
    mobileNumber: String?, firstName: String?, lastName: String?, emailId: String?, dob: String?,
    gender : String?, employmentType : String?,
    officialEmailId: String?, password: String?, companyName: String?, panNumber: String?,
    udyamNumber: String, pincode1: String?, pincode2: String?, address1: String?, role: String,
    address2: String?, firstNameFocus: FocusRequester, lastNameFocus: FocusRequester,
    emailIdFocus: FocusRequester, officialEmailIdFocus: FocusRequester, dobFocus: FocusRequester,
    focusPassword: FocusRequester, companyNameFocus: FocusRequester, context: Context,
    mobileNumberFocus: FocusRequester, panNumberFocus: FocusRequester, city1: String?,
    udyamNumberFocus: FocusRequester, employmentFocus: FocusRequester, genderFocus: FocusRequester,
    income: String, countryCode: String, pincodeFocus: FocusRequester,
    pincodeFocus1: FocusRequester, addressFocus: FocusRequester, address2Focus: FocusRequester,
    pincodeResponse: PincodeModel?, nearCityResponse: PincodeModel?, firstNameError: String?,
    lastNameError: String?, dobError: String?, emaiIdError: String?,
    officialEmaiIdError: String?, mobileNumberError: String?, passwordError: String?,
    genderError: String?, panError: String?, employmentError: String?, udyamError: String?,
    companyNameError: String?, cityFocus: FocusRequester, pincodeError1: String?,
    cityError1: String?, addressError: String?, city2: String?, pincodeError2: String?,
    cityFocus1: FocusRequester, cityError2: String?, address2Error: String?
) {
    var genderSelectedText by remember { mutableStateOf(gender ?: "") }
    var genderExpand by remember { mutableStateOf(false) }
    val genderList = listOf(
        stringResource(id = R.string.male), stringResource(id = R.string.female),
        stringResource(id = R.string.transgender)
    )
    val onGenderDismiss: () -> Unit = { genderExpand = false }
    val onGenderSelected: (String) -> Unit = { selectedText ->
        genderSelectedText = selectedText
        registerViewModel.onGenderChanged(selectedText)
    }

    var employmentSelectedText by remember { mutableStateOf(employmentType ?: "") }
    var employement by remember { mutableStateOf(false) }
    val emplyTypeList = listOf(
        stringResource(id = R.string.salaried), stringResource(id = R.string.self_employment)
    )
    val onEmploymentSelected: (String) -> Unit = { selectedText ->
        employmentSelectedText = selectedText
        registerViewModel.onEmploymentChanged(selectedText)

    }
    val onEmploymentDismiss: () -> Unit = { employement = false }
    checkboxValue?.let {
        FixedTopBottomScreen(
            isSelfScrollable = false, navController = navController, showBackButton = true,
            buttonText = stringResource(id = R.string.next), showCheckBox = true, checkboxState = it,
            checkBoxText = stringResource(id = R.string.register_check),
            onCheckBoxChange = { registerViewModel.onCheckChanged(it) },
            onBackClick = { navigateSignInPage(navController) },
            backGroudColorChange = if (checkboxValue) true else false,
            onClick = {
                onRegisterClicked(
                    genderSelectedText = genderSelectedText, firstName = firstName,
                    employmentSelectedText = employmentSelectedText, lastName = lastName,
                    emailId = emailId, officialEmailId = officialEmailId, password = password,
                    dob = dob, companyName = companyName, mobileNumber = mobileNumber,
                    checkboxValue = checkboxValue, panNumber = panNumber, udyamNumber = udyamNumber,
                    pincode1 = pincode1, pincode2 = pincode2, address1 = address1,
                    address2 = address2, registerViewModel = registerViewModel,
                    firstNameFocus = firstNameFocus, lastNameFocus = lastNameFocus,
                    emailIdFocus = emailIdFocus, officialEmailIdFocus = officialEmailIdFocus,
                    dobFocus = dobFocus, focusPassword = focusPassword, role = role,
                    companyNameFocus = companyNameFocus, mobileNumberFocus = mobileNumberFocus,
                    panNumberFocus = panNumberFocus, udyamNumberFocus = udyamNumberFocus,
                    genderFocus = genderFocus, employmentFocus = employmentFocus, context = context,
                    pincodeFocus = pincodeFocus, pincodeFocus1 = pincodeFocus1, income = income,
                    addressFocus = addressFocus, address2Focus = address2Focus,
                    countryCode = countryCode, pincodeResponse = pincodeResponse,
                    nearCityResponse = nearCityResponse,
                )
            }
        ) {
            CenteredMoneyImage(
                image = R.drawable.register_page_image, imageSize = 150.dp, top = 10.dp,
                contentScale = ContentScale.Fit
            )
            RegisterText(
                text = stringResource(id = R.string.borrower_register), textColor = appDarkTeal,
                style = normal32Text700
            )
            RegisterText(
                text = stringResource(id = R.string.basic_detail), style = normal20Text700
            )
            NameFeilds(
                firstName = firstName, lastName = lastName, registerViewModel = registerViewModel,
                firstNameFocus = firstNameFocus, lastNameFocus = lastNameFocus,
                firstNameError = firstNameError, lastNameError = lastNameError,
                emailIdFocus = emailIdFocus
            )
            EmailFeilds(
                emailId = emailId, officialEmailId = officialEmailId,
                registerViewModel = registerViewModel, emailIdFocus = emailIdFocus,
                officialEmailIdFocus = officialEmailIdFocus, emaiIdError = emaiIdError,
                officialEmaiIdError = officialEmaiIdError, focusPassword = focusPassword
            )

            PasswordMobileFeilds(
                password = password, registerViewModel = registerViewModel, dobFocus = dobFocus,
                mobileNumber = mobileNumber, focusPassword = focusPassword,
                mobileNumberFocus = mobileNumberFocus, mobileNumberError = mobileNumberError,
                passwordError = passwordError,
            )


            dobError?.let {
                dob?.let { dob ->
                    DatePickerFeild(
                        dob = dob, dobFocus = dobFocus, genderFocus = genderFocus,
                        registerViewModel = registerViewModel, dobError = it, context = context
                    )
                }
            }
            DropDownTextField(
                start = 40.dp, end = 40.dp, top = 10.dp, selectedText = genderSelectedText,
                hint = stringResource(id = R.string.gender), focus = genderFocus,
                onNextFocus = panNumberFocus, expand = genderExpand, error = genderError,
                setExpand = { genderExpand = it }, itemList = genderList,
                onDismiss = onGenderDismiss,onItemSelected = onGenderSelected
            )

            panNumber?.let { panNumber ->
                InputField(
                    inputText = panNumber.uppercase(), hint = stringResource(id = R.string.pan),
                    top = 10.dp,
                    modifier = Modifier.focusRequester(panNumberFocus),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Next,
                        keyboardType = if (panNumber.length in 0..4 || panNumber.length == 9) {
                            KeyboardType.Text
                        } else {
                            KeyboardType.Text
                        }
                    ),
                    keyboardActions = KeyboardActions(onNext = { employmentFocus.requestFocus() }),
                    onValueChange = {
                        registerViewModel.onPanNumberChanged(it)
                        registerViewModel.updatePanError(null)
                    },
                    error = panError
                )
            }

            DropDownTextField(
                start = 40.dp, end = 40.dp, top = 10.dp, selectedText = employmentSelectedText,
                hint = stringResource(id = R.string.employment_type_mandatory), expand = employement,
                focus = employmentFocus, onNextFocus = companyNameFocus,
                setExpand = { employement = it }, itemList = emplyTypeList,
                onDismiss = onEmploymentDismiss, error = employmentError,
                onItemSelected = onEmploymentSelected
            )
            CompanyUdyamFeilds(
                companyName = companyName, udyamNumber = udyamNumber, udyamError = udyamError,
                registerViewModel = registerViewModel, udyamNumberFocus = udyamNumberFocus,
                companyNameFocus = companyNameFocus, companyNameError = companyNameError,
                pincodeFocus = pincodeFocus
            )
            CityPincodeFeilds(
                pincode1 = pincode1, pincodeFocus = pincodeFocus, cityFocus = cityFocus,
                registerViewModel = registerViewModel, pincodeError1 = pincodeError1,
                pincodeResponse = pincodeResponse, city1 = city1, addressFocus = addressFocus,
                cityError1 = cityError1, context = context
            )
            AddressFeilds(
                address1 = address1, addressFocus = addressFocus, pincodeFocus1 = pincodeFocus1,
                registerViewModel = registerViewModel, addressError = addressError,
                pincode2 = pincode2, address2Focus = address2Focus, pincodeError2 = pincodeError2,
                nearCityResponse = nearCityResponse, city2 = city2, cityFocus1 = cityFocus1,
                cityError2 = cityError2, address2 = address2, address2Error = address2Error,
                context = context
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun AddressFeilds(
    address1: String?, addressFocus: FocusRequester, pincodeFocus1: FocusRequester,
    registerViewModel: RegisterViewModel, addressError: String?, pincode2: String?,
    address2Focus: FocusRequester, pincodeError2: String?, nearCityResponse: PincodeModel?,
    city2: String?, cityFocus1: FocusRequester, cityError2: String?, address2: String?,
    address2Error: String?,context: Context
) {
    InputField(
        inputText = address1, hint = stringResource(id = R.string.official_address_with_star), top = 10.dp,
        modifier = Modifier.focusRequester(addressFocus),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onNext = { pincodeFocus1.requestFocus() }),
        onValueChange = {
            registerViewModel.onAddressChanged(it)
            registerViewModel.updateAddressError(null)
        },
        error = addressError
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        InputField(
            inputText = pincode2, hint = stringResource(id = R.string.pincode_mandatory), start = 0.dp,
            end = 0.dp, top = 10.dp,
            modifier = Modifier
                .weight(1f)
                .focusRequester(pincodeFocus1),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onNext = { address2Focus.requestFocus() }),
            onValueChange = {
                registerViewModel.onPinCodeChanged2(it,context = context)
                registerViewModel.updatePinCodeError2(null)
            },
            error = pincodeError2
        )
        InputField(
            inputText = nearCityResponse?.cities?.get(0), hint = stringResource(id = R.string.city), start = 5.dp,
            end = 0.dp, top = 10.dp, enable = false, readOnly = city2?.isEmpty() == true,
            modifier = Modifier
                .weight(1f)
                .focusRequester(cityFocus1),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onNext = { address2Focus.requestFocus() }),
            onValueChange = { registerViewModel.onCityChanged2(it) },
            error = cityError2,
        )
    }

    InputField(
        inputText = address2, hint = stringResource(id = R.string.permanent_address_with_star), top = 10.dp,
        modifier = Modifier.focusRequester(address2Focus),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        onValueChange = {
            registerViewModel.onAddressTwoChanged(it)
            registerViewModel.updateAddress2Error(null)
        },
        error = address2Error
    )
}

@Composable
fun CityPincodeFeilds(
    pincode1: String?, pincodeFocus: FocusRequester, cityFocus: FocusRequester, city1: String?,
    registerViewModel: RegisterViewModel, pincodeError1: String?,context: Context,
    pincodeResponse: PincodeModel?, addressFocus: FocusRequester, cityError1: String?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        InputField(
            inputText = pincode1, hint = stringResource(id = R.string.pincode_mandatory), start = 0.dp,
            end = 0.dp, top = 10.dp,
            modifier = Modifier
                .weight(1f)
                .focusRequester(pincodeFocus),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onNext = { cityFocus.requestFocus() }),
            onValueChange = {
                registerViewModel.onPinCodeChanged1(it, context = context)
                registerViewModel.updatePinCodeError1(null)
            },
            error = pincodeError1
        )
        InputField(
            inputText = pincodeResponse?.cities?.get(0), hint = stringResource(id = R.string.city),
            start = 5.dp, end = 0.dp, top = 10.dp, enable = false,
            readOnly = city1?.isEmpty() == true,
            modifier = Modifier
                .weight(1f)
                .focusRequester(cityFocus),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onNext = { addressFocus.requestFocus() }),
            onValueChange = { registerViewModel.onCityChanged(it) },
            error = cityError1,
        )
    }
}

@Composable
fun CompanyUdyamFeilds(
    companyName: String?, udyamNumber: String?, registerViewModel: RegisterViewModel,
    udyamNumberFocus: FocusRequester, companyNameFocus: FocusRequester, udyamError: String?,
    companyNameError: String?, pincodeFocus: FocusRequester
) {
    companyName?.let { companyName ->
        InputField(
            inputText = companyName, hint = stringResource(id = R.string.company_name), top = 10.dp,
            modifier = Modifier.focusRequester(companyNameFocus),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            error = companyNameError,
            keyboardActions = KeyboardActions(onNext = { udyamNumberFocus.requestFocus() }),
            onValueChange = {
                registerViewModel.onCompanyNameChanged(it)
            }
        )
    }
    udyamNumber?.let { udyamNumber ->
        InputField(
            inputText = udyamNumber.uppercase(), hint = stringResource(id = R.string.udyam_number),
            top = 10.dp, modifier = Modifier.focusRequester(udyamNumberFocus),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters, imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onNext = { pincodeFocus.requestFocus() }),
            onValueChange = {
                registerViewModel.onUdyamNumberChanged(it)
                registerViewModel.updateUdyamError(null)
            },
            error = udyamError
        )
    }
}

@Composable
fun PasswordMobileFeilds(
    password: String?, mobileNumber: String?, registerViewModel: RegisterViewModel,
    focusPassword: FocusRequester, mobileNumberFocus: FocusRequester,
    mobileNumberError: String?, passwordError: String?, dobFocus: FocusRequester
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    password?.let { password ->
        InputField(
            top = 10.dp, inputText = password, hint = stringResource(id = R.string.enter_password),
            modifier = Modifier.focusRequester(focusPassword),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(onNext = { mobileNumberFocus.requestFocus() }),
            error = passwordError,
            onValueChange = {
                registerViewModel.onPasswordChanged(it)
                registerViewModel.updatePasswordError(null)
            },
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                    registerViewModel.updateGeneralError(null)
                }) {
                    Icon(imageVector = image, stringResource(id = R.string.password_icon))
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        )
    }
    mobileNumber?.let { mobileNumber ->
        InputField(
            inputText = mobileNumber,
            hint = stringResource(id = R.string.phone_number),
            top = 10.dp,
            modifier = Modifier.focusRequester(mobileNumberFocus),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Phone
            ),
            keyboardActions = KeyboardActions(onNext = { dobFocus.requestFocus() }),
            onValueChange = {
                registerViewModel.onMobileNumberChanged(it)
                registerViewModel.updateMobileNumberError(null)
            },
            error = mobileNumberError,
            leadingIcon = { Text(text = "+91") }
        )
    }
}

@Composable
fun EmailFeilds(
    emailId: String?, officialEmailId: String?, registerViewModel: RegisterViewModel,
    emailIdFocus: FocusRequester, officialEmailIdFocus: FocusRequester, emaiIdError: String?,
    officialEmaiIdError: String?, focusPassword: FocusRequester
) {
    emailId?.let { emailId ->
        InputField(
            inputText = emailId, top = 10.dp, hint = stringResource(id = R.string.email_id),
            modifier = Modifier.focusRequester(emailIdFocus),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(onNext = { officialEmailIdFocus.requestFocus() }),
            onValueChange = {
                registerViewModel.onPersonalEmailIdChanged(it)
                registerViewModel.updatePersonalEmailError(null)
            },
            error = emaiIdError
        )
    }
    officialEmailId?.let { officialEmailId ->
        InputField(
            inputText = officialEmailId, top = 10.dp,
            hint = stringResource(id = R.string.official_email_id),
            modifier = Modifier.focusRequester(officialEmailIdFocus),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(onNext = { focusPassword.requestFocus() }),
            onValueChange = {
                registerViewModel.onOfficialEmailIdChanged(it)
                registerViewModel.updateOfficialEmailError(null)
            },
            error = officialEmaiIdError
        )
    }
}

@Composable
fun NameFeilds(
    firstName: String?, lastName: String?, registerViewModel: RegisterViewModel,
    firstNameFocus: FocusRequester, lastNameFocus: FocusRequester, firstNameError: String?,
    lastNameError: String?, emailIdFocus: FocusRequester
) {
    firstName?.let { firstName ->
        InputField(
            inputText = firstName, hint = stringResource(id = R.string.first_name), top = 10.dp,
            modifier = Modifier.focusRequester(firstNameFocus),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onNext = { lastNameFocus.requestFocus() }),
            onValueChange = {
                registerViewModel.onFirstNameChanged(it)
                registerViewModel.updateFirstNameError(null)
            },
            error = firstNameError
        )
    }
    lastName?.let { lastName ->
        InputField(
            inputText = lastName, hint = stringResource(id = R.string.last_name), top = 10.dp,
            modifier = Modifier.focusRequester(lastNameFocus),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onNext = { emailIdFocus.requestFocus() }),
            onValueChange = {
                registerViewModel.onLastNameChanged(it)
                registerViewModel.updateLastNameError(null)
            },
            error = lastNameError
        )
    }
}

fun onRegisterClicked(
    genderSelectedText: String, employmentSelectedText: String, firstName: String?,
    lastName: String?, emailId: String?, officialEmailId: String?, password: String?, dob: String?,
    companyName: String?, mobileNumber: String?, checkboxValue: Boolean, panNumber: String?,
    udyamNumber: String, pincode1: String?, pincode2: String?, address1: String?,
    address2: String?, registerViewModel: RegisterViewModel, firstNameFocus: FocusRequester,
    lastNameFocus: FocusRequester, emailIdFocus: FocusRequester,
    officialEmailIdFocus: FocusRequester, focusPassword: FocusRequester, dobFocus: FocusRequester,
    companyNameFocus: FocusRequester, mobileNumberFocus: FocusRequester,
    panNumberFocus: FocusRequester, udyamNumberFocus: FocusRequester, genderFocus: FocusRequester,
    employmentFocus: FocusRequester, context: Context, pincodeFocus: FocusRequester,
    pincodeFocus1: FocusRequester, addressFocus: FocusRequester, address2Focus: FocusRequester,
    countryCode: String, income: String, pincodeResponse: PincodeModel?, role: String,
    nearCityResponse: PincodeModel?
) {
    val gender = genderSelectedText.lowercase(Locale.ROOT)
    val employmentType = employmentSelectedText.lowercase(Locale.ROOT)
    firstName?.let { firstName ->
        lastName?.let { lastName ->
            emailId?.let { emailId ->
                officialEmailId?.let { officialEmailId ->
                    password?.let { password ->
                        dob?.let { dob ->
                            gender.let { gender ->
                                employmentType.let { employmentType ->
                                    companyName?.let { companyName ->
                                        mobileNumber?.let { mobileNumber ->
                                            checkboxValue.let { checkboxValue ->
                                                panNumber?.let { panNumber ->
//                                                    udyamNumber?.let { udyamNumber ->
                                                    pincode1?.let { pincode1 ->
                                                        pincode2?.let { pincode2 ->
                                                            address1?.let { address1 ->
                                                                address2?.let { address2 ->
                                                                    registerViewModel.otpButtonValidation(
                                                                        firstName = firstName,
                                                                        lastName = lastName,
                                                                        emailId = emailId,
                                                                        officialEmailId = officialEmailId,
                                                                        password = password,
                                                                        mobileNumber = mobileNumber,
                                                                        dob = dob,
                                                                        gender = gender,
                                                                        employmentType = employmentType,
                                                                        companyName = companyName,
                                                                        panNumber = panNumber.uppercase(),
                                                                        checkboxValue = checkboxValue,
                                                                        firstNameFocus = firstNameFocus,
                                                                        lastNameFocus = lastNameFocus,
                                                                        emailIdFocus = emailIdFocus,
                                                                        officialEmailIdFocus = officialEmailIdFocus,
                                                                        focusPassword = focusPassword,
                                                                        mobileNumberFocus = mobileNumberFocus,
                                                                        dobFocus = dobFocus,
                                                                        genderFocus = genderFocus,
                                                                        employmentFocus = employmentFocus,
                                                                        companyNameFocus = companyNameFocus,
                                                                        panNumberFocus = panNumberFocus,
                                                                        context = context,
//                                                                            udyamNumber = udyamNumber?.uppercase() ?: "",
                                                                        udyamNumber = udyamNumber?.takeIf { it.isNotEmpty() }?.uppercase() ?: "",
                                                                        udyamNumberFocus = udyamNumberFocus,
                                                                        pinCode = pincode1,
                                                                        pinCodeFocus = pincodeFocus,
                                                                        pinCode1 = pincode2,
                                                                        pincodeFocus1 = pincodeFocus1,
                                                                        address = address1,
                                                                        adressFocus = addressFocus,
                                                                        address2 = address2,
                                                                        adress2Focus = address2Focus,
                                                                        profile = Profile(
                                                                            firstName = firstName,
                                                                            lastName = lastName,
                                                                            password = password,
                                                                            dob = dob,
                                                                            mobileNumber = mobileNumber,
                                                                            countryCode = countryCode,
                                                                            panNumber = panNumber.uppercase(),
                                                                            email = emailId,
                                                                            income = income,
                                                                            pincode1 = pincode1,
                                                                            pincode2 = pincode2,
                                                                            role = role,
                                                                            gender = gender,
                                                                            if (employmentType.equals(
                                                                                    "salaried"
                                                                                )
                                                                            ) employmentType else "selfEmployed",
                                                                            address2 = address2,
                                                                            city1 = pincodeResponse?.cities?.get(0)
                                                                                ?: "",
                                                                            city2 = nearCityResponse?.cities?.get(0)
                                                                                ?: "",
                                                                            address1 = address1,
                                                                            companyName = companyName,
                                                                            udyamNumber = udyamNumber?.takeIf { it.isNotEmpty() }?.uppercase(),
                                                                            state1 = pincodeResponse?.state
                                                                                ?: "",
                                                                            state2 = nearCityResponse?.state
                                                                                ?: "",
                                                                            officialEmail = officialEmailId
                                                                        )
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
//                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun onRegisteredSuccessful(
    successResponse: Signup?, navController: NavHostController, mobileNumber: String?
) {
    successResponse?.let { response ->
        response.data?.let { data ->
            val orderId = data.orderId
            mobileNumber?.let { mobileNumber ->
                orderId?.let { orderId ->
                    navigateToOtpScreen(
                        navController, mobileNumber, orderId, "1"
                    )
                }
            }
        }
    }
}

@Composable
fun DatePickerFeild(
    dob: String, dobFocus: FocusRequester, genderFocus: FocusRequester, context: Context,
    registerViewModel: RegisterViewModel, dobError: String,
) {
    //For ABFL & BFL
//    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val (selectedDate, setSelectedDate) = remember { mutableStateOf(dob) }

    //To check person's age should be greater than 18
    val maxDate = remember {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -18)
        calendar.timeInMillis
    }


    // Initialize Calendar instance
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            val newDate = dateFormat.format(calendar.time)
            setSelectedDate(newDate)
            registerViewModel.onDobChanged(newDate)  // Update ViewModel with the new date
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
//        datePicker.maxDate = System.currentTimeMillis()  // Limit the date picker to current date
        datePicker.maxDate = maxDate  // Limit the date picker to current date -18 years
    }

    InputField(
        inputText = dob,
        hint = stringResource(id = R.string.dob),
        top = 10.dp,
        modifier = Modifier
            .clickable { datePickerDialog.show() }
            .focusRequester(dobFocus),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),
        readOnly = true,
        keyboardActions = KeyboardActions(onNext = { genderFocus.requestFocus() }),
        onValueChange = { registerViewModel.onDobChanged(it) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = stringResource(id = R.string.select_date),
                modifier = Modifier.clickable { datePickerDialog.show() }
            )
        },
        error = dobError
    )
}


@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navController = NavHostController(LocalContext.current))
}