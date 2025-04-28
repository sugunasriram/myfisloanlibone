package com.github.sugunasriram.myfisloanlibone.fis_code.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.BuildConfig
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToAboutUsScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToIssueListScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanStatusScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToPrivacyPolicyScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToTermsConditionsScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToUpdateProfileScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appGray85
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.slideActiveColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.textBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.storage.TokenManager
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.RegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBarUi(
    coroutineScope: CoroutineScope, drawerState: DrawerState, isHamBurgerVisible: Boolean = true
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(appGray85)
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (isHamBurgerVisible) {
                Image(
                    painter = painterResource(R.drawable.hamburger_menu),
                    contentDescription = stringResource(id = R.string.hamburger_menu),
                    modifier = Modifier
                        .clickable { openDrawer(coroutineScope, drawerState) }
                        .padding(top = 16.dp, bottom = 16.dp, start = 20.dp, end = 20.dp)
                        .size(width = 30.dp, height = 20.dp)
                        .align(Alignment.TopStart)

                )
            } else {
                Spacer(modifier = Modifier.size(10.dp)) // Placeholder space
                closeDrawer(coroutineScope, drawerState)
            }
            Image(
                painter = painterResource(R.drawable.app_logo),
                contentDescription = stringResource(id = R.string.app_logo),
                modifier = Modifier
                    .size(width = 70.dp, height = 50.dp)
            )
        }

    }
}

fun openDrawer(coroutineScope: CoroutineScope, drawerState: DrawerState) {
    coroutineScope.launch {
        drawerState.open()
    }
}

fun closeDrawer(coroutineScope: CoroutineScope, drawerState: DrawerState) {
    coroutineScope.launch {
        drawerState.close()
    }
}

@Composable
fun SideMenuTextButton(title: String, onClick: () -> Unit) {
    Text(
        text = title, style = normal20Text400, color = appBlack,
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp)
            .clickable { onClick() }
            .padding(start = 12.dp, end = 16.dp, top = 9.dp, bottom = 9.dp),
    )
}

@Composable
fun SideMenuImage(title: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, top = 28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_image_icon),
            contentDescription = stringResource(id = R.string.profile_image_icon),
            modifier = Modifier
                .padding(start = 20.dp)
                .size(150.dp)
                .padding(top = 2.dp, bottom = 2.dp, start = 2.dp, end = 22.dp),
        )
        Text(
            text = title, style = normal32Text500, modifier = Modifier, color = textBlack
        )

    }
}

val brush = Brush.verticalGradient(
    listOf(
        Color(0xFF499BFC).copy(0.8f), Color(0xFF91C2FB).copy(0.3f),
        Color(0xFF499BFC).copy(0.8f)
    )
)


@Composable
fun SideMenuContent(
    coroutineScope: CoroutineScope, drawerState: DrawerState, navController: NavHostController,
) {
    val registerViewModel: RegisterViewModel = viewModel()
    val inProgress by registerViewModel.inProgress.collectAsState()
    val isCompleted by registerViewModel.isCompleted.collectAsState()
    val getUserResponse by registerViewModel.getUserResponse.collectAsState()

    var isAboutExpanded by remember { mutableStateOf(false) }
    var showConfirmationPopUp by remember { mutableStateOf(false) }


    val context = LocalContext.current
    // Fetch user details when this composable is invoked
    LaunchedEffect(Unit) {
        registerViewModel.getUserDetail(context, navController)
    }

    if (inProgress) {
        CenterProgress()
    } else {
        if (isCompleted) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(
                        shape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp),
                        brush = brush
                    )
                    .padding(top = 10.dp)
            ) {
                getUserResponse?.data?.let { user ->
                    val fullName = "${user.firstName ?: ""} ${user.lastName ?: ""}"
                    SideMenuImage(fullName) {}
                }
                HorizontalDivider(start = 0.dp, end = 0.dp)
                SideMenuTextButton("Update Profile") {
                    closeDrawer(coroutineScope, drawerState)
                    navigateToUpdateProfileScreen(navController, fromFlow = "SideBar")
                }
                SideMenuTextButton("Loan Status") {
                    closeDrawer(coroutineScope, drawerState)
                    navigateToLoanStatusScreen(navController)
                }
                SideMenuTextButton("Issues") {
                    closeDrawer(coroutineScope, drawerState)
                    navigateToIssueListScreen(
                        navController = navController, orderId = "12345", fromFlow = "Both",
                        providerId = "12345", loanState = "No Need", fromScreen = "HAMBURGER"
                    )
                }
                /*SideMenuTextButton("Support") {
                    closeDrawer(coroutineScope, drawerState)
                }
                SideMenuTextButton("Notification") {
                    closeDrawer(coroutineScope, drawerState)
                }*/
                SideMenuTextButton("About Us") {
                    closeDrawer(coroutineScope, drawerState)
                    navigateToAboutUsScreen(navController = navController)
                }
                SideMenuTextButton("Privacy Policy") {
                    closeDrawer(coroutineScope, drawerState)
                    navigateToPrivacyPolicyScreen(navController = navController)
                }
                SideMenuTextButton("Terms & Conditions") {
                    closeDrawer(coroutineScope, drawerState)
                   navigateToTermsConditionsScreen(navController = navController)
                }
                SideMenuTextButton("Logout") {
                    showConfirmationPopUp = true
//                    coroutineScope.launch {
//                        // Show confirmation dialog if showConfirmationPopUp is true
//                        if (showConfirmationPopUp) {
//                            ConfirmationDialog(
//                                headerText = stringResource(id = R.string.logout_confirmation),
//                                text = "Are you sure you want to logout?",
//                                onYesClick = {
//                                    coroutineScope.launch {
//                                        val refreshToken = TokenManager.read("refreshToken")
//                                        refreshToken?.let {
//                                            registerViewModel.logout(refreshToken, navController)
//                                        }
//                                    }
//                                    closeDrawer(coroutineScope, drawerState)
//                                    showConfirmationPopUp = false
//                                },
//                                onNoClick = {
//                                    showConfirmationPopUp = false
//                                },
//                            )
//                        }
//                    }



//                    coroutineScope.launch {
//                        val refreshToken = TokenManager.read("refreshToken")
//                        refreshToken?.let {
//                            registerViewModel.logout(refreshToken, navController)
//                        }
//                    }
//                    closeDrawer(coroutineScope, drawerState)
                }
                if (showConfirmationPopUp){
                    AlertDialog(
                        onDismissRequest = { showConfirmationPopUp = false },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showConfirmationPopUp = false;
                                    coroutineScope.launch {
                                    val refreshToken = TokenManager.read("refreshToken")
                                    refreshToken?.let {
                                        registerViewModel.logout(refreshToken, navController)
                                    }
                                }
                                closeDrawer(coroutineScope, drawerState)
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = appBlue)
                            ) {
                                Text("Yes", color = Color.White)
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    showConfirmationPopUp = false
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = appBlue)
                            ) {
                                Text("No", color = Color.White)
                            }
                        },
                        title = {
                            Text(
                                text = stringResource(id = R.string.logout_confirmation), style = normal32Text500,
                                modifier = Modifier, color = textBlack
                            )
                        },
                        text = { Text(stringResource(id = R.string.are_you_sure_you_want_to_logout)
                        ) },
                        modifier = Modifier
                            .border(2.dp, slideActiveColor)
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}



@Preview
@Composable
private fun TopBarPreview() {
    TopBarUi(rememberCoroutineScope(), rememberDrawerState(DrawerValue.Closed),true)
//    SideMenuContent(rememberCoroutineScope(), rememberDrawerState(DrawerValue.Open),
//        rememberNavController()
//    )
}



