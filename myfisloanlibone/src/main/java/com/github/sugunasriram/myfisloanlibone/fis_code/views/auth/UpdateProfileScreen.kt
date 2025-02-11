package com.github.sugunasriram.myfisloanlibone.fis_code.views.auth

import android.app.DatePickerDialog
import android.content.Context
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
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InputField
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
fun UpdateProfileScreen(navController: NavHostController) {

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
    val address1: String? by registerViewModel.address1.observeAsState("")
    val address2: String? by registerViewModel.address2.observeAsState("")
    val city1: String? by registerViewModel.city1.observeAsState("")
    val pincode1: String? by registerViewModel.pinCode1.observeAsState("")
    val gender: String? by registerViewModel.gender.observeAsState("")
    val city2: String? by registerViewModel.city2.observeAsState("")
    val state1: String? by registerViewModel.state1.observeAsState("")
    val state2: String? by registerViewModel.state2.observeAsState("")
    val pincode2: String? by registerViewModel.pinCode2.observeAsState("")
    val employmentType: String? by registerViewModel.employment.observeAsState("")
    val showInternetScreen by registerViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by registerViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by registerViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by registerViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by registerViewModel.unAuthorizedUser.observeAsState(false)


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
    val (personalEmailIdFocus) = FocusRequester.createRefs()
    val (officialEmailIdFocus) = FocusRequester.createRefs()
    val (mobileNumberFocus) = FocusRequester.createRefs()
    val (dobFocus) = FocusRequester.createRefs()
    val (panNumberFocus) = FocusRequester.createRefs()
    val (companyNameFocus) = FocusRequester.createRefs()
    val (udyamNumberFocus) = FocusRequester.createRefs()
    val (addressFocus) = FocusRequester.createRefs()
    val (cityFocus) = FocusRequester.createRefs()
    val (pincodeFocus) = FocusRequester.createRefs()
    val (addressFocus1) = FocusRequester.createRefs()
    val (cityFocus1) = FocusRequester.createRefs()
    val (pincodeFocus1) = FocusRequester.createRefs()
    val tapfocusManager = LocalFocusManager.current

    val firstNameError: String? by registerViewModel.firstNameError.observeAsState("")
    val lastNameError: String? by registerViewModel.lastNameError.observeAsState("")
    val personalEmailIdError: String? by registerViewModel.personalEmailIdError.observeAsState("")
    val officialEmailIdError: String? by registerViewModel.officialEmailIdError.observeAsState("")
    val mobileNumberError: String? by registerViewModel.mobileNumberError.observeAsState("")
    val dobError: String? by registerViewModel.dobError.observeAsState("")
    val addressError: String? by registerViewModel.addressError.observeAsState("")
    val cityError1: String? by registerViewModel.cityError1.observeAsState("")
    val pincodeError1: String? by registerViewModel.pinCodeError1.observeAsState("")
    val panError: String? by registerViewModel.panError.observeAsState("")
    val udyamerror: String? by registerViewModel.udyamError.observeAsState("")
    val generalError: String? by registerViewModel.generalError.observeAsState("")
    val address2Error: String? by registerViewModel.address2Error.observeAsState("")
    val cityError2: String? by registerViewModel.cityError2.observeAsState("")
    val pincodeError2: String? by registerViewModel.pinCodeError2.observeAsState("")

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
                            inputText = firstName ?: "", hint = stringResource(id = R.string.first_name),
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
                                    modifier = Modifier.clickable {
                                        firstNameFocus.requestFocus()
                                    }.size(20.dp)
                                )
                            },
                            keyboardActions = KeyboardActions(onNext = { lastNameFocus.requestFocus() }),
                            onValueChange = {
                                registerViewModel.onFirstNameChanged(it)
                                registerViewModel.updateFirstNameError(null)
                            }, error = firstNameError
                        )
                        InputField(
                            inputText = lastName, hint = stringResource(id = R.string.last_name),
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
                                    modifier = Modifier.clickable {
                                        lastNameFocus.requestFocus()
                                    }.size(20.dp)
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
                            modifier = Modifier
                                .focusRequester(personalEmailIdFocus),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
                            ),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = stringResource(id = R.string.edit_icon),
                                    modifier = Modifier.clickable {
                                        personalEmailIdFocus.requestFocus()
                                    }.size(20.dp)
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
                                    modifier = Modifier.clickable {
                                        officialEmailIdFocus.requestFocus()
                                    }.size(20.dp)
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
                            UpdateDatePickerFeild(
                                dob = dob ?: "", dobFocus = dobFocus, dobError = it,
                                panNumberFocus = panNumberFocus, context = context,
                                registerViewModel = registerViewModel
                            )
                        }
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
                            keyboardActions = KeyboardActions(onNext = { companyNameFocus.requestFocus() }),
                            onValueChange = {
                                registerViewModel.onPanNumberChanged(it)
                                registerViewModel.updatePanError(null)
                            },
                        )
                        InputField(inputText = companyName,
                            hint = stringResource(id = R.string.company_name), top = 10.dp,
                            modifier = Modifier.focusRequester(companyNameFocus),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
                            ),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = stringResource(id = R.string.edit_icon),
                                    modifier = Modifier.clickable {
                                        companyNameFocus.requestFocus()
                                    }.size(20.dp)
                                )
                            },
                            keyboardActions = KeyboardActions(onNext = { udyamNumberFocus.requestFocus() }),
                            onValueChange = {
                                registerViewModel.onCompanyNameChanged(it)
                            }
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
                                    modifier = Modifier.clickable {
                                        udyamNumberFocus.requestFocus()
                                    }.size(20.dp)
                                )
                            },
                            keyboardActions = KeyboardActions(onNext = { addressFocus.requestFocus() }),
                            onValueChange = {
                                registerViewModel.onUdyamNumberChanged(it)
                                registerViewModel.updateUdyamError(null)
                            },
                            error = udyamerror
                        )
                        cityError1?.let { cityError1 ->
                            pincodeError1?.let { pincodeError1 ->
                                city1?.let { city1 ->
                                    pincode1?.let { pincode1 ->
                                        UpdateCityAndPincodeFeilds(
                                            city1 = city1, pincode1 = pincode1,
                                            cityFocus = cityFocus, pincodeFocus = pincodeFocus,
                                            registerViewModel = registerViewModel,
                                            cityError1 = cityError1,
                                            pincodeError1 = pincodeError1,
                                            addressFocus = addressFocus, context = context
                                        )
                                    }
                                }
                            }
                        }
                        addressError?.let { addressError ->
                            address1?.let { address1 ->
                                UpdateAddressFeild(
                                    address = address1, addressError = addressError,
                                    addressFocus = addressFocus, pincodeFocus1 = pincodeFocus1,
                                    registerViewModel = registerViewModel,
                                )
                            }
                        }
                        cityError2?.let { cityError2 ->
                            pincodeError2?.let { pincodeError2 ->
                                city2?.let { city2 ->
                                    pincode2?.let { pincode2 ->
                                        UpdateCityAndPincodeFeilds2(
                                            city2 = city2, pincode2 = pincode2,
                                            cityFocus1 = cityFocus1, cityError2 = cityError2,
                                            pincodeFocus = pincodeFocus1,
                                            registerViewModel = registerViewModel,
                                            pincodeError2 = pincodeError2,
                                            addressFocus1 = addressFocus1, context = context
                                        )
                                    }
                                }
                            }
                        }
                        address2Error?.let { address2Error ->
                            address2?.let { address2 ->
                                UpdateAddressFeild2(
                                    address2 = address2, addressError = address2Error,
                                    addressFocus = addressFocus1,
                                    registerViewModel = registerViewModel
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

                                val employmentTypeSmallCase =
                                    employmentType?.lowercase(Locale.ROOT)

                                registerViewModel.updateValidation(
                                    navController = navController, checkboxValue = checkboxValue,
                                    firstNameFocus = firstNameFocus, lastNameFocus = lastNameFocus,
                                    personalEmailIdFocus = personalEmailIdFocus, context = context,
                                    officialEmailIdFocus = officialEmailIdFocus,
                                    panNumberFocus = panNumberFocus,
                                    udyamNumberFocus = udyamNumberFocus,
                                    profile = Profile(
                                        firstName = firstName, lastName = lastName, dob = dob,
                                        panNumber = panNumber?.uppercase(), income = income,
//                                        udyamNumber = udyamNumber?.uppercase(),
                                        udyamNumber = udyamNumber?.takeIf { it.isNotEmpty() }?.uppercase(),
                                        email = personalEmailId, address1 = address1,
                                        address2 = address2, pincode1 = pincode1,
                                        pincode2 = pincode2, gender = gender,
                                        employmentType = if (employmentTypeSmallCase.equals(
                                                "salaried"
                                            )
                                        ) employmentTypeSmallCase else "selfEmployed",
                                        city1 = city1, city2 = city2, state1 = state1,
                                        companyName = companyName, state2 = state2,
                                        officialEmail = officialEmailId,
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
                    .clickable { navController.popBackStack() }
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
            )
        }
    }
}

@Composable
fun UpdateCityAndPincodeFeilds(
    city1: String?, pincode1: String?, cityFocus: FocusRequester, pincodeFocus: FocusRequester,
    registerViewModel: RegisterViewModel, cityError1: String, pincodeError1: String,
    addressFocus: FocusRequester,context: Context
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        InputField(
            inputText = pincode1,
            hint = stringResource(id = R.string.pincode),
            start = 0.dp,
            end = 0.dp,
            top = 10.dp,
            error = pincodeError1,
            modifier = Modifier
                .weight(1f)
                .focusRequester(pincodeFocus),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onNext = { cityFocus.requestFocus() }),
            onValueChange = { registerViewModel.onPinCodeChanged(it,context) },
        )
        InputField(
            inputText = city1, hint = stringResource(id = R.string.city), start = 5.dp, end = 0.dp,
            top = 10.dp, enable = false, readOnly = !city1.isNullOrEmpty(), error = cityError1,
            modifier = Modifier
                .weight(1f)
                .focusRequester(cityFocus),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onNext = { addressFocus.requestFocus() }),
            onValueChange = { registerViewModel.onCityChanged(it) },
        )
    }
}

@Composable
fun UpdateAddressFeild(
    address: String?, addressError: String, addressFocus: FocusRequester,
    registerViewModel: RegisterViewModel, pincodeFocus1: FocusRequester
) {
    InputField(
        inputText = address, hint = stringResource(id = R.string.address_one), top = 10.dp,
        modifier = Modifier.focusRequester(addressFocus), error = addressError,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onNext = { pincodeFocus1.requestFocus() }),
        onValueChange = { registerViewModel.onAddressChanged(it) },
    )
}

@Composable
fun UpdateAddressFeild2(
    address2: String?, addressError: String, addressFocus: FocusRequester,
    registerViewModel: RegisterViewModel,
) {
    InputField(
        inputText = address2, hint = stringResource(id = R.string.address_two), top = 10.dp,
        modifier = Modifier.focusRequester(addressFocus),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done, keyboardType = KeyboardType.Text
        ),
        onValueChange = { registerViewModel.onAddressTwoChanged(it) },
        error = addressError
    )
}

@Composable
fun UpdateCityAndPincodeFeilds2(
    city2: String?, pincode2: String?, cityFocus1: FocusRequester, pincodeFocus: FocusRequester,
    registerViewModel: RegisterViewModel, cityError2: String, pincodeError2: String,
    addressFocus1: FocusRequester,context: Context
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        InputField(
            inputText = pincode2,
            hint = stringResource(id = R.string.pincode),
            start = 0.dp,
            end = 0.dp,
            top = 10.dp,
            error = pincodeError2,
            modifier = Modifier
                .weight(1f)
                .focusRequester(pincodeFocus),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onNext = { cityFocus1.requestFocus() }),
            onValueChange = {
                registerViewModel.onPinCodeChanged1(it, context = context)
            },
        )

        InputField(
            inputText = city2, hint = stringResource(id = R.string.city), start = 5.dp, end = 0.dp,
            top = 10.dp, enable = false, readOnly = !city2.isNullOrEmpty(), error = cityError2,
            modifier = Modifier
                .weight(1f)
                .focusRequester(cityFocus1),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onNext = { addressFocus1.requestFocus() }),
            onValueChange = { registerViewModel.onCityChanged1(it) },
        )
    }
}

@Composable
fun UpdateDatePickerFeild(
    dob: String, dobFocus: FocusRequester, panNumberFocus: FocusRequester, dobError: String,
    registerViewModel: RegisterViewModel, context: Context,
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
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
        keyboardActions = KeyboardActions(onNext = { panNumberFocus.requestFocus() }),
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