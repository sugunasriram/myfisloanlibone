package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenteredMoneyImage
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CheckBoxText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CurvedPrimaryButtonFull
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InnerScreenWithHamburger
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.disableColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal32Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.primaryBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan.PersonalLoanViewModel

@Composable
fun PersonaLoanScreen(navController: NavHostController,fromFlow:String) {
    val context = LocalContext.current
    val personalLoanViewModel: PersonalLoanViewModel = viewModel()
    val adharNumberLink: Boolean by personalLoanViewModel.adharNumberLink.observeAsState(false)
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
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 50.dp, bottom = 30.dp),
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

        CurvedPrimaryButtonFull(
            text = stringResource(id = R.string.next),
            modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 20.dp, top = 40.dp),
            backgroundColor = if (adharNumberLink) appBlue else disableColor
        ) {
            personalLoanViewModel.buttonValidation(
                adharNumberLink = adharNumberLink,
                navController = navController, context = context, fromFlow = fromFlow
            )
        }
    }
}

@Preview
@Composable
fun PersonaLoanScreenPreview(){
    PersonaLoanScreen(rememberNavController(),"Personal")
}