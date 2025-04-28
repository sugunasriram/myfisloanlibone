package com.github.sugunasriram.myfisloanlibone.fis_code.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appTheme
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text400

@Composable
fun DropDown(
    mExpanded: Boolean, purpose: List<String>, onItemSelected: (String) -> Unit,
    onDismiss: () -> Unit, modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = mExpanded, onDismissRequest = onDismiss, modifier = modifier
    ) {
        purpose.forEach { label ->
            DropdownMenuItem(
                onClick = {
                    onItemSelected(label)
                    onDismiss()
                },
                modifier = Modifier
            ) {
                Text(text = label, style = normal20Text400)
            }
        }
    }
}

@Preview
@Composable
fun DDropDownTextFieldPreview (){
    var genderSelectedText by remember { mutableStateOf("") }
    var genderExpand by remember { mutableStateOf(false) }
    val genderList = listOf(
        stringResource(id = R.string.male), stringResource(id = R.string.female),
        stringResource(id = R.string.transgender)
    )
    val onGenderDismiss: () -> Unit = { genderExpand = false }
    val onGenderSelected: (String) -> Unit = { selectedText ->
        genderSelectedText = selectedText
//        registerViewModel.onGenderChanged(selectedText)
    }
    val genderError: String? = ""

    DropDownTextField(
        start = 40.dp, end = 40.dp, top = 10.dp, selectedText = "",
        hint = stringResource(id = R.string.gender),
         expand = genderExpand, error = genderError,
        setExpand = { genderExpand = it }, itemList = genderList,
        onDismiss = onGenderDismiss,onItemSelected = onGenderSelected
    )
}
@Composable
fun DropDownTextField(
    start: Dp = 20.dp, end: Dp = 20.dp, top: Dp = 10.dp, bottom: Dp = 0.dp, selectedText: String,
    hint: String, expand: Boolean, setExpand: (Boolean) -> Unit, itemList: List<String>,
    focus: FocusRequester = FocusRequester.Default,
    onNextFocus: FocusRequester = FocusRequester.Default, error: String? = null,
    onDismiss: () -> Unit, onItemSelected: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val formattedText = selectedText.replaceFirstChar { it.uppercase() }

    Row(modifier = Modifier
        .padding(start = start, end = end, top = top, bottom = bottom)
        .clickable {
            setExpand(!expand)
            if (!expand) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        }
    ) {
        Box{
            InputField(
                inputText = formattedText, hint = hint, start = 0.dp, end = 0.dp, top = 0.dp,
                readOnly = true,
                modifier = Modifier
                    .focusRequester(focus)
                    .clickable {
                        setExpand(!expand)
                        if (!expand) {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onNext = { onNextFocus.requestFocus() }),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = appTheme, unfocusedBorderColor = appTheme,
                    cursorColor = Color.Transparent, errorCursorColor = Color.Transparent
                ),
                onValueChange = {},
                error = error ?: "",

                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.down_ward_image),
                        contentDescription = stringResource(id = R.string.down_ward_image),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                setExpand(!expand)
                                if (!expand) {
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                            }
                    )
                }
            )
        }
        DropDown(
            mExpanded = expand, purpose = itemList, onItemSelected = onItemSelected,
            onDismiss = onDismiss
        )
    }
}


