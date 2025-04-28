package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.AgreementAnimation
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.NumberFullWidthCard
import com.github.sugunasriram.myfisloanlibone.fis_code.components.OnlyReadAbleText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToAnnualIncomeScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.Profile
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.ProfileResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.bold20Text100
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.lightishGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal12Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.softSteelGray
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.RegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReviewDetailsScreen(navController: NavHostController, purpose: String, fromFlow: String) {

    val registerViewModel: RegisterViewModel = viewModel()
    val checkboxState: Boolean by registerViewModel.checkBoxDetail.observeAsState(false)
    val showInternetScreen by registerViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by registerViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by registerViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by registerViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by registerViewModel.unAuthorizedUser.observeAsState(false)

    val inProgress by registerViewModel.inProgress.collectAsState()
    val isCompleted by registerViewModel.isCompleted.collectAsState()
    val userDetails by registerViewModel.getUserResponse.collectAsState()

    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    BackHandler {
        goBack(navController = navController, fromFlow = fromFlow)
    }

    when {
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        else -> {
            ReviewDetailView(
                scaffoldState = scaffoldState, navController = navController, context = context,
                coroutineScope = coroutineScope, inProgress = inProgress, isCompleted = isCompleted,
                userDetails = userDetails, checkboxState = checkboxState, purpose = purpose,
                registerViewModel = registerViewModel, fromFlow = fromFlow
            )
        }
    }
}

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReviewDetailView(
    scaffoldState: BottomSheetScaffoldState, navController: NavHostController,
    coroutineScope: CoroutineScope, context: Context, inProgress: Boolean, isCompleted: Boolean,
    userDetails: ProfileResponse?, checkboxState: Boolean, purpose: String, fromFlow: String,
    registerViewModel: RegisterViewModel
) {
    if (inProgress) {
        AgreementAnimation(text = "", image = R.raw.processing_please_wait)
    } else {
        if (isCompleted) {
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetContent = {
                    BottomSheetContent(
                        navController = navController, scaffoldState = scaffoldState,
                        coroutineScope = coroutineScope, purpose = purpose,
                        registerViewModel = registerViewModel, fromFlow = fromFlow
                    )
                },
                sheetPeekHeight = 0.dp
            ) {
                FixedTopBottomScreen(
                    navController = navController,
                    isSelfScrollable = false,
                    showCheckBox = true,
                    buttonText = stringResource(id = R.string.next),
                    backGroudColorChange = checkboxState,
                    checkBoxText = stringResource(id = R.string.agree_terms),
                    checkboxState = checkboxState,
                    onBackClick = {
                        goBack(navController = navController, fromFlow = fromFlow)
                    },
                    onCheckBoxChange = { isChecked ->
                        registerViewModel.onCheckBoxDetailChanged(isChecked)
                    },
                    onClick = {
                        onReviewClick(
                            checkboxState = checkboxState, navController = navController,
                            purpose = purpose, context = context, fromFlow = fromFlow
                        )
                    }
                ) {
                    PersonalDetailView(
                        userDetails = userDetails, purpose = purpose
                    )
                }
            }
        } else {
            registerViewModel.getUserDetail(context = context, navController = navController)
        }
    }
}

fun goBack(navController: NavHostController, fromFlow: String) {
    if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
        navigateToAnnualIncomeScreen(
            navController = navController, fromFlow = fromFlow
        )
    } else if (fromFlow.equals("Purchase Finance", ignoreCase = true)) {
        navigateToLoanProcessScreen(
            navController = navController, transactionId="Sugu",statusId = 17,
            responseItem = "No Need",
            offerId = "1234", fromFlow = "Purchase Finance"
        )
    }
}


@Composable
fun PersonalDetailView(
    userDetails: ProfileResponse?, purpose: String
) {
    userDetails?.let { response ->
        StartingText(
            text = stringResource(id = R.string.review_details),
            start = 40.dp, end = 30.dp, bottom = 5.dp, top = 30.dp, style = normal16Text500,
            textAlign = TextAlign.Start,
        )
        StartingText(
            text = stringResource(id = R.string.personal_detail),
            start = 40.dp, end = 30.dp, bottom = 5.dp, style = normal14Text700,
            textAlign = TextAlign.Start,
        )
        response.data?.let { profile ->
            PersonalDetailsCard(profile)
            OrderDetailText()
            EmploymentDetailsCard(profile, purpose)
        }
    }
}

@Composable
fun PersonalDetailsCard(profile: Profile) {
    NumberFullWidthCard(cardColor = lightishGray) {
        profile.firstName?.let { firstName ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.first_name), textValue = firstName,
                textColorHeader = softSteelGray, textColorValue = appBlack, start = 40.dp,
                top = 10.dp, bottom = 10.dp
            )
        }
        profile.lastName?.let { lastName ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.last_name), textValue = lastName,
                textColorHeader = softSteelGray, textColorValue = appBlack, start = 40.dp,
                top = 10.dp, bottom = 10.dp
            )
        }
        profile.dob?.let { dob ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.date_of_birth),
                textValue = dob, start = 40.dp, textColorHeader = softSteelGray,
                textColorValue = appBlack, top = 10.dp, bottom = 10.dp
            )
        }
        profile.gender?.let { gender ->
            val formattedGender = gender.replaceFirstChar { it.uppercase() }
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.gender), textValue = formattedGender, top = 10.dp,
                textColorHeader = softSteelGray, textColorValue = appBlack, start = 40.dp,
                bottom = 10.dp
            )
        }
        profile.panNumber?.let { panNumber ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.pan), textValue = panNumber, top = 10.dp,
                textColorHeader = softSteelGray, textColorValue = appBlack, start = 40.dp,
                bottom = 10.dp
            )
        }
        profile.mobileNumber?.let { mobileNumber ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.contact_number), textValue = mobileNumber,
                textColorHeader = softSteelGray, textColorValue = appBlack, start = 40.dp,
                top = 10.dp, bottom = 10.dp
            )
        }
        profile.email?.let { email ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.email_id), textValue = email, top = 10.dp,
                textColorHeader = softSteelGray, textColorValue = appBlack, start = 40.dp,
                bottom = 10.dp
            )
        }
        profile.officialEmail?.let { officialEmail ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.official_email_id),
                textValue = officialEmail, textColorHeader = softSteelGray,
                textColorValue = appBlack, start = 40.dp, top = 10.dp, bottom = 10.dp
            )
        }
    }
}

@Composable
fun EmploymentDetailsCard(profile: Profile, purpose: String) {
    NumberFullWidthCard(cardColor = lightishGray) {
        profile.employmentType?.let { employmentType ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.employment_type),
                textValue = employmentType.lowercase()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                textColorHeader = softSteelGray, textColorValue = appBlack, start = 40.dp,
                top = 10.dp, bottom = 10.dp
            )
        }
        profile.income?.let { income ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.income_all),
                textValue = income, top = 10.dp, textColorHeader = softSteelGray,
                textColorValue = appBlack, start = 40.dp, bottom = 10.dp
            )
        }
        profile.companyName?.let { companyName ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.company_name), textValue = companyName,
                textColorHeader = softSteelGray, textColorValue = appBlack, start = 40.dp,
                top = 10.dp, bottom = 10.dp
            )
        }
        profile.address1?.let { address1 ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.address_line_1), textValue = address1,
                textColorHeader = softSteelGray, textColorValue = appBlack, start = 40.dp,
                top = 10.dp, bottom = 10.dp
            )
        }
        profile.pincode1?.let { pinCode ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.pincode), textValue = pinCode,
                bottom = 10.dp, textColorHeader = softSteelGray, top = 10.dp,
                textColorValue = appBlack, start = 40.dp
            )
        }
        profile.address2?.let { address2 ->
            OnlyReadAbleText(
                textHeader = stringResource(id = R.string.address_line_2), textValue = address2,
                textColorHeader = softSteelGray, textColorValue = appBlack, start = 40.dp,
                top = 10.dp, bottom = 10.dp
            )
        }
        profile.pincode2?.let { pinCode ->
            OnlyReadAbleText(
                textColorHeader = softSteelGray, textColorValue = appBlack, start = 40.dp,
                textHeader = stringResource(id = R.string.pincode), textValue = pinCode,
                top = 10.dp, bottom = 10.dp,
            )
        }
        OnlyReadAbleText(
            textHeader = stringResource(id = R.string.purpose), textValue = purpose, top = 10.dp,
            textColorHeader = softSteelGray, textColorValue = appBlack, start = 40.dp,
            bottom = 10.dp
        )
    }
}

@Composable
fun OrderDetailText() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.order_detail), style = normal14Text700,
            modifier = Modifier.padding(start = 40.dp, bottom = 5.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(
    navController: NavHostController, scaffoldState: BottomSheetScaffoldState, fromFlow: String,
    coroutineScope: CoroutineScope, purpose: String, registerViewModel: RegisterViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(40.dp), color = Color.White)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = stringResource(id = R.string.bottom_sheet_close),
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 20.dp, top = 10.dp, bottom = 10.dp)
                .clickable {
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.collapse()
                        registerViewModel.onCheckBoxDetailReset()

                    }
                }
        )
        Text(
            text = stringResource(id = R.string.bottom_sheet_header), style = bold20Text100,
            textAlign = TextAlign.Center, color = appBlack, modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(id = R.string.bottom_sheet_body),
            style = normal12Text400, textAlign = TextAlign.Justify, color = appGray,
            modifier = Modifier.padding(start = 70.dp, end = 70.dp, top = 10.dp, bottom = 10.dp)
        )
        CurvedPrimaryButtonFull(
            text = stringResource(id = R.string.provide_consent).uppercase(),
            modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 20.dp, top = 40.dp)
        ) {
            coroutineScope.launch {
                scaffoldState.bottomSheetState.collapse()
            }
            navigateToLoanProcessScreen(
                navController = navController, transactionId="Sugu",
                        statusId = 8, responseItem = purpose,
                offerId = "1234", fromFlow = fromFlow
            )
        }
    }
}

fun onReviewClick(
    checkboxState: Boolean, navController: NavHostController, purpose: String, context: Context,
    fromFlow: String
) {
    if (checkboxState) {
        if (fromFlow.equals("Personal Loan", ignoreCase = true)) {
            navigateToLoanProcessScreen(
                navController = navController, transactionId="Sugu",
                statusId = 8, responseItem = purpose,
                offerId = "1234", fromFlow = fromFlow
            )
        }  else if (fromFlow.equals("Purchase Finance", ignoreCase = true)) {
            navigateToLoanProcessScreen(
                navController = navController, transactionId="Sugu",
                statusId = 18, responseItem = purpose,
                offerId = "1234", fromFlow = fromFlow
            )
        }

    } else {
        CommonMethods().toastMessage(
            context = context, toastMsg = context.getString(R.string.select_checkbox_to_proceed_further)
        )
    }
}






