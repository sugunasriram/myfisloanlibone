package com.github.sugunasriram.myfisloanlibone.fis_code.views.personalLoan

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.HorizontalDivider
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InnerScreenWithHamburger
import com.github.sugunasriram.myfisloanlibone.fis_code.components.LanguageText
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appDarkTeal
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal36Text700

@Composable
fun LanguageSelectionScreen(navController: NavHostController) {

    val languages = listOf(
        stringResource(id = R.string.english),
        stringResource(id = R.string.kannada),
        stringResource(id = R.string.hindi),
        stringResource(id = R.string.tamil),
        stringResource(id = R.string.telegu),
        stringResource(id = R.string.marati)
    )
    var showIcon by rememberSaveable { mutableStateOf(true) }

    InnerScreenWithHamburger(isSelfScrollable = true, navController = navController) {
        LazyColumn {
            items(languages.size) { index ->
                if (index == 0) {
                    RegisterText(
                        text = stringResource(id = R.string.choose_language),
                        textColor = appDarkTeal, style = normal36Text700,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 50.dp)
                    )
                }
                HorizontalDivider()
                LanguageText(
                    language = languages[index],
                    showIcon = if (index == 0) showIcon else false
                ) {
                    if (index != 0) {
                        showIcon = !showIcon
                    }
                }
            }
        }
    }
}