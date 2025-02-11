package com.github.sugunasriram.myfisloanlibone.fis_code.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text400

@Composable
fun RadioText(
    text:String = stringResource(id = R.string.sent_otp_to_registered_email),
    radioState: Boolean, onCheckedChange: ((Boolean) -> Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = radioState, onClick =  { onCheckedChange(!radioState) },
            colors = RadioButtonDefaults.colors(selectedColor = appBlue,)
        )
        Text(text = text, fontSize = 14.sp, color = appBlack, style = normal14Text400)
    }
}