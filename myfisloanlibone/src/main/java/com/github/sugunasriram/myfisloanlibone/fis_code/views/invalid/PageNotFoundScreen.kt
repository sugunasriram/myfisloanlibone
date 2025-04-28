package com.github.sugunasriram.myfisloanlibone.fis_code.views.invalid

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.ClickableTextWithIcon
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InnerScreenWithHamburger
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.TopBar
import com.github.sugunasriram.myfisloanlibone.fis_code.components.WrapBorderButton
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.azureBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.bold16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.errorGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.errorRed
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.negativeGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal24Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal30Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.robotoSerifNormal16Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.robotoSerifNormal24Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.semibold14Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.semibold32Text500
import kotlinx.coroutines.delay

@Composable
fun NegativeCommonScreen(
    navController: NavHostController, showRefreshButton: Boolean = true,
    errorText: String = stringResource(id = R.string.page_notfound), onClick: () -> Unit,
    solutionText: String = stringResource(id = R.string.please_try_again_after_sometime),
    errorImage: Painter = painterResource(id = R.drawable.error_404_image),
    buttonText: String = stringResource(id = R.string.retry)
) {
    FixedBottomAndTopBar(showBottom = true, navController = navController) {
        Image(
            painter = errorImage, contentDescription = "", contentScale = ContentScale.Crop
        )

        StartingText(
            text = errorText, alignment = Alignment.TopCenter, style = normal24Text500,
            textColor = Color.Red, top = 10.dp
        )
        StartingText(
            text = solutionText, alignment = Alignment.TopCenter, style = normal16Text400,
            textColor = negativeGray, top = 5.dp
        )
        if (showRefreshButton) {
            ClickableTextWithIcon(text = buttonText, image = R.drawable.refresh_icon) {
                onClick()
            }
        }
    }
}

@Composable
fun FixedBottomAndTopBar(
    navController: NavHostController, showBottom: Boolean = false,
    isSelfScrollable: Boolean = false, pageContent: @Composable () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        // Top bar
        TopBar(navController = navController)

        // Scrollable content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (isSelfScrollable) {
                Column(modifier = Modifier.fillMaxSize()) {
                    pageContent()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    pageContent()
                }
            }
        }
        if (showBottom) {
            Image(
                painter = painterResource(id = R.drawable.ondc_icon),
                contentDescription = stringResource(id = R.string.ondc_icon),
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .size(height = 50.dp, width = 200.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun EmptyLoanStatus(navController: NavHostController) {
    FixedBottomAndTopBar(navController = navController) {
        StartingText(
            text = stringResource(id = R.string.loan_status), textColor = appBlueTitle,
            start = 30.dp, end = 30.dp, top = 30.dp, bottom = 5.dp, style = normal30Text700,
            alignment = Alignment.TopCenter
        )
        Image(
            painter = painterResource(id = R.drawable.loan_not_found),
            contentDescription = "", contentScale = ContentScale.Crop,
            modifier = Modifier.padding(top = 50.dp)
        )
        StartingText(
            text = stringResource(id = R.string.no_existing_loans), textColor = errorGray,
            start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp, style = normal14Text500,
            alignment = Alignment.TopCenter
        )
    }
}

@Composable
fun SessionTimeOutScreen(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.loan_not_found),
                contentDescription = "", contentScale = ContentScale.Crop,
                modifier = Modifier.padding(top = 50.dp)
            )
            StartingText(
                text = stringResource(id = R.string.session_timed_out), textColor = errorGray,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp, style = normal30Text700,
                alignment = Alignment.TopCenter
            )
            StartingText(
                text = stringResource(id = R.string.please_try_after_sometime), start = 30.dp,
                end = 30.dp, textColor = errorGray, top = 10.dp, bottom = 5.dp,
                style = normal14Text500, alignment = Alignment.TopCenter
            )

            ClickableTextWithIcon(
                text = stringResource(id = R.string.retry), image = R.drawable.refresh_icon
            ) { onClick() }
        }
    }
}

@Composable
fun LoanNotApprovedScreen(
    navController: NavHostController,
    text: String = stringResource(id = R.string.loan_not_approved),
) {
    LaunchedEffect(Unit) {
        delay(5000)
        navigateApplyByCategoryScreen(navController = navController)
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StartingText(
                text = text, textColor = errorRed, style = normal32Text700,
                alignment = Alignment.TopCenter
            )

            Image(
                painter = painterResource(id = R.drawable.no_loans_approved_image),
                contentDescription = "", contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 50.dp, start = 20.dp, end = 20.dp)
            )
            StartingText(
                text = stringResource(id = R.string.no_offers_available_discription),
                textColor = errorGray, start = 30.dp, end = 30.dp, top = 30.dp, bottom = 5.dp,
                style = normal14Text500, alignment = Alignment.TopCenter
            )
        }
    }
}

@Composable
fun RequestTimeOutScreen(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.request_time_out_image),
                contentDescription = "", contentScale = ContentScale.Crop,
            )

            StartingText(
                text = stringResource(id = R.string.session_timed_out), textColor = errorGray,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp, style = normal30Text700,
                alignment = Alignment.TopCenter
            )
            StartingText(
                text = stringResource(id = R.string.please_try_after_sometime),
                textColor = errorGray, start = 30.dp, end = 30.dp,
                style = normal14Text500, alignment = Alignment.TopCenter
            )

            ClickableTextWithIcon(
                text = stringResource(id = R.string.retry),
                image = R.drawable.refresh_icon
            ) { onClick() }
        }
    }
}


@Composable
fun UnexpectedErrorScreen(
    navController: NavHostController,
    errorMsgShow: Boolean = true, onClick: () -> Unit,
    errorText: String = stringResource(id = R.string.please_try_after_sometime),
    errorMsg: String = stringResource(id = R.string.something_went_wrong),
) {
    // Top bar
    TopBar(navController = navController, ifErrorFlow = true)

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.error_image),
                contentDescription = "", contentScale = ContentScale.Crop,
            )

            StartingText(
                text = stringResource(id = R.string.oops), textColor = errorGray,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp, style = normal30Text700,
                alignment = Alignment.TopCenter
            )
            if (errorMsgShow) {
                StartingText(
                    start = 30.dp, end = 30.dp, top = 30.dp, bottom = 5.dp, style = normal30Text700,
                    text = errorMsg, textColor = errorGray, alignment = Alignment.TopCenter
                )
            }

            StartingText(
                text = errorText, textColor = errorGray, style = normal14Text500, start = 30.dp,
                end = 30.dp, top = 10.dp, bottom = 5.dp, alignment = Alignment.TopCenter
            )

            ClickableTextWithIcon(
                text = stringResource(id = R.string.retry), image = R.drawable.refresh_icon
            ) { onClick() }
        }
    }
}


@Composable
fun NoLoanOffersFoundScreen(
    navController: NavHostController
) {
    // Top bar
    TopBar(navController = navController, ifErrorFlow = true)

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            StartingText(
                text = stringResource(R.string.sorry),
                textColor = errorRed,
                style = semibold32Text500,
                alignment = Alignment.Center,
            )

            StartingText(
                text = stringResource(R.string.no_loan_offers_available),
                textColor = appBlueTitle,
                style = semibold32Text500,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                alignment = Alignment.Center
            )

            Image(
                painter = painterResource(id = R.drawable.no_offers_image),
                contentDescription = "", contentScale = ContentScale.Crop,
            )

            StartingText(
                text = stringResource(R.string.we_regret_to_inform_you_that_there_are_no_loan_offers_available_for_you_currently),
                textColor = errorGray,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                style = semibold14Text500,
                alignment = Alignment.Center
            )

            WrapBorderButton(
                text = stringResource(id = R.string.go_back).uppercase(),
                modifier = Modifier.padding(top = 30.dp, start = 30.dp, end = 30.dp),
                alignment = Alignment.Center,
                style = bold16Text400,
                shape = RoundedCornerShape(10.dp),
                backgroundColor = azureBlue, textColor = Color.White
            ) {
                navigateApplyByCategoryScreen(navController = navController)
            }
        }
    }
}


@Composable
fun NoResponseFormLenders(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        // Top bar
        TopBar(navController = navController)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.loan_process_stoped_icon),
                    contentDescription = "", contentScale = ContentScale.Crop,
                )

                StartingText(
                    text = stringResource(R.string.sorry),
                    textColor = negativeGray,
                    start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                    style = robotoSerifNormal24Text500,
                    alignment = Alignment.Center
                )

                StartingText(
                    text = "No response from lenders",
                    textColor = negativeGray,
                    start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                    style = robotoSerifNormal24Text500,
                    alignment = Alignment.Center
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ondc_icon),
            contentDescription = stringResource(id = R.string.ondc_icon),
            Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .size(height = 50.dp, width = 200.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun UnAuthorizedScreen(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.un_authorized_image),
                contentDescription = "", contentScale = ContentScale.Crop,
            )

            StartingText(
                text = stringResource(id = R.string.un_authorized_access), textColor = errorGray,
                start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp, style = normal30Text700,
                alignment = Alignment.TopCenter
            )
            StartingText(
                text = stringResource(id = R.string.please_try_with_valid_credentials),
                textColor = errorGray, start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp,
                style = normal14Text500, alignment = Alignment.TopCenter
            )

            ClickableTextWithIcon(
                text = stringResource(id = R.string.retry_login), image = R.drawable.refresh_icon,
            ) { onClick() }
        }
    }
}


@Composable
fun FormRejectionScreen(
    navController: NavHostController,
    fromFlow: String,
    errorMsg: String? = null,
    onClick: () -> Unit = { navigateApplyByCategoryScreen(navController = navController) },
    errorTitle: String = stringResource(id = R.string.kyc_failed)
) {

    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StartingText(
                text = errorTitle, textColor = errorRed, style = normal32Text700,
                alignment = Alignment.TopCenter
            )

            Image(
                painter = painterResource(id = R.drawable.no_loans_approved_image),
                contentDescription = "", contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 50.dp, start = 20.dp, end = 20.dp)
            )

            val displayedSubText = errorMsg?.takeIf { it.isNotEmpty() }
                ?: stringResource(id = R.string.form_submission_rejected_or_pending)

            StartingText(
                text = displayedSubText,
                textColor = errorGray, start = 30.dp, end = 30.dp, top = 30.dp, bottom = 5.dp,
                style = normal14Text500, alignment = Alignment.TopCenter
            )

            ClickableTextWithIcon(
                text = stringResource(id = R.string.retry),
                image = R.drawable.refresh_icon
            ) {
                onClick()
            }

        }
    }

    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateApplyByCategoryScreen(navController = navController)
            }
        }
    }

    DisposableEffect(key1 = backDispatcher) {
        backDispatcher?.addCallback(callback)
        onDispose { callback.remove() }
    }
}

@Composable
fun SomethingWentWrongScreen(navController: NavHostController) {

    // Top bar
    TopBar(navController = navController, ifErrorFlow = true)

    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.something_went_wrong),
                contentDescription = "", contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 50.dp, start = 20.dp, end = 20.dp),
            )

            Spacer(modifier = Modifier.padding(vertical = 50.dp))

            Column(Modifier.padding(10.dp)) {

                StartingText(
                    text = "Something went wrong",
                    textColor = errorGray,
                    style = robotoSerifNormal24Text500,
                    alignment = Alignment.TopCenter,
                )

                StartingText(
                    text = "Please try again after sometime",
                    textColor = errorGray,
                    style = normal20Text400,
                    alignment = Alignment.TopCenter,
                    top = 10.dp
                )

                StartingText(
                    text = "Try Again", textColor = appBlue, style = robotoSerifNormal16Text500,
                    alignment = Alignment.TopCenter, top = 10.dp
                )
            }


//            StartingText(
//                text = displayedSubText,
//                textColor = errorGray, start = 30.dp, end = 30.dp, top = 30.dp, bottom = 5.dp,
//                style = normal14Text500, alignment = Alignment.TopCenter
//            )

            val callback = remember {
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        navigateApplyByCategoryScreen(navController = navController)
                    }
                }
            }

            DisposableEffect(key1 = backDispatcher) {
                backDispatcher?.addCallback(callback)
                onDispose { callback.remove() }
            }
        }
    }

}

@Preview
@Composable
fun NoResponseScreenPreview() {
    Surface {
        NoResponseFormLenders(rememberNavController())
    }
}

@Preview
@Composable
fun FixedBottomAndTopBarScreenPreview() {
    Surface {
        FixedBottomAndTopBar(rememberNavController(), showBottom = true) {

        }
    }
}

@Preview
@Composable
private fun UnAuthorizedScreenPreview() {
    Surface {
        UnAuthorizedScreen {}
    }

}

@Preview
@Composable
fun FormRejectionScreenPreview(modifier: Modifier = Modifier) {
    Surface {
        FormRejectionScreen(navController = rememberNavController(), fromFlow = "", onClick = {})
    }
}

@Preview
@Composable
private fun SomethingWentWrongScreenPreview() {
    Surface {
        SomethingWentWrongScreen(rememberNavController())
    }

}

@Preview
@Composable
private fun NoLoanOffersFoundScreenPreview() {
    Surface {
        NoLoanOffersFoundScreen(rememberNavController())
    }
}
