package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.SpaceTextWithRadio
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToSelectBankScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToWebViewFlowOneScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.BankItem
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.lightishGray
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods

val bankList = arrayListOf<BankItem?>(
    BankItem(
        bankName = "Finvu",
        imageBank = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTBaGPXz45LQV0sjCCqrERZUTWGDT7c3j9adcVGIhTfzw&s",
        id = "15678"
    )
)

@Composable
fun SelectAccountAgreegatorScreen(
    navController: NavHostController, purpose: String, fromFlow: String
) {

    BackHandler {
        if (fromFlow.equals("Invoice Loan", ignoreCase = true)) {
            navigateToSelectBankScreen(
                navController = navController, purpose = purpose, fromFlow = fromFlow
            )
        }
    }
    val context = LocalContext.current
    val (selectedOption, onOptionSelected) = remember { mutableStateOf<String?>(if (bankList.size == 1) bankList[0]?.bankName else null) }
    FixedTopBottomScreen(
        navController = navController, backGroudColorChange = selectedOption != null,
        onBackClick = {
            navigateToSelectBankScreen(
                navController = navController, purpose = purpose, fromFlow = fromFlow
            )
        },
        onClick = {
            onButtonClicked(
                selectedOption = selectedOption, navController = navController,
                purpose = purpose, fromFlow = fromFlow, context = context
            )
        }
    ) {
        ScreenTopSection(
            headerText = stringResource(id = R.string.select_account_aggregator),
            subHeaderText = stringResource(id = R.string.choose_rbi_approved_account)
        )
        SpaceTextWithRadio(
            backGroundColor = lightishGray, radioOptions = bankList, top = 2.dp,
            selectedOption = selectedOption, onOptionSelected = onOptionSelected
        )
    }
}

fun onButtonClicked(
    selectedOption: String?, navController: NavHostController, purpose: String, fromFlow: String,
    context: Context
) {
    if (selectedOption != null) {
        navigateToWebViewFlowOneScreen(navController, purpose, fromFlow)
    } else {
        CommonMethods().toastMessage(
            context = context,
            toastMsg = context.getString(R.string.please_select_account_aggregator)
        )
    }
}
