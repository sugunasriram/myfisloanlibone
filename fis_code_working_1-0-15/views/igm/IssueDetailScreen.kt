package com.github.sugunasriram.myfisloanlibone.fis_code.views.igm

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.NumberFullWidthCard
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToIssueListScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.CloseIssueBody
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.ComplainantActionsItem
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.IssueByIdResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.IssueResolutions
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.IssueResolutionsProviders
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.RespondentActionsItem
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.lightishGray
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.primaryBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.semiBold20Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.softSteelGray
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.igm.CreateIssueViewModel

@Composable
fun IssueDetailScreen(navController: NavHostController, issueId: String,fromFlow: String) {

    val createIssuePLViewModel: CreateIssueViewModel = viewModel()
    val context = LocalContext.current

    val issueClosing by createIssuePLViewModel.issueClosing.collectAsState()
    val issueClosed by createIssuePLViewModel.issueClosed.collectAsState()
    val checkingStatus by createIssuePLViewModel.checkingStatus.collectAsState()
    val checkedStatus by createIssuePLViewModel.checkedStatus.collectAsState()
    val loadingIssue by createIssuePLViewModel.loadingIssue.collectAsState()
    val loadedIssue by createIssuePLViewModel.loadedIssue.collectAsState()
    val issueByIdResponse by createIssuePLViewModel.issueByIdResponse.collectAsState()


    val showInternetScreen by createIssuePLViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by createIssuePLViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by createIssuePLViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by createIssuePLViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by createIssuePLViewModel.unAuthorizedUser.observeAsState(false)

    when {
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        else -> {
            IssueDetailView(
                issueClosing = issueClosing, checkingStatus = checkingStatus, context = context,
                loadingIssue = loadingIssue, checkedStatus = checkedStatus, issueId = issueId,
                loadedIssue = loadedIssue, issueClosed = issueClosed, fromFlow = fromFlow,
                issueByIdResponse = issueByIdResponse, navController = navController,
                createIssuePLViewModel = createIssuePLViewModel,
            )
        }
    }
}

@Composable
fun IssueDetailView(
    issueClosing: Boolean, checkingStatus: Boolean, loadingIssue: Boolean, context: Context,
    checkedStatus: Boolean, loadedIssue: Boolean, issueClosed: Boolean, issueId: String,
    issueByIdResponse: IssueByIdResponse?, navController: NavHostController,
    createIssuePLViewModel: CreateIssueViewModel,fromFlow: String
) {
    if (issueClosing || checkingStatus || loadingIssue) {
        CenterProgress()
    } else if (!issueClosing && issueClosed) {
        navigateToIssueListScreen(
            navController = navController, orderId = "12345", fromFlow = fromFlow,
            providerId = "12345", loanState = "No Need", fromScreen = "Create Issue"
        )
    } else {
        if (checkedStatus || loadedIssue) {
            issueByIdResponse?.let {
                IssueDetails(
                    response = issueByIdResponse, navController = navController, issueId = issueId,
                    createIssuePLViewModel = createIssuePLViewModel, context = context,
                )
            }
        } else if(issueClosed){
            navigateToIssueListScreen(
                navController = navController, orderId = "12345", fromFlow = fromFlow,
                providerId = "12345", loanState = "No Need", fromScreen = "Create Issue"
            )
        } else {
            createIssuePLViewModel.issueById(issueId, context)
        }
    }
}

@Composable
fun IssueDetails(
    response: IssueByIdResponse, navController: NavHostController, context: Context,
    issueId: String, createIssuePLViewModel: CreateIssueViewModel,
) {
    val showCloseButton = !response.data?.data?.summary?.status?.lowercase().equals("close") &&
            !response.data?.data?.summary?.status?.lowercase().equals("closed")

    FixedTopBottomScreen(
        navController = navController, showBottom = showCloseButton, buttonText = "Close",
        buttonText2 = "Status", showdoubleButton = true, isSelfScrollable = false,
        onBackClick = { navController.popBackStack() }, buttonEnd = 5.dp, buttonBottom = 5.dp,
        buttonStart = 5.dp, buttonTop = 5.dp,
        onClick = {
            response.data?.data?.summary?.loanType?.let { loanType ->
                response.data.data.summary.id.let { issueId ->
                    createIssuePLViewModel.closeIssue(
                        CloseIssueBody(
                            loanType = loanType, issueId = issueId, status = "CLOSED",
                            rating = "THUMBS-UP"
                        ),
                        context = context
                    )
                }
            }
        },
        onClick2 = { createIssuePLViewModel.issueStatus(issueId, context) }
    ) {
        if (response.data?.data?.summary?.status?.lowercase().equals("open") ||
            response.data?.data?.summary?.status?.lowercase().equals("processing")
        ) {
            NumberFullWidthCard(cardColor = lightishGray, start = 0.dp, end = 0.dp) {
                Text(
                    text = stringResource(id = R.string.issue_has_been_reported_successfully),
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = normal20Text500,
                    color = primaryBlue
                )
                Text(
                    text = stringResource(id = R.string.issue_has_been_reported_successfully_discription),
                    modifier = Modifier.padding(
                        start = 20.dp, end = 20.dp, top = 20.dp, bottom = 30.dp
                    ),
                    textAlign = TextAlign.Start, style = normal16Text400, color = softSteelGray
                )
            }
        } else if (response.data?.data?.summary?.status?.lowercase().equals("resolved")) {
            NumberFullWidthCard(cardColor = lightishGray, start = 0.dp, end = 0.dp) {
                Text(
                    text = stringResource(id = R.string.issue_has_been_resolved_successfully),
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = normal20Text500,
                    color = primaryBlue
                )
                Text(
                    text = stringResource(id = R.string.issue_has_been_resolved_successfully_discription),
                    modifier = Modifier.padding(
                        start = 20.dp, end = 20.dp, top = 20.dp, bottom = 30.dp
                    ),
                    textAlign = TextAlign.Start, style = normal16Text400, color = softSteelGray
                )
            }
        } else if (response.data?.data?.summary?.status?.lowercase().equals("closed")) {
            NumberFullWidthCard(cardColor = lightishGray, start = 0.dp, end = 0.dp) {
                Text(
                    text = stringResource(id = R.string.issue_has_been_closed_successfully),
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start, style = normal20Text500, color = primaryBlue
                )
                Text(
                    text = stringResource(id = R.string.issue_has_been_closed_successfully_discription),
                    modifier = Modifier.padding(
                        start = 20.dp, end = 20.dp, top = 20.dp, bottom = 30.dp
                    ),
                    textAlign = TextAlign.Start, style = normal16Text400, color = softSteelGray
                )

            }
        }

        when (response.data?.data?.summary?.status?.lowercase()) {
            "open" -> Color.Red        // Red color for Open
            "processing" -> Color.Blue // Blue color for Processing
            "resolved" -> Color.Green  // Green color for Resolved
            else -> Color.Gray         // Default color for other statuses
        }

        //
        var updatedDate : String ;
        if (response?.data?.data?.details?.issueClose?.message?.issue?.updatedAt != null){
            updatedDate = response?.data?.data?.details?.issueClose?.message?.issue?.updatedAt
        }else if (response?.data?.data?.details?.onIssueStatus?.message?.issue?.updatedAt != null){
            updatedDate = response?.data?.data?.details?.onIssueStatus?.message?.issue?.updatedAt
        }
        else if (response?.data?.data?.details?.onIssue?.message?.issue?.updatedAt != null){
            updatedDate = response?.data?.data?.details?.onIssue?.message?.issue?.updatedAt
        }else if (response?.data?.data?.details?.issueOpen?.message?.issue?.updatedAt != null){
            updatedDate = response?.data?.data?.details?.issueOpen?.message?.issue?.updatedAt
        }else{
            updatedDate = response.data?.data?.createdAt.toString()
        }

        response.data?.data?.createdAt?.let { createdAt ->
            response.data.data.summary?.shortDescription?.let { shortDescription ->
            response.data.data.summary?.longDescription?.let { longDescription ->
                response.data.data.summary?.id?.let { issueId ->
                    response.data.data.summary.status?.let { status ->
                        response.data.data.summary.orderId?.let { orderId ->
                            LoanIssueCard1(
                                createdDate = CommonMethods().displayFormattedDate(createdAt),
                                updatedDate = CommonMethods().displayFormattedDate(updatedDate),
                                issueId = issueId, status = status, loanId = orderId,
                                shortDescription = shortDescription,
                                longDescription = longDescription
                            )
                        }
                    }
                }
            }
            }
        }
        if (!response.data?.data?.summary?.respondentActions.isNullOrEmpty()) {
            response.data?.data?.summary?.respondentActions?.let { respondentActions ->
                LoanRespondentDetailCard(respondentActions)
            }
        }
        if (!response.data?.data?.summary?.complainantActions.isNullOrEmpty()) {
            response.data?.data?.summary?.complainantActions?.let { complainantActions ->
                LoanComplainantDetailCard(complainantActions)
            }
        }
        if (response.data?.data?.summary?.resolutions != null) {
            response.data?.data?.summary?.resolutions?.let { resolutions ->
                response.data?.data?.summary?.resolutionsProviders?.let { resolutionsProviders ->
                    LoanResolutionDetailCard(resolutions, resolutionsProviders)
                }
            }
        }
    }
}

@Composable
fun LoanResolutionDetailCard(issueResolutions: IssueResolutions,
                             issueResolutionsProviders: IssueResolutionsProviders) {
    StartingText(
        modifier = Modifier, text = stringResource(id = R.string.resolutions),
        textColor = appBlack, top = 20.dp, start = 20.dp, end = 30.dp,
        style = semiBold20Text500, textAlign = TextAlign.Start,
    )
        NumberFullWidthCard(cardColor = lightishGray, start = 0.dp, end = 0.dp, top = 20.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
            ) {
                issueResolutions?.shortDesc?.let { shortDesc ->
                    ReusableRowText(
                        text = stringResource(id = R.string.short_description)+ ": ", value = shortDesc,
                    )
                }
                issueResolutions?.longDesc?.let { longDesc ->
                    ReusableRowText(
                        text = stringResource(id = R.string.long_description)+ ": ", value = longDesc,
                    )
                }

                issueResolutionsProviders?.contactPerson?.let { contactPerson ->
                    ReusableRowText(
                        text = stringResource(id = R.string.name)+ ": ", value = contactPerson,
                    )
                }
                issueResolutionsProviders?.contactEmail?.let { email ->
                    ReusableRowText(
                        text = stringResource(id = R.string.email)+ ": ", value = email,
                    )
                }
                issueResolutionsProviders?.contactPhone?.let { phone ->
                    ReusableRowText(
                        text = stringResource(id = R.string.mobile_number)+ ": ", value = phone,
                    )
                }
                issueResolutionsProviders?.organizationName?.let { org ->
                    ReusableRowText(
                        text = stringResource(id = R.string.organization) + ": ", value = org,
                    )
                }

                HorizontalDashedLine()

                ReusableRowText(text = "\t\t", value = "\t\t")
            }
        }

}

@Composable
fun LoanRespondentDetailCard(respondentActionList: List<RespondentActionsItem?>) {
    StartingText(
        modifier = Modifier, text = stringResource(id = R.string.respondent_actions),
        textColor = appBlack, top = 20.dp, start = 20.dp, end = 30.dp,
        style = semiBold20Text500, textAlign = TextAlign.Start,
    )
    respondentActionList.forEach { respondentAction ->
        NumberFullWidthCard(cardColor = lightishGray, start = 0.dp, end = 0.dp, top = 20.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
            ) {
                respondentAction?.respondentAction?.let { respondentAction ->
                    ReusableRowText(
                        text = stringResource(id = R.string.action_colon), value = respondentAction,
                    )
                }
                respondentAction?.shortDesc?.let { shortDesc ->
                    ReusableRowText(
                        text = stringResource(id = R.string.description_colon), value = shortDesc,
                    )
                }
                respondentAction?.updatedBy?.person?.name?.let { name ->
                    ReusableRowText(
                        text = stringResource(id = R.string.updated_by_colon), value = name,
                    )
                }
                respondentAction?.updatedBy?.org?.name?.let { name ->
                    ReusableRowText(
                        text = "\t\t\t\t\t\t\t\t\t\t", value = name,
                    )
                }
                respondentAction?.updatedBy?.contact?.phone?.let { phone ->
                    ReusableRowText(
                        text = "\t\t\t\t\t\t\t\t\t\t", value = phone,
                    )
                }
                respondentAction?.updatedBy?.contact?.email?.let { email ->
                    ReusableRowText(
                        text = "\t\t\t\t\t\t\t\t\t\t", value = email,
                    )
                }

                respondentAction?.updatedAt?.let { updatedAt ->
                    ReusableRowText(
                        text = stringResource(id = R.string.updated_on_colon),
                        value = CommonMethods().displayFormattedDate(updatedAt),
                    )
                }

                HorizontalDashedLine()

                ReusableRowText(text = "\t\t", value = "\t\t")
            }
        }

    }
}

@Composable
fun LoanComplainantDetailCard(complainantActionList: List<ComplainantActionsItem?>) {
    StartingText(
        modifier = Modifier, text = stringResource(id = R.string.complainant_actions), end = 30.dp,
        textColor = appBlack, top = 20.dp, start = 20.dp, style = semiBold20Text500,
        textAlign = TextAlign.Start,
    )
    complainantActionList.forEach { complainantAction ->
        NumberFullWidthCard(cardColor = lightishGray, start = 0.dp, end = 0.dp, top = 20.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
            ) {

                complainantAction?.complainantAction?.let { complainantAction ->
                    ReusableRowText(
                        value = complainantAction,
                        text = stringResource(id = R.string.action_colon),
                    )
                }

                complainantAction?.shortDesc?.let { shortDesc ->
                    ReusableRowText(
                        text = stringResource(id = R.string.description_colon), value = shortDesc,
                    )
                }
                complainantAction?.updatedBy?.orgName?.let { orgName ->
                    ReusableRowText(
                        text = stringResource(id = R.string.updated_by_colon), value = orgName,
                    )
                }

                complainantAction?.updatedBy?.let { update ->
                    update.org?.name?.let { name ->
                        ReusableRowText(text = "\t\t\t\t\t\t\t\t\t\t", value = name)
                    }
                    update.contactPhone?.let { contactPhone ->
                        ReusableRowText(
                            text = "\t\t\t\t\t\t\t\t\t\t", value = contactPhone,
                        )
                    }
                    update.contactEmail?.let { contactEmail ->
                        ReusableRowText(text = "\t\t\t\t\t\t\t\t\t\t", value = contactEmail)
                    }

                }
                complainantAction?.updatedAt?.let { updatedAt ->
                    ReusableRowText(
                        text = stringResource(id = R.string.updated_on_colon),
                        value = CommonMethods().displayFormattedDate(updatedAt),
                    )
                }
                HorizontalDashedLine()
                ReusableRowText(
                    text = "\t\t", value = "\t\t",
                )
            }
        }
    }
}

@Composable
fun LoanIssueCard1(createdDate: String, updatedDate: String, issueId: String,
                   status: String, loanId: String,
                   shortDescription: String,
                   longDescription: String) {
    NumberFullWidthCard(cardColor = lightishGray, start = 0.dp, end = 0.dp, top = 30.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Created On : $createdDate", color = softSteelGray, style = normal16Text400,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
            )
            StatusChip(status = status, modifier = Modifier.padding(top = 20.dp, end = 20.dp))
        }
        Text(
            text = "Last Updated : $updatedDate", color = softSteelGray, style = normal16Text400,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        )
        ReusableText(text = "Loan Id", color = appBlack, style = normal16Text700, top = 20.dp)
        ReusableText(text = loanId)

        ReusableText(text = "Issue Id", color = appBlack, style = normal16Text700, top = 20.dp)
        ReusableText(text = issueId)

        ReusableText(text = "Short Description", color = appBlack, style = normal16Text700, top = 20.dp)
        ReusableText(text = shortDescription)

        ReusableText(text = "Long Description", color = appBlack, style = normal16Text700, top = 20.dp)
        ReusableText(text = longDescription)

    }
}

@Composable
fun HorizontalDashedLine(
    color: Color = Color.Gray, lineHeight: Dp = 2.dp, dashLength: Dp = 10.dp, gapLength: Dp = 5.dp
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(lineHeight)
    ) {
        val strokeWidth = lineHeight.toPx()
        val dashOn = dashLength.toPx()
        val dashOff = gapLength.toPx()

        drawLine(
            color = color, start = androidx.compose.ui.geometry.Offset(0f, center.y),
            end = androidx.compose.ui.geometry.Offset(size.width, center.y),
            strokeWidth = strokeWidth, cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashOn, dashOff), 0f),

            )
    }
}

@Composable
fun ReusableText(
    text: String, color: Color = softSteelGray, style: TextStyle = normal16Text700,
    textAlign: TextAlign = TextAlign.Start, modifier: Modifier = Modifier,
    start: Dp = 20.dp, end: Dp = 0.dp, top: Dp = 0.dp, bottom: Dp = 10.dp
) {
    Text(
        text = text, color = color, style = style, textAlign = textAlign,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, bottom = bottom, end = end, top = top)
    )
}