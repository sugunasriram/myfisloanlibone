package com.github.sugunasriram.myfisloanlibone.fis_code.views.auth

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CheckBoxText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.DropDown
import com.github.sugunasriram.myfisloanlibone.fis_code.components.DropDownTextField
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InputField
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.AppScreens
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.Profile
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.disableColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CustomSnackBar
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.showSnackError
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.RegisterViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpdateProfileScreen(navController: NavHostController,fromFlow:String) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val income = "500000"

    val snackState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val registerViewModel: RegisterViewModel = viewModel()
    val firstName: String? by registerViewModel.firstName.observeAsState("")
    val lastName: String? by registerViewModel.lastName.observeAsState("")
    val personalEmailId: String? by registerViewModel.personalEmailId.observeAsState("")
    val officialEmailId: String? by registerViewModel.officialEmailId.observeAsState("")
    val companyName: String? by registerViewModel.companyName.observeAsState("")
    val mobileNumber: String? by registerViewModel.mobileNumber.observeAsState("")
    val dob: String? by registerViewModel.dob.observeAsState("")
    val udyamNumber: String? by registerViewModel.udyamNumber.observeAsState("")
    val panNumber: String? by registerViewModel.panNumber.observeAsState("")
    val checkboxValue: Boolean by registerViewModel.checkBox.observeAsState(false)
    val officialAddress: String? by registerViewModel.officialAddress.observeAsState("")
    val permanentAddress: String? by registerViewModel.permanentAddress.observeAsState("")
    val city1: String? by registerViewModel.city1.observeAsState("")
    val pinCode1: String? by registerViewModel.pinCode1.observeAsState("")
    val gender: String? by registerViewModel.gender.observeAsState("")
    val city2: String? by registerViewModel.city2.observeAsState("")
    val state1: String? by registerViewModel.state1.observeAsState("")
    val state2: String? by registerViewModel.state2.observeAsState("")
    val pinCode2: String? by registerViewModel.pinCode2.observeAsState("")
    val employmentType: String? by registerViewModel.employment.observeAsState("")
    val showInternetScreen by registerViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by registerViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by registerViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by registerViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by registerViewModel.unAuthorizedUser.observeAsState(false)
    val companyNameError: String? by registerViewModel.companyNameError.observeAsState("")


    val inProgress by registerViewModel.inProgress.collectAsState()
    val isCompleted by registerViewModel.isCompleted.collectAsState()
    val isUpdating by registerViewModel.isUpdating.collectAsState()
    val upDated by registerViewModel.upDated.collectAsState()
    val shownMsg by registerViewModel.shownMsg.collectAsState()
    val shouldShowKeyboard by registerViewModel.shouldShowKeyboard.observeAsState(false)

    LaunchedEffect(shouldShowKeyboard) {
        if (shouldShowKeyboard) {
            keyboardController?.show()
            registerViewModel.resetKeyboardRequest()
        }
    }

    val (firstNameFocus) = FocusRequester.createRefs()
    val (lastNameFocus) = FocusRequester.createRefs()
    val (employmentFocus) = FocusRequester.createRefs()
    val (personalEmailIdFocus) = FocusRequester.createRefs()
    val (officialEmailIdFocus) = FocusRequester.createRefs()
    val (mobileNumberFocus) = FocusRequester.createRefs()
    val (dobFocus) = FocusRequester.createRefs()
    val (panNumberFocus) = FocusRequester.createRefs()
    val (companyNameFocus) = FocusRequester.createRefs()
    val (udyamNumberFocus) = FocusRequester.createRefs()
    val (officialAddressFocus) = FocusRequester.createRefs()
    val (cityFocus) = FocusRequester.createRefs()
    val (pinCodeFocus1) = FocusRequester.createRefs()
    val (permanentAddressFocus) = FocusRequester.createRefs()
    val (cityFocus1) = FocusRequester.createRefs()
    val (pinCodeFocus2) = FocusRequester.createRefs()
    val (genderFocus) = FocusRequester.createRefs()
    val tapfocusManager = LocalFocusManager.current

    val firstNameError: String? by registerViewModel.firstNameError.observeAsState("")
    val lastNameError: String? by registerViewModel.lastNameError.observeAsState("")
    val personalEmailIdError: String? by registerViewModel.personalEmailIdError.observeAsState("")
    val officialEmailIdError: String? by registerViewModel.officialEmailIdError.observeAsState("")
    val mobileNumberError: String? by registerViewModel.mobileNumberError.observeAsState("")
    val dobError: String? by registerViewModel.dobError.observeAsState("")
    val addressError: String? by registerViewModel.officialAddressError.observeAsState("")
    val cityError1: String? by registerViewModel.cityError1.observeAsState("")
    val pinCodeError1: String? by registerViewModel.pinCodeError1.observeAsState("")
    val panError: String? by registerViewModel.panError.observeAsState("")
    val udyamerror: String? by registerViewModel.udyamError.observeAsState("")
    val generalError: String? by registerViewModel.generalError.observeAsState("")
    val address2Error: String? by registerViewModel.permanentAddressError.observeAsState("")
    val cityError2: String? by registerViewModel.cityError2.observeAsState("")
    val pinCodeError2: String? by registerViewModel.pinCodeError2.observeAsState("")
    val genderError: String? by registerViewModel.genderError.observeAsState("")
    val employmentError: String? by registerViewModel.employmentError.observeAsState("")

    var genderSelectedText : String = gender ?: ""
    var selectedCity1 by remember { mutableStateOf(city1 ?: "") }
    val onCity1Selected: (String) -> Unit = { selectedText ->
        selectedCity1 = selectedText
    }
    var selectedCity2 by remember { mutableStateOf(city2 ?: "") }
    val onCity2Selected: (String) -> Unit = { selectedText ->
        selectedCity2 = selectedText
    }

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

    var employmentSelectedText : String =  employmentType ?: ""
    var employement by remember { mutableStateOf(false) }
    val emplyTypeList = listOf(
        stringResource(id = R.string.salaried), stringResource(id = R.string.self_employment)
    )
    val onEmploymentSelected: (String) -> Unit = { selectedText ->
        employmentSelectedText = selectedText
        registerViewModel.onEmploymentChanged(selectedText)

    }
    val onEmploymentDismiss: () -> Unit = { employement = false }


    if (!showInternetScreen && !showTimeOutScreen && !showServerIssueScreen && !unexpectedErrorScreen) {
        if (inProgress) {
            CenterProgress()
        } else {
            if (!isCompleted) {
                registerViewModel.getUserDetail(context, navController)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { tapfocusManager.clearFocus() })
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    )
                    {
                        TopSection(navController)
                        InputField(
                            inputText = firstName ?: "",
                            hint = stringResource(id = R.string.first_name),
                            top = 10.dp,
                            modifier = Modifier.focusRequester(firstNameFocus),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
                            ),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = stringResource(id = R.string.edit_icon),
                                    modifier = Modifier
                                        .clickable { firstNameFocus.requestFocus() }
                                        .size(20.dp)
                                )
                            },
                            keyboardActions = KeyboardActions(onNext = { lastNameFocus.requestFocus() }),
                            onValueChange = {
                                registerViewModel.onFirstNameChanged(it)
                                registerViewModel.updateFirstNameError(null)
                            },
                            error = firstNameError
                        )
                        InputField(
                            inputText = lastName?:"", hint = stringResource(id = R.string.last_name),
                            top = 10.dp,
                            modifier = Modifier.focusRequester(lastNameFocus),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
                            ),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = stringResource(id = R.string.edit_icon),
                                    modifier = Modifier
                                        .clickable { lastNameFocus.requestFocus() }
                                        .size(20.dp)
                                )
                            },
                            keyboardActions = KeyboardActions(onNext = { personalEmailIdFocus.requestFocus() }),
                            onValueChange = {
                                registerViewModel.onLastNameChanged(it)
                                registerViewModel.updateLastNameError(null)
                            },
                            error = lastNameError
                        )
                        InputField(
                            inputText = personalEmailId, top = 10.dp, error = personalEmailIdError,
                            hint = stringResource(id = R.string.email_id),
                            modifier = Modifier.focusRequester(personalEmailIdFocus),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
                            ),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = stringResource(id = R.string.edit_icon),
                                    modifier = Modifier
                                        .clickable { personalEmailIdFocus.requestFocus() }
                                        .size(20.dp)
                                )
                            },
                            keyboardActions = KeyboardActions(onNext = { officialEmailIdFocus.requestFocus() }),
                            onValueChange = {
                                registerViewModel.onPersonalEmailIdChanged(it)
                                registerViewModel.updatePersonalEmailError(null)
                            }
                        )
                        InputField(
                            inputText = officialEmailId, top = 10.dp, error = officialEmailIdError,
                            hint = stringResource(id = R.string.official_email_id),
                            modifier = Modifier
                                .focusRequester(officialEmailIdFocus),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
                            ),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = stringResource(id = R.string.edit_icon),
                                    modifier = Modifier
                                        .clickable { officialEmailIdFocus.requestFocus() }
                                        .size(20.dp)
                                )
                            },
                            keyboardActions = KeyboardActions(onNext = { dobFocus.requestFocus() }),
                            onValueChange = {
                                registerViewModel.onOfficialEmailIdChanged(it)
                                registerViewModel.updateOfficialEmailError(null)
                            },
                        )
                        InputField(
                            inputText = mobileNumber,
                            hint = stringResource(id = R.string.phone_number),
                            top = 10.dp,
                            enable = false,
                            readOnly = true,
                            modifier = Modifier.focusRequester(mobileNumberFocus),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next, keyboardType = KeyboardType.Phone
                            ),
                            keyboardActions = KeyboardActions(onNext = { dobFocus.requestFocus() }),
                            onValueChange = {},
                            error = mobileNumberError,
                            leadingIcon = { Text(text = "+91") }
                        )
                        dobError?.let {
                            UpdateDatePickerField(
                                dob = dob ?: "", dobFocus = dobFocus, dobError = it,
                                nextFocus = genderFocus, context = context,
                                registerViewModel = registerViewModel
                            )
                        }
                        DropDownTextField(
                            start = 40.dp,
                            end = 40.dp,
                            top = 10.dp,
                            selectedText = gender ?: "",
                            hint = stringResource(id = R.string.gender),
                            focus = genderFocus,
                            onNextFocus = panNumberFocus,
                            expand = genderExpand,
                            error = genderError,
                            setExpand = { genderExpand = it },
                            itemList = genderList,
                            onDismiss = onGenderDismiss,
                            onItemSelected = onGenderSelected
                        )
                        InputField(
                            inputText = panNumber?.uppercase(),
                            hint = stringResource(id = R.string.pan),
                            top = 10.dp,
                            error = panError,
                            modifier = Modifier.focusRequester(panNumberFocus),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Characters,
                                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
                            ),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = stringResource(id = R.string.edit_icon),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            keyboardActions = KeyboardActions(onNext = { employmentFocus.requestFocus() }),
                            onValueChange = {
                                registerViewModel.onPanNumberChanged(it)
                                registerViewModel.updatePanError(null)
                            },
                        )
                        DropDownTextField(
                            start = 40.dp,
                            end = 40.dp,
                            top = 10.dp,
                            selectedText = employmentType ?: "",
                            hint = stringResource(id = R.string.employment_type_mandatory),
                            expand = employement,
                            focus = employmentFocus,
                            onNextFocus = companyNameFocus,
                            setExpand = { employement = it },
                            itemList = emplyTypeList,
                            onDismiss = onEmploymentDismiss,
                            error = employmentError,
                            onItemSelected = onEmploymentSelected
                        )
                        InputField(inputText = companyName,
                            hint = stringResource(id = R.string.company_name), top = 10.dp,
                            modifier = Modifier.focusRequester(companyNameFocus),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
                            ),
                            error = companyNameError,
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = stringResource(id = R.string.edit_icon),
                                    modifier = Modifier
                                        .clickable { companyNameFocus.requestFocus() }
                                        .size(20.dp)
                                )
                            },
                            keyboardActions = KeyboardActions(onNext = { udyamNumberFocus.requestFocus() }),
                            onValueChange = { registerViewModel.onCompanyNameChanged(it) }
                        )
                        InputField(
                            inputText = udyamNumber,
                            hint = stringResource(id = R.string.udyam_number),
                            top = 10.dp,
                            modifier = Modifier.focusRequester(udyamNumberFocus),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Characters,
                                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
                            ),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = stringResource(id = R.string.edit_icon),
                                    modifier = Modifier
                                        .clickable { udyamNumberFocus.requestFocus() }
                                        .size(20.dp))
                            },
                            keyboardActions = KeyboardActions(onNext = { pinCodeFocus1.requestFocus() }),
                            onValueChange = {
                                registerViewModel.onUdyamNumberChanged(it)
                                registerViewModel.updateUdyamError(null)
                            },
                            error = udyamerror
                        )
                        addressError?.let { addressError ->
                            UpdateAddressField(
                                address = officialAddress ?: "", addressError = addressError,
                                addressFocus = officialAddressFocus, pincodeFocus1 = pinCodeFocus2,
                                registerViewModel = registerViewModel,
                            )
                        }
                        cityError1?.let { cityError1 ->
                            pinCodeError1?.let { pinCodeError1 ->
                                UpdateCityAndPinCodeFields(id="1",
                                    selectedCity = selectedCity1.ifEmpty { city1 ?: "" },
                                    pinCode = pinCode1 ?: "",
                                    cityFocus = cityFocus,
                                    pinCodeFocus = pinCodeFocus1,
                                    registerViewModel = registerViewModel,
                                    cityError = cityError1,
                                    pinCodeError = pinCodeError1,
                                    nextFocus = officialAddressFocus,
                                    context = context,
                                    onValueChanged = {
                                        registerViewModel.onPinCodeChanged1(it, context)
                                        registerViewModel.updatePinCodeError1("")
                                    },
                                    onItemSelected = onCity1Selected
                                )
                            }
                        }
                        address2Error?.let { address2Error ->
                            UpdateAddressField2(
                                address2 = permanentAddress ?: "", addressError = address2Error,
                                addressFocus = permanentAddressFocus,
                                registerViewModel = registerViewModel
                            )
                        }
                        cityError2?.let { cityError2 ->
                            pinCodeError2?.let { pinCodeError2 ->
                                UpdateCityAndPinCodeFields(id="2",
                                    selectedCity = selectedCity2.ifEmpty { city2 ?: "" },
                                    pinCode = pinCode2 ?: "",
                                    cityFocus = cityFocus1,
                                    pinCodeFocus = pinCodeFocus2,
                                    registerViewModel = registerViewModel,
                                    cityError = cityError2,
                                    pinCodeError = pinCodeError2,
                                    nextFocus = permanentAddressFocus,
                                    context = context,
                                    onValueChanged = {
                                        registerViewModel.onPinCodeChanged2(it, context)
                                        registerViewModel.updatePinCodeError2("")
                                    },
                                    onItemSelected = onCity2Selected
                                )
                            }
                        }

                        if (!generalError.isNullOrEmpty()) {
                            showSnackError(coroutineScope, snackState, generalError)
                            SnackbarHost(hostState = snackState) {
                                generalError?.let {
                                    CustomSnackBar(message = it, containerColor = Color.Red)
                                }
                            }
                        }
                    }

                    CheckBoxText(
                        boxState = checkboxValue,
                        text = stringResource(id = R.string.register_check),
                        onCheckedChange = { registerViewModel.onCheckChanged(it) }
                    )
                    if (isUpdating) {
                        CenterProgress()
                    } else {
                        if (upDated && !shownMsg) {
                            CommonMethods().toastMessage(
                                context = context,
                                toastMsg = stringResource(id = R.string.profile_updated)
                            )
                            registerViewModel.updateShownMsg(true)
                            navController.popBackStack()
                        } else {
                            CurvedPrimaryButtonFull(
                                text = stringResource(id = R.string.save),
                                backgroundColor = if (checkboxValue) appBlue else disableColor,
                                modifier = Modifier.padding(
                                    start = 30.dp, end = 30.dp, bottom = 20.dp
                                )
                            ) {
                                val employmentTypeSmallCase = employmentType?.lowercase(Locale.ROOT)
                                registerViewModel.updateValidation(
                                    navController = navController,
                                    checkboxValue = checkboxValue,
                                    firstNameFocus = firstNameFocus,
                                    lastNameFocus = lastNameFocus,
                                    personalEmailIdFocus = personalEmailIdFocus,
                                    officialEmailIdFocus = officialEmailIdFocus,
                                    dobFocus = dobFocus,
                                    genderFocus = genderFocus,
                                    panNumberFocus = panNumberFocus,
                                    employmentTypeFocus = employmentFocus,
                                    companyNameFocus = companyNameFocus,
                                    udyamNumberFocus = udyamNumberFocus,
                                    pinCodeFocus1 = pinCodeFocus1,
                                    pinCodeFocus2 = pinCodeFocus2,
                                    permanentAddressFocus = permanentAddressFocus,
                                    officialAddressFocus = officialAddressFocus,
                                    context = context, isGST = (fromFlow =="Invoice Loan"),
                                    profile = Profile(
                                        firstName = firstName?.trim(),
                                        lastName = lastName?.trim(),
                                        dob = dob,
                                        panNumber = panNumber?.uppercase(),
                                        income = income?.trim(),
                                        udyamNumber = udyamNumber?.takeIf { it.isNotEmpty() }
                                            ?.uppercase(),
                                        email = personalEmailId?.trim(),
                                        address1 = officialAddress?.trim(),
                                        address2 = permanentAddress?.trim(),
                                        pincode1 = pinCode1,
                                        pincode2 = pinCode2,
                                        gender = genderSelectedText.lowercase(Locale.ROOT).ifEmpty { gender ?: "" },
                                        employmentType = if (employmentTypeSmallCase.equals("selfEmployed",true)) "selfEmployed" else employmentTypeSmallCase,
                                        city1 = selectedCity1.ifEmpty { city1 ?: "" },
                                        city2 = selectedCity2.ifEmpty { city2 ?: "" },
                                        state1 = state1,
                                        companyName = companyName?.trim(),
                                        state2 = state2,
                                        officialEmail = officialEmailId?.trim(),
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        CommonMethods().HandleErrorScreens(
            navController = navController, showInternetScreen = showInternetScreen,
            showTimeOutScreen = showTimeOutScreen, showServerIssueScreen = showServerIssueScreen,
            unexpectedErrorScreen = unexpectedErrorScreen, unAuthorizedUser = unAuthorizedUser
        )
    }
}


@Composable
fun TopSection(navController: NavHostController) {
    Box(contentAlignment = Alignment.TopStart) {
        Image(
            painter = painterResource(id = R.drawable.update_profile_image),
            contentDescription = stringResource(id = R.string.profile_image_icon),
            modifier = Modifier.fillMaxSize()
        )
        Box(
            contentAlignment = Alignment.TopCenter, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 200.dp)
        ) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.edit_profile_image),
                    contentDescription = stringResource(id = R.string.profile_image_icon),
                )
                Text(
                    text = stringResource(id = R.string.my_profile), style = normal32Text700,
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp), color = appBlueTitle,
                )
            }
        }
        Box(modifier = Modifier.padding(10.dp)) {
            Image(
                painter = painterResource(id = R.drawable.back_white_icon),
                contentDescription = stringResource(id = R.string.back_icon),
                modifier = Modifier
                    .clickable { navController.popBackStack(AppScreens.ApplyBycategoryScreen.route,false) }
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
            )
        }
    }
}

@Composable
fun UpdateCityAndPinCodeFields(
    id:String,
    selectedCity: String?,
    pinCode: String?,
    cityFocus: FocusRequester,
    pinCodeFocus: FocusRequester,
    registerViewModel: RegisterViewModel,
    cityError: String,
    pinCodeError: String,
    nextFocus: FocusRequester,
    context: Context, onValueChanged: (String) -> Unit,
    onItemSelected: (String) -> Unit
) {
    var expand by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val nearCityResponse by if(id=="1") registerViewModel.pinCodeResponse.collectAsState()
    else registerViewModel.nearCityResponse.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        InputField(
            inputText = pinCode,
            hint = stringResource(id = R.string.pincode),
            start = 0.dp,
            end = 5.dp,
            top = 10.dp,
            error = pinCodeError,
            modifier = Modifier.weight(1f).focusRequester(pinCodeFocus),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onNext = { nextFocus.requestFocus() }),
            onValueChange = onValueChanged,
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .focusRequester(cityFocus)
                .clickable {
                    expand = !expand
                    if (!expand) {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                }
        ) {
            InputField(
                inputText = selectedCity,
                hint = stringResource(id = R.string.city),
                start = 0.dp,
                end = 0.dp,
                top = 10.dp,
                readOnly = true,
                error = cityError,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {},
                keyboardActions = KeyboardActions(onNext = { nextFocus.requestFocus() }),
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.down_ward_image),
                        contentDescription = stringResource(id = R.string.down_ward_image),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { expand = !expand }
                    )
                }
            )

            DropDown(
                mExpanded = expand,
                purpose = nearCityResponse?.cities ?: emptyList(),
                onItemSelected = { city ->
                    onItemSelected(city)
                    expand = false
                },
                onDismiss = { expand = false }
            )
        }
    }
}

@Composable
fun UpdateAddressField(
    address: String?, addressError: String, addressFocus: FocusRequester,
    registerViewModel: RegisterViewModel, pincodeFocus1: FocusRequester
) {
    InputField(
        inputText = address, hint = stringResource(id = R.string.official_address_with_star), top = 10.dp,
        modifier = Modifier.focusRequester(addressFocus), error = addressError,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onNext = { pincodeFocus1.requestFocus() }),
        onValueChange = {
            registerViewModel.onAddressChanged(it)
            registerViewModel.updateAddressError("")
        },
    )
}

@Composable
fun UpdateAddressField2(
    address2: String?, addressError: String, addressFocus: FocusRequester,
    registerViewModel: RegisterViewModel,
) {
    InputField(
        inputText = address2, hint = stringResource(id = R.string.permanent_address_with_star), top = 10.dp,
        modifier = Modifier.focusRequester(addressFocus),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done, keyboardType = KeyboardType.Text
        ),
        onValueChange = {
            registerViewModel.onAddressTwoChanged(it)
            registerViewModel.updateAddress2Error("")
        },
        error = addressError
    )
}


@Composable
fun UpdateDatePickerField(
    dob: String, dobFocus: FocusRequester, nextFocus: FocusRequester, dobError: String,
    registerViewModel: RegisterViewModel, context: Context,
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
        inputText = dob, hint = stringResource(id = R.string.dob), top = 10.dp, error = dobError,
        modifier = Modifier
            .clickable { datePickerDialog.show() }
            .focusRequester(dobFocus),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next, keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onNext = { nextFocus.requestFocus() }),
        onValueChange = { registerViewModel.onDobChanged(it) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = stringResource(id = R.string.select_date),
                modifier = Modifier.clickable { datePickerDialog.show() }
            )
        }
    )
}