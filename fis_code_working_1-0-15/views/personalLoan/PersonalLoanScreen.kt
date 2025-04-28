package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.LoanLib
import com.github.sugunasriram.myfisloanlibone.MainActivity
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenteredMoneyImage
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CheckBoxText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InnerScreenWithHamburger
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.disableColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.PersonalLoanViewModel

@Composable
fun PersonaLoanScreen(navController: NavHostController,fromFlow:String) {
    val context = LocalContext.current
    val personalLoanViewModel: PersonalLoanViewModel = viewModel()
    val adharNumberLink: Boolean by personalLoanViewModel.adharNumberLink.observeAsState(false)
    val consent: Boolean by personalLoanViewModel.consent.observeAsState(false)
    val generalError by personalLoanViewModel.generalError.observeAsState("")

    BackHandler {
        personalLoanViewModel.updateGeneralError(null)
        navigateApplyByCategoryScreen(navController)
    }
    InnerScreenWithHamburger(isSelfScrollable = false, navController = navController) {
        CenteredMoneyImage(imageSize = 200.dp, top = 20.dp)
        RegisterText(
            text = stringResource(id = R.string.personal_loan),
            textColor = appBlueTitle, style = normal32Text700
        )
        Text(
            text = stringResource(id = R.string.select_documents),
            modifier = Modifier.padding(start = 20.dp,end = 20.dp, top = 50.dp, bottom = 30.dp),
            textAlign = TextAlign.Start,
            style = normal20Text500
        )
        CheckBoxText(
            boxState =adharNumberLink ,
            text = stringResource(id = R.string.adhar_linked_number),
            start = 20.dp,
            onCheckedChange = {
                personalLoanViewModel.onAdharNumberLinkChanged(it)
                personalLoanViewModel.updateGeneralError(null)
            }
        )
        CheckBoxText(
            boxState = consent,
            text = stringResource(id = R.string.account_consent),
            start = 20.dp,
            onCheckedChange = {
                personalLoanViewModel.onConsentChanged(it)
                personalLoanViewModel.updateGeneralError(null)
            })
        if (!generalError.isNullOrEmpty()){
            CommonMethods().toastMessage(context, generalError.toString())
        }

        CurvedPrimaryButtonFull(
            text = stringResource(id = R.string.next),
            modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 20.dp, top = 40.dp),
            backgroundColor = if(consent && adharNumberLink) appBlue else disableColor
        ) {
            personalLoanViewModel.buttonValidation(
                adharNumberLink = adharNumberLink, consent = consent,
                navController = navController, context = context, fromFlow = fromFlow
            )
        }

        //Sugu - testing - to be removed  - Start
        CurvedPrimaryButtonFull(
            text = "Send Loan Details",
            modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 20.dp, top = 40.dp),
            backgroundColor = if(consent && adharNumberLink) appBlue else disableColor
        ) {
            LoanLib.callback?.invoke(LoanLib.LoanDetails(interestRate = 5.0, loanAmount = 10000.0, tenure = 12))
            (context as? Activity)?.finish()

        }
        //Sugu - testing - to be removed  - End
    }
}

@Preview
@Composable
fun PersonaLoanScreenPreview(){
    PersonaLoanScreen(rememberNavController(),"Personal")
}