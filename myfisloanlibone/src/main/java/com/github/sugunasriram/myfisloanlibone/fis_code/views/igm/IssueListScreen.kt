package com.github.sugunasriram.myfisloanlibone.fis_code.views.igm

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.StartingText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToCreateIssueScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToIssueDetailScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanStatusScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.IssueListBody
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.IssueListResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.IssueObj
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.OrderIssueResponse
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.greenColour
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.softSteelGray
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.igm.CreateIssueViewModel
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.negativeGray


@Composable
fun IssueListScreen(
    navController: NavHostController, orderId: String, loanState: String, providerId: String,
    fromFlow: String, fromScreen: String
) {

    val context = LocalContext.current
    val createIssueViewModel: CreateIssueViewModel = viewModel()
    val issueListLoading by createIssueViewModel.issueListLoading.collectAsState()
    val issueListLoaded by createIssueViewModel.issueListLoaded.collectAsState()
    val issueList by createIssueViewModel.issueListResponse.collectAsState()
    val orderIssuesLoading by createIssueViewModel.orderIssuesLoading.collectAsState()
    val orderIssuesLoaded by createIssueViewModel.orderIssuesLoaded.collectAsState()
    val orderIssuesResponse by createIssueViewModel.orderIssuesResponse.collectAsState()

    val showInternetScreen by createIssueViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by createIssueViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by createIssueViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by createIssueViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by createIssueViewModel.unAuthorizedUser.observeAsState(false)

    BackHandler { onIssueBackClick(fromScreen = fromScreen, navController = navController) }

    when {
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        else -> {
            IssueListView(
                issueListLoading = issueListLoading, orderIssuesLoading = orderIssuesLoading,
                issueListLoaded = issueListLoaded, orderIssuesLoaded = orderIssuesLoaded,
                navController = navController, fromScreen = fromScreen, fromFlow = fromFlow,
                orderIssuesResponse = orderIssuesResponse, loanState = loanState,
                providerId = providerId, orderId = orderId, issueList = issueList,
                createIssueViewModel = createIssueViewModel, context = context
            )
        }
    }
}

@Composable
fun IssueListView(
    orderIssuesResponse: OrderIssueResponse?, loanState: String, providerId: String,
    issueListLoading: Boolean, orderIssuesLoading: Boolean, context: Context,
    navController: NavHostController, fromScreen: String, orderId: String,
    issueListLoaded: Boolean, orderIssuesLoaded: Boolean, issueList: IssueListResponse?,
    createIssueViewModel: CreateIssueViewModel,fromFlow: String
) {
    if (issueListLoading || orderIssuesLoading) {
        CenterProgress()
    } else {
        if (issueListLoaded || orderIssuesLoaded) {

            val issues =
                if (issueList != null) issueList.data?.pageData?.issues else
                    orderIssuesResponse?.data?.data
            issues?.let {

                FixedTopBottomScreen(
                    navController = navController, isSelfScrollable = issues.isEmpty(), buttonText = "Raise Issue",
                    showBottom = fromScreen.equals("Loan Detail", ignoreCase = true),
                    onBackClick = {
                        onIssueBackClick(fromScreen = fromScreen, navController = navController)
                    },
                    onClick = {
                        onRaiseIssueClick(
                            orderIssuesResponse = orderIssuesResponse, navController = navController,
                            orderId = orderId, providerId = providerId, loanState = loanState
                        )
                    }
                ) {
                    if (it.isEmpty()) {
                        EmptyListScreen()
                    } else {
                        Row {
                            StartingText(
                                text = stringResource(id = R.string.issue_list),
                                textColor = appBlueTitle,
                                start = 30.dp,
                                end = 30.dp,
                                top = 10.dp,
                                bottom = 5.dp,
                                style = normal32Text700
                            )
                        }

                        IssueListScreen(navController, issues, fromFlow = fromFlow)
                    }

                }
            }
        } else {
            if (fromScreen.equals("Loan Detail", ignoreCase = true)) {
                createIssueViewModel.orderIssues(orderId = orderId, context = context)
            } else {
                createIssueViewModel.getIssueListForUser(context, IssueListBody(1, 10))
            }
        }
    }
}


@Composable
fun EmptyListScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_existing_issues),
            textAlign = TextAlign.Center, fontSize = 32.sp, color = negativeGray,
            fontFamily = FontFamily(Font(R.font.robotocondensed_regular)),
            fontWeight = FontWeight(800),

            )
    }
}

@Composable
fun IssueListScreen(navController: NavHostController, issueList: List<IssueObj>?,fromFlow: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        issueList?.forEach { issue ->
            IssueCard(issue, onClick = {
                issue.id?.let {
                    navigateToIssueDetailScreen(
                        navController = navController, issueId = it, fromFlow = fromFlow
                    )
                }
            })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun IssueCard(issue: IssueObj, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp), elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top // Aligns the elements at the top of the row
            ) {
                issue.summary?.loanType?.let { loanType ->
                 val loanName =  if (loanType.equals("INVOICE_BASED_LOAN")){
                        "Invoice Based Loan"
                    } else {
                        "Personal Loan"
                    }
                    ReusableRowText(text = "Item Name : ", value = loanName)
                }
                val preferredColor = when (issue.summary?.status?.lowercase()) {
                    "open" -> Color.Red        // Red color for Open
                    "processing" -> Color.Blue // Blue color for Processing
                    "resolved" -> Color.Green  // Green color for Resolved
                    else -> Color.Gray         // Default color for other statuses
                }

                issue.summary?.status?.let {
                    StatusChip(
                        status = it, backGroundColor = Color.White, borderColor = preferredColor,
                        textColor = preferredColor, cardWidth = 1.dp,
                        modifier = Modifier.padding(top = 5.dp, end = 0.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            issue.summary?.orderId?.let { ReusableRowText(text = "Order ID : ", value = it) }
            issue.summary?.id?.let { ReusableRowText(text = "Issue ID : ", value = it) }
            ReusableRowText(text = "Category Name : ", value = "${issue.summary?.category}")
        }
    }
}

@Composable
fun ReusableRowText(
    text: String, value: String, color: Color = softSteelGray, style: TextStyle = normal16Text700,
    textAlign: TextAlign = TextAlign.Start, modifier: Modifier = Modifier,
    start: Dp = 0.dp, end: Dp = 0.dp, top: Dp = 0.dp, bottom: Dp = 10.dp
) {
    Row {
        Text(
            text = text, color = appBlack, style = normal16Text700, textAlign = textAlign,
            modifier = modifier
                .padding(start = start, bottom = bottom, end = end, top = top)
        )
        Text(
            text = value, color = color, style = style, textAlign = textAlign,
            modifier = modifier
                .padding(start = start, bottom = bottom, end = end, top = top)
        )
    }
}

@Composable
fun StatusChip(
    status: String, modifier: Modifier, backGroundColor: Color = greenColour,
    borderColor: Color = greenColour, cardWidth: Dp = 0.dp, textColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .background(color = backGroundColor, shape = RoundedCornerShape(16.dp))
            .border(width = cardWidth, color = borderColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 18.dp, vertical = 4.dp)
    ) {
        Text(
            text = status, color = textColor,
            style = MaterialTheme.typography.body2.copy(fontSize = 12.sp)
        )
    }
}

fun onRaiseIssueClick(
    orderIssuesResponse: OrderIssueResponse?, navController: NavHostController, loanState: String,
    orderId: String, providerId: String,
) {
    orderIssuesResponse?.data?.data?.forEach { issue ->
        issue.summary?.loanType?.let { loanType ->
            val setFlow = CommonMethods().setFromFlow(loanType)
            navigateToCreateIssueScreen(
                navController = navController, orderId = orderId, providerId = providerId,
                orderState = loanState, fromFlow = setFlow,
            )
        }
    }
}

fun onIssueBackClick(fromScreen: String, navController: NavHostController) {
    if (fromScreen.equals("HAMBURGER", ignoreCase = true)) {
        navigateApplyByCategoryScreen(navController)
    } else {
        navigateToLoanStatusScreen(navController)
    }
}