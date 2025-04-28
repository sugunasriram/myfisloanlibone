package com.github.sugunasriram.myfisloanlibone.fis_code.components


import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateApplyByCategoryScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.auth.BankItem
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.gst.InvoicesItem
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlueTitle
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appDarkTeal
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appGray85
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appTheme
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.azureBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.bold14Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.bold16Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.bold18Text100
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.cursorColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.disableColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.hintTextColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.hyperTextColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.newBlueColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.newRedColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.newWhiteColor
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal12Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal14Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal16Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal18Text400
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal36Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.otpBackground
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.primaryBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.semiBold24Text700
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.softSteelGray
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.auth.OtpViewModel
import java.util.Locale


@Composable
fun InnerScreenWithHamburger(
    modifier: Modifier = Modifier, isSelfScrollable: Boolean = false,
    isHamBurgerVisible: Boolean = true, navController: NavHostController = rememberNavController(),
    pageContent: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFF9AC0F5))
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        if (isHamBurgerVisible) {
            ModalDrawer(drawerState = drawerState,
                drawerBackgroundColor = Color.White,
                drawerContent = {
                    SideMenuContent(coroutineScope, drawerState, navController)
                }) {
                Scaffold(
                    topBar = { TopBarUi(coroutineScope, drawerState, isHamBurgerVisible) },
                ) { paddingValues ->
                    if (isSelfScrollable) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
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
            }
        } else {
            Scaffold(
                topBar = { TopBarUi(coroutineScope, drawerState, isHamBurgerVisible) },
            )

            { paddingValues ->
                if (isSelfScrollable) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
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
        }

    }
}

@Composable
fun OutlinedTextFieldValidation(
    value: String, onValueChange: (String) -> Unit, enabled: Boolean = true, error: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(0.8f), readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current, label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null, leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = error.isNotEmpty(), singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default, maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(disabledTextColor = Color.Black),
    trailingIcon: @Composable (() -> Unit)? = {
        if (error.isNotEmpty())
            Icon(Icons.Filled.Error, "error", tint = MaterialTheme.colors.error)
    },
) {
    Column(modifier = modifier.padding(0.dp)) {
        OutlinedTextField(
            enabled = enabled,
            readOnly = readOnly,
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(start = 16.dp, top = 0.dp)
            )
        }
    }
}

@Composable
fun InputField(
    inputText: String?, hint: String, modifier: Modifier = Modifier, start: Dp = 40.dp,
    mandatory: Boolean = false,
    end: Dp = 40.dp, visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null, leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default, top: Dp = 30.dp,
    keyboardActions: KeyboardActions = KeyboardActions.Default, label: (() -> Unit)? = null,
    enable: Boolean = true, error: String? = null, onValueChange: (String) -> Unit,
    readOnly: Boolean = false, hintAlign: TextAlign = TextAlign.Start,
    textStyle: TextStyle = normal18Text400,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = appTheme,
        unfocusedBorderColor = appTheme,
        cursorColor = cursorColor
    )
) {
    OutlinedTextFieldValidation(
        value = inputText ?: "",
        onValueChange = onValueChange,
        modifier = modifier
            .padding(start = start, end = end, top = top)
            .fillMaxWidth(),
        readOnly = readOnly,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        label = {
            Text(
                text = hint, color = hintTextColor, style = normal18Text400,
                textAlign = hintAlign
            )
        },
        colors = colors,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enable,
        error = error ?: ""
    )
}


@Composable
fun NotRegisteredText(
    text: String, modifier: Modifier = Modifier, padding: Dp = 10.dp, textColor: Color = appBlack,
    style: TextStyle = bold18Text100
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Text(
            text = text,
            color = textColor,
            style = style
        )
    }
}

@Composable
fun SignUpText(
    text: String, modifier: Modifier = Modifier, textColor: Color = hyperTextColor,
    start: Dp = 8.dp, end: Dp = 8.dp, top: Dp = 8.dp, bottom: Dp = 8.dp,
    style: TextStyle = normal14Text500, onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = text,
            color = textColor,
            style = style,
            modifier = modifier
                .clickable { onClick() }
                .padding(start = start, end = end, top = top, bottom = bottom),
        )
    }
}

@Composable
fun HyperText(
    text: String, modifier: Modifier = Modifier, textColor: Color = primaryBlue, start: Dp = 8.dp,
    end: Dp = 40.dp, top: Dp = 8.dp, bottom: Dp = 8.dp, boxTop: Dp = 0.dp,
    alignment: Alignment = Alignment.TopEnd, onClick: () -> Unit
) {
    Box(
        contentAlignment = alignment,
        modifier = modifier
            .padding(end = end, top = boxTop)
            .fillMaxSize()
    ) {
        Text(
            text = text,
            color = textColor,
            modifier = modifier
                .clickable { onClick() }
                .padding(start = start, top = top, bottom = bottom),
            style = bold16Text400

        )
    }
}

@Composable
fun RegisterText(
    text: String, textColor: Color = appDarkTeal, modifier: Modifier = Modifier, top: Dp = 0.dp,
    style: TextStyle = normal36Text700, textAlign: TextAlign = TextAlign.Center, bottom: Dp = 0.dp,
    start: Dp = 0.dp, end: Dp = 0.dp, size: Dp = 10.dp, boxAlign: Alignment = Alignment.Center
) {
    Box(
        contentAlignment = boxAlign,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = top, bottom = bottom, start = start, end = end)
    ) {
        Text(
            text = text,
            color = textColor,
            style = style,
            textAlign = textAlign
        )
    }
}

@Composable
fun OnlyReadAbleText(
    textHeader: String, textValue: String, modifier: Modifier = Modifier, end: Dp = 20.dp,
    textColorHeader: Color = appBlueTitle, textColorValue: Color = appBlueTitle, start: Dp = 10.dp,
    top: Dp = 8.dp, bottom: Dp = 0.dp, style: TextStyle = normal14Text400,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
    ) {
        Text(
            text = textHeader,
            style = style,
            color = textColorHeader,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = textValue,
            style = style,
            color = textColorValue,
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun LanguageText(
    language: String, showIcon: Boolean, style: TextStyle = normal14Text400, start: Dp = 24.dp,
    end: Dp = 24.dp, top: Dp = 10.dp, bottom: Dp = 10.dp, height: Dp = 35.dp, width: Dp = 1.dp,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
    ) {
        Text(text = language, style = style)
        if (showIcon) {
            Image(
                painter = painterResource(id = R.drawable.correct_icon),
                contentDescription = stringResource(id = R.string.correct_icon)
            )
        }
    }
}

@Composable
fun SpaceBetweenText(
    offer: Boolean = false, text: String = "", value: String = "", top: Dp = 20.dp,
    image: Painter = painterResource(id = R.drawable.app_logo), showImage: Boolean = false,
    showText: Boolean = true, start: Dp = 10.dp, end: Dp = 10.dp,
    color: Color = appBlueTitle, offerText: String = stringResource(id = R.string.order_value),
    style: TextStyle = normal14Text400,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = 10.dp)
    ) {
        if (showImage) {
            Image(
                painter = image,
                contentDescription = stringResource(id = R.string.bank_image),
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
            if (showText) {
                Image(
                    painter = painterResource(id = R.drawable.forward_icon),
                    contentDescription = stringResource(id = R.string.bank_image)
                )
            } else {
                Text(text = text, style = style, color = color)
            }
        } else {
            Text(text = text, style = style, fontWeight = FontWeight.Bold)
            Column(horizontalAlignment = Alignment.End) {
                Text(text = value, style = style, fontWeight = FontWeight.Bold)
                if (offer) {
                    Text(text = offerText, style = style, modifier = Modifier.padding(0.dp))
                }
            }
        }
    }
}

@Composable
fun MultipleColorText(
    text: String, modifier: Modifier = Modifier, textColor: Color = hyperTextColor,
    start: Dp = 8.dp, resendOtpColor: Color = Color.Red, end: Dp = 8.dp, top: Dp = 8.dp,
    bottom: Dp = 8.dp, style: TextStyle = normal14Text500, onClick: () -> Unit,
) {
    val annotatedString = AnnotatedString.Builder().apply {
        append(text)
        val resendOtpIndex = text.indexOf("Resend OTP")
        if (resendOtpIndex != -1) {
            addStyle(SpanStyle(color = resendOtpColor), resendOtpIndex, text.length)
        }
    }.toAnnotatedString()

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = annotatedString,
            color = textColor,
            style = style,
            modifier = modifier
                .clickable { onClick() }
                .padding(start = start, end = end, top = top, bottom = bottom),
        )
    }
}

@Composable
fun StartingText(
    text: String, textColor: Color = appDarkTeal, modifier: Modifier = Modifier, top: Dp = 0.dp,
    bottom: Dp = 0.dp, start: Dp = 0.dp, end: Dp = 0.dp, style: TextStyle = normal14Text400,
    textAlign: TextAlign = TextAlign.Center, backGroundColor: Color = Color.Transparent,
    alignment: Alignment = Alignment.TopStart,
) {
    Box(
        contentAlignment = alignment,
        modifier = modifier
            .fillMaxWidth()
            .background(backGroundColor)
            .padding(top = top, bottom = bottom, start = start, end = end)
    ) {
        Text(
            text = text,
            color = textColor,
            style = style,
            textAlign = textAlign
        )
    }
}


@Composable
fun TextValueInARow(
    textHeader: String, textValue: String, modifier: Modifier = Modifier, end: Dp = 20.dp,
    textColorHeader: Color = appBlueTitle, textColorValue: Color = appBlueTitle, start: Dp = 10.dp,
    style: TextStyle = normal14Text400, style1: TextStyle = normal20Text700, top: Dp = 8.dp,
    bottom: Dp = 0.dp,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
    ) {
        Text(
            text = textHeader,
            style = style,
            color = textColorHeader,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = textValue,
            style = style1,
            color = textColorValue,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun TextHyphenValueInARow(
    textHeader: String, textValue: String, modifier: Modifier = Modifier, end: Dp = 20.dp,
    textColorHeader: Color = appBlueTitle, textColorValue: Color = appBlueTitle, start: Dp = 10.dp,
    style: TextStyle = normal14Text400, style1: TextStyle = normal20Text700, top: Dp = 8.dp,
    bottom: Dp = 0.dp,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
    ) {
        Text(
            text = textHeader,
            style = style,
            color = textColorHeader,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = " - ",
            style = style1,
            color = textColorValue,
            modifier = Modifier.weight(0.15f),
            textAlign = TextAlign.Start
        )
        Text(
            text = textValue,
            style = style1,
            color = textColorValue,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun SpaceBetweenTextIcon(
    modifier: Modifier = Modifier, text: String, @DrawableRes image: Int = R.drawable.correct_icon,
    style: TextStyle = normal16Text700, start: Dp = 0.dp, end: Dp = 0.dp, top: Dp = 10.dp,
    bottom: Dp = 10.dp, height: Dp = 35.dp, width: Dp = 1.dp, textColor: Color = appBlack,
    backGroundColor: Color = Color.White, imageSize: Dp = 30.dp, textStart: Dp = 0.dp,
    imageEnd: Dp = 8.dp, onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = start, end = end, top = top, bottom = bottom)
            .background(color = backGroundColor)
    ) {
        Text(
            text = text,
            style = style,
            color = textColor,
            modifier = modifier.padding(start = textStart)
        )
        Image(
            painter = painterResource(id = image),
            contentDescription = stringResource(id = R.string.correct_icon),
            modifier
                .clickable { onClick() }
                .padding(start = 4.dp, top = 18.dp, bottom = 18.dp, end = imageEnd)
                .size(imageSize)

        )
    }
}

@Composable
fun HeaderValueInARow(
    textHeader: String, textValue: String, modifier: Modifier = Modifier, end: Dp = 20.dp,
    textColorHeader: Color = appBlueTitle, textColorValue: Color = appBlueTitle, start: Dp = 10.dp,
    headerStyle: TextStyle = normal14Text400, valueStyle: TextStyle = normal20Text700,
    valueTextAlign: TextAlign = TextAlign.End, top: Dp = 8.dp, bottom: Dp = 0.dp
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
    ) {
        Text(
            text = textHeader,
            style = headerStyle,
            color = textColorHeader,
            modifier = Modifier.weight(1f)
        )

        /* Align below code to right */
        Text(
            text = textValue,
            style = valueStyle,
            color = textColorValue,
            modifier = Modifier.weight(1f),
            textAlign = valueTextAlign
        )
    }
}


@Composable
fun HeaderValueInARowWithTextBelowValue(
    textHeader: String, textColorHeader: Color = appBlueTitle, modifier: Modifier = Modifier,
    textHeaderStyle: TextStyle = normal14Text400, textValue: String, top: Dp = 8.dp,
    textColorValue: Color = appBlueTitle, textValueStyle: TextStyle = normal14Text400,
    textBelowValue: String, textColorBelowValue: Color = appBlueTitle, bottom: Dp = 0.dp,
    textBelowStyle: TextStyle = normal12Text400, start: Dp = 25.dp, end: Dp = 30.dp,
    style: TextStyle = normal14Text400,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = textHeader,
                style = textHeaderStyle,
                color = textColorHeader,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            Text(
                text = textValue,
                style = textValueStyle,
                color = textColorValue,
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.End
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start

            )
            Text(
                text = textBelowValue,
                style = textBelowStyle,
                color = textColorBelowValue,

                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                textAlign = TextAlign.End,
                softWrap = true
            )
        }
    }
}

@Composable
fun HeaderNextRowValue(
    textHeader: String, textValue: String, modifier: Modifier, textColorHeader: Color = appBlack,
    textColorValue: Color = appBlueTitle, rowBackground: Color = Color.Transparent,
    start: Dp = 10.dp, end: Dp = 20.dp, top: Dp = 8.dp, bottom: Dp = 0.dp,
    style: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.robotocondensed_regular)),
        fontWeight = FontWeight(400)
    ),
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(1f)
            .background(color = rowBackground)
    ) {
        Text(
            text = textHeader,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.robotocondensed_regular)),
                fontWeight = FontWeight(400)
            ),
            color = textColorHeader.copy(alpha = 0.5f),
            modifier = Modifier
                .fillMaxSize()
        )
        Text(
            text = textValue,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.robotocondensed_regular)),
                fontWeight = FontWeight(400)
            ),
            color = textColorValue,
            modifier = Modifier
                .fillMaxSize(),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun ImageHeaderRow(
    spaceBeforeImage: Dp = 0.dp, imagePainter: Painter, top: Dp = 0.dp, bottom: Dp = 0.dp,
    start: Dp = 0.dp, end: Dp = 0.dp, textHeader: String = "header", textStyle: TextStyle,
    textColor: Color, imageSize: Dp = 14.dp, contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier
        .size(imageSize)
        .padding(top = top, bottom = bottom, start = start, end = end),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(1.dp)
    ) {
        Spacer(modifier = Modifier.size(spaceBeforeImage)) // Space between image and text

        Image(
            painter = imagePainter,
            contentDescription = null, // Provide a description for accessibility
            modifier = modifier,
            contentScale = contentScale
        )
        Spacer(modifier = Modifier.size(16.dp)) // Space between image and text
        Text(
            text = textHeader,
            style = textStyle,
            color = textColor,
        )
        Spacer(modifier = Modifier.weight(1f)) // Pushes the button to the end of the row

    }
}

@Composable
fun ImageTextButtonRow(
    imagePainter: Painter, textHeader: String, textStyle: TextStyle, textColor: Color,
    buttonText: String, buttonTextColor: Color = Color.White, buttonColor: Color = appDarkTeal,
    buttonTextStyle: TextStyle = normal12Text400, onButtonClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(1.dp)
    ) {
        Image(
            painter = imagePainter,
            contentDescription = null, // Provide a description for accessibility
            modifier = Modifier
                .size(64.dp)
                .weight(1f)
        )
        Spacer(modifier = Modifier.size(16.dp)) // Space between image and text`
        Text(
            text = textHeader,
            style = textStyle,
            color = textColor,
            modifier = Modifier.weight(3f)
        )
        Spacer(modifier = Modifier.weight(1f)) // Pushes the button to the end of the row
        WrapBorderButton(
            text = buttonText,
            modifier = Modifier
                .padding(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 0.dp)
                .weight(3f),
            style = buttonTextStyle,
            shape = RoundedCornerShape(10.dp),
            backgroundColor = buttonColor,
            textColor = buttonTextColor,
        ) { onButtonClick() }
    }
}

@Composable
fun WebViewTopBar(navController: NavHostController, title: String) {
    var backPressedTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(appGray85)
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_icon_blue),
            contentDescription = stringResource(id = R.string.back_icon),
            modifier = Modifier
                .clickable {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressedTime < 2000) {
                        navigateApplyByCategoryScreen(navController)
                    } else {
                        CommonMethods().toastMessage(
                            context = context, toastMsg = "Press back again to go to the home page"
                        )
                        backPressedTime = currentTime
                    }
                }
                .padding(start = 4.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
        )
        Text(
            text = title,
            style = semiBold24Text700,
            color = appBlueTitle,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(start = 12.dp)
        )

        Image(
            painter = painterResource(R.drawable.notification_icon),
            contentDescription = stringResource(id = R.string.notification_icon),
            modifier = Modifier
                .clickable {}
                .padding(top = 16.dp, bottom = 16.dp, start = 20.dp, end = 15.dp)
                .size(width = 20.dp, height = 20.dp)
        )
    }
}

@Composable
fun TextInputLayout(
    text: String = "", textFieldVal: TextFieldValue, onTextChanged: (TextFieldValue) -> Unit,
    onLostFocusValidation: (TextFieldValue) -> Unit, hintText: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default, readOnly: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default, modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier) {

//        var newText: String = text.replace(hintText.toLowerCa11se(Locale.ROOT), "").trim()
        var newText: String = text.replace(hintText.toLowerCase(Locale.ROOT), "").trim()

        // Display hint text only when the field is not focused and empty
        if (isFocused) {
            Text(
                text = hintText,
                color = hintTextColor,
                style = normal18Text400,
                textAlign = TextAlign.Start
            )
            newText = textFieldVal.text.replace(hintText.toLowerCase(Locale.ROOT), "").trim()
            if (newText.isEmpty() || newText.equals("0")) {
                newText = ""
            }
        } else {
            Text(
                text = ""
            )
            newText = textFieldVal.text

            onLostFocusValidation(textFieldVal)
        }
        OutlinedTextField(
            value = textFieldVal, onValueChange = onTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },

            keyboardOptions = keyboardOptions,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.robotocondensed_bold)),
                fontWeight = FontWeight(800),
                color = appBlue
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = appTheme,
                unfocusedBorderColor = appTheme,
                cursorColor = Color.Transparent
            ),
            readOnly = readOnly
        )
    }
}

@Composable
fun TextInputLayoutForTenure(
    text: String = "", onTextChanged: (String) -> Unit, hintText: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default, modifier: Modifier = Modifier,
    readOnly: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier) {

        var newText: String

        // Display hint text only when the field is not focused and empty
        if (isFocused) {
            Text(
                text = hintText, color = hintTextColor, style = normal18Text400,
                textAlign = TextAlign.Start
            )
            newText = text.replace(hintText.lowercase(Locale.ROOT), "").trim()
            if (newText.isEmpty() || newText.equals("0")) {
                newText = ""
            }
            TextFieldValue(newText)
        } else {
            Text(text = "")
            newText = text

        }
        OutlinedTextField(
            value = newText, onValueChange = onTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },

            keyboardOptions = keyboardOptions,
            textStyle = TextStyle(
                textAlign = TextAlign.Center, fontSize = 30.sp, color = appBlue,
                fontFamily = FontFamily(Font(R.font.robotocondensed_bold)),
                fontWeight = FontWeight(800),

                ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = appTheme, unfocusedBorderColor = appTheme,
                cursorColor = cursorColor
            ),
            readOnly = readOnly
        )
    }
}

@Composable
fun FixedTopBottomScreen(
    navController: NavHostController, modifier: Modifier = Modifier, showBottom: Boolean = true,
    showdoubleButton: Boolean = false, showHyperText: Boolean = false,
    showCheckBox: Boolean = false, checkboxState: Boolean = false,
    isSelfScrollable: Boolean = false, checkBoxText: String = stringResource(id = R.string.accept),
    buttonText: String = stringResource(id = R.string.accept),
    backGroudColor: Color = Color.White, onClick2: (() -> Unit)? = null,
    buttonText2: String = stringResource(id = R.string.accept),
    backGroudColorChange: Boolean = true, onClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null, onCheckBoxChange: ((Boolean) -> Unit)? = null,
    buttonStart: Dp = 30.dp, buttonEnd: Dp = 30.dp,buttonTop: Dp = 10.dp,buttonBottom: Dp = 30.dp,
    showBackButton: Boolean = true, pageContent: @Composable () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backGroudColor)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        TopBar(navController, showBackButton, onClick = { onBackClick?.let { onBackClick() } })
        if (isSelfScrollable) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                pageContent()
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                pageContent()
            }
        }
        if (showBottom) {
            if (showHyperText) {
                HyperlinkText(onSahamatiClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sahamati.org.in/"))
                    context.startActivity(intent)
                },
                    onRbiClick = {
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://rbi.org.in/"))
                        context.startActivity(intent)
                    })
            }
            if (showCheckBox) {
                CheckBoxText(
                    textColor = softSteelGray,
                    style = normal12Text400,
                    boxState = checkboxState,
                    text = checkBoxText,
                    onCheckedChange = { isChecked ->
                        onCheckBoxChange?.invoke(isChecked)
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurvedPrimaryButtonFull(
                    text = buttonText,
                    modifier = Modifier
                        .padding(
                            start = buttonStart,
                            end = buttonEnd,
                            top = buttonTop,
                            bottom = buttonBottom
                        )
                        .weight(1f),
                    backgroundColor = if (backGroudColorChange) azureBlue else disableColor,
                    textColor = Color.White
                ) { onClick?.let { onClick() } }
                if (showdoubleButton)
                    CurvedPrimaryButtonFull(
                        text = buttonText2,
                        modifier = Modifier
                            .padding(
                                start = buttonStart,
                                end = buttonEnd,
                                top = buttonTop,
                                bottom = buttonBottom
                            )
                            .weight(1f),
                        backgroundColor = if (backGroudColorChange) azureBlue else disableColor,
                        textColor = Color.White
                    ) { onClick2?.let { onClick2() } }
            }
        }
    }
}

@Composable
fun SpaceTextWithRadio(
    backGroundColor: Color = Color.White, radioOptions: ArrayList<BankItem?>, top: Dp = 15.dp,
    selectedOption: String?, onOptionSelected: (String?) -> Unit
) {
    radioOptions.forEach { data ->
        data?.let {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        data.bankName?.let { onOptionSelected(it) }
                    }
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = top)
                    .background(backGroundColor)
            ) {
                data.imageBank?.let { imageUrl ->
                    val painter = rememberImagePainter(data = imageUrl,
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.bank_icon)
                        }
                    )
                    Image(
                        painter = painter,
                        contentDescription = stringResource(id = R.string.bank_image),
                        modifier = Modifier
                            .size(40.dp)
                            .padding(start = 10.dp)
                    )
                }
                data.bankName?.let { bankName ->
                    Text(
                        text = bankName,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 30.dp, end = 30.dp, bottom = 5.dp),
                        style = bold14Text500,
                        textAlign = TextAlign.Start,
                    )
                }

                RadioButton(
                    selected = (data.bankName == selectedOption),
                    modifier = Modifier.padding(all = 8.dp),
                    onClick = { data.bankName?.let { onOptionSelected(it) } },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = primaryBlue
                    )
                )
            }
        }
    }
}

@Composable
fun StartImageWithText(
    @DrawableRes image: Int = R.drawable.blue_bg_white_arrow, start: Dp = 15.dp, end: Dp = 15.dp,
    text: String = stringResource(id = R.string.share_bank_statements_instantly),
    top: Dp = 25.dp, bottom: Dp = 0.dp
) {
    Row(modifier = Modifier.padding(start = start, end = end, top = top, bottom = bottom)) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            text = text,
            style = normal14Text400,
            color = appBlack,
            modifier = Modifier.padding(start = 15.dp)
        )
    }
}

@Composable
fun InputView(
    value: TextFieldValue, onValueChange: (value: TextFieldValue) -> Unit,
    focusRequester: FocusRequester, onKeyEvent: (KeyEvent) -> Boolean
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = false,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = appTheme, shape = RoundedCornerShape(8.dp))
            .background(shape = RoundedCornerShape(8.dp), color = otpBackground)
            .wrapContentSize()
            .focusRequester(focusRequester)
            .onKeyEvent(onKeyEvent),
        maxLines = 1,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        },
        cursorBrush = SolidColor(appTheme),
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = null)
    )
}

@Composable
fun OtpView(
    textList: List<MutableState<TextFieldValue>>, requestList: List<FocusRequester>
) {
    val otpViewModel: OtpViewModel = viewModel()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 0.dp) //Sugu1
                .padding(top = 30.dp)
                .align(Alignment.TopCenter)
        ) {
            for (i in textList.indices) {
                InputView(
                    value = textList[i].value,
                    onValueChange = { newValue ->
                        // Filter to only allow single digits
                        val filteredValue = newValue.text.filter { it.isDigit() }.take(1)

                        otpViewModel.updateOtpError(null)
                        // Check if the filtered value is empty and handle backspace
                        if (filteredValue.isEmpty() && textList[i].value.text.isNotEmpty()) {
                            textList[i].value = TextFieldValue("")
                            if (i > 0) {
                                requestList[i - 1].requestFocus()
                            }
                            return@InputView
                        }

                        // Set the value and move cursor to the end
                        textList[i].value = TextFieldValue(
                            text = filteredValue,
                            selection = TextRange(filteredValue.length)
                        )

                        // Move focus to the next field if the current one is filled
                        if (filteredValue.length == 1 && i < textList.size - 1) {
                            requestList[i + 1].requestFocus()
                        }
                    },
                    focusRequester = requestList[i],
                    onKeyEvent = { keyEvent ->
                        if (keyEvent.key == Key.Backspace && keyEvent.type == KeyEventType.KeyUp &&
                            textList[i].value.text.isEmpty() && i > 0
                        ) {
                            requestList[i - 1].requestFocus()
                            true
                        } else {
                            false
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun OtpViewPreview (){
    val textList =
        List(6) { remember { mutableStateOf(TextFieldValue(text = "", selection = TextRange(0))) } }
    val requesterList = List(6) { remember { FocusRequester() } }

    OtpView(textList = textList, requestList = requesterList)

}


@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier, textColor: Color = hyperTextColor, start: Dp = 8.dp,
    end: Dp = 8.dp, top: Dp = 8.dp, bottom: Dp = 8.dp, style: TextStyle = normal14Text500,
    onSahamatiClick: () -> Unit, onRbiClick: () -> Unit
) {
    val annotatedString = buildAnnotatedString {

        append("visit ")

        pushStringAnnotation(tag = "URL", annotation = "sahamati")
        withStyle(style = SpanStyle(color = textColor)) { append("Sahamati") }

        pop()
        append(" or ")

        pushStringAnnotation(tag = "URL", annotation = "rbi")
        withStyle(style = SpanStyle(color = textColor)) { append("RBI") }

        pop()
        append(" website to know more")
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 5.dp, end = 5.dp)
    ) {
        ClickableText(
            text = annotatedString,
            style = style.copy(color = Color.Black),
            modifier = modifier
                .padding(start = start, end = end, top = top, bottom = bottom),
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        when (annotation.item) {
                            "sahamati" -> onSahamatiClick()
                            "rbi" -> onRbiClick()
                        }
                    }
            }
        )
    }
}

@Composable
fun TopBar(
    navController: NavHostController, showBackButton: Boolean = true,
    showNotificationIcon: Boolean = true, onClick: () -> Unit = { navController.popBackStack() }
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(appGray85)
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp)
    ) {
        if (showBackButton) {
            Image(
                painter = painterResource(id = R.drawable.back_icon_blue),
                contentDescription = stringResource(id = R.string.back_icon),
                modifier = Modifier
                    .clickable {
                        onClick()
                    }
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(40.dp))
        }

        Image(
            painter = painterResource(R.drawable.app_logo),
            contentDescription = stringResource(id = R.string.app_logo),
            modifier = Modifier
                .size(width = 70.dp, height = 50.dp)
                .align(Alignment.CenterVertically)
        )

        if (showNotificationIcon) {
            /*Image(
                painter = painterResource(R.drawable.notification_icon),
                contentDescription = stringResource(id = R.string.notification_icon),
                modifier = Modifier
                    .clickable {}
                    .padding(top = 16.dp, bottom = 16.dp, start = 20.dp, end = 15.dp)
                    .size(width = 20.dp, height = 20.dp)
            )*/
            Text("")
        }
    }
}

@Composable
fun GstTransactionCard(
    boxState: Boolean = true, showCheckBox: Boolean = true, start: Dp = 8.dp, end: Dp = 0.dp,
    top: Dp = 8.dp, bottom: Dp = 8.dp, onCheckedChange: ((Boolean) -> Unit), onClick: () -> Unit,
    invoiceData: InvoicesItem?
) {
    Row(
        modifier = Modifier
            .padding(start = start, top = top, bottom = bottom, end = end)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .weight(1f)
                .clickable { onClick() }
                .fillMaxWidth(),
            elevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    invoiceData?.ctin?.let { ctin ->
                        Text(
//                        text = "Padmavati Steel Corporation Pvt. Ltd.",
                            text = "$ctin",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
//                    invoiceData?.ctin?.let { ctin ->
//                        invoiceData.idt?.let { date ->
//                            val dateString = date
//                            val actualDate = dateString.split("T")[0]
//                            Text(
//                                text = "$actualDate • $ctin",
//                                color = Color.Gray,
//                                fontSize = 12.sp
//                            )
//                        }
//                    }
                    invoiceData?.inum?.let { inum ->
                        invoiceData.idt?.let { date ->
                            val actualDate = CommonMethods().editingDate(date = date)
                            Text(
                                text = "$actualDate • $inum",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                invoiceData?.value?.let { inVoiceAmount ->
                    Text(
                        text = "₹ $inVoiceAmount",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = stringResource(id = R.string.forward_logo),
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        if (showCheckBox) {
            CheckBoxNoText(boxState = boxState, onCheckedChange = onCheckedChange, start = 0.dp)
        }
    }
}

@Composable
fun OnlyClickAbleText(
    textHeader: String, textValue: String, modifier: Modifier = Modifier, end: Dp = 20.dp,
    textColorHeader: Color = appBlueTitle, textColorValue: Color = appBlueTitle, start: Dp = 10.dp,
    top: Dp = 8.dp, bottom: Dp = 0.dp, style: TextStyle = normal14Text400, onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
    ) {
        Text(
            text = textHeader,
            style = style,
            color = textColorHeader,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = textValue,
            style = style,
            color = textColorValue,
            modifier = Modifier
                .weight(1f)
                .clickable { onClick() },
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun ClickableHeaderValueInARow(
    textHeader: String, textValue: String, modifier: Modifier = Modifier, end: Dp = 20.dp,
    textColorHeader: Color = appBlueTitle, textColorValue: Color = appBlueTitle, start: Dp = 10.dp,
    headerStyle: TextStyle = normal14Text400, valueStyle: TextStyle = normal20Text700,
    valueTextAlign: TextAlign = TextAlign.End, onClick: () -> Unit, top: Dp = 8.dp,
    bottom: Dp = 0.dp,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
    ) {
        Text(
            text = textHeader,
            style = headerStyle,
            color = textColorHeader,
            modifier = Modifier.weight(1f)
        )

        /* Align below code to right */
        Text(
            text = textValue,
            style = valueStyle,
            color = textColorValue,
            modifier = Modifier
                .weight(1f)
                .clickable { onClick() },
            textAlign = valueTextAlign
        )
    }
}

@Composable
fun ClickHeaderValueInARowWithTextBelowValue(
    textHeader: String, textColorHeader: Color = appBlueTitle, textValue: String,
    textHeaderStyle: TextStyle = normal14Text400, textColorValue: Color = appBlueTitle,
    textValueStyle: TextStyle = normal14Text400, textBelowValue: String, start: Dp = 25.dp,
    end: Dp = 30.dp, textBelowStyle: TextStyle = normal12Text400, modifier: Modifier = Modifier,
    textColorBelowValue: Color = appBlueTitle, top: Dp = 8.dp, bottom: Dp = 0.dp,
    style: TextStyle = normal14Text400, onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = textHeader,
                style = textHeaderStyle,
                color = textColorHeader,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            Text(
                text = textValue,
                style = textValueStyle,
                color = textColorValue,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClick() },
                textAlign = TextAlign.End
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start

            )
            Text(
                text = textBelowValue,
                style = textBelowStyle,
                color = textColorBelowValue,

                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                textAlign = TextAlign.End,
                softWrap = true
            )
        }
    }
}

@Composable
fun ContinueText(
    startText: String, endText: String, start: Dp = 0.dp, end: Dp = 0.dp, top: Dp = 0.dp,
    bottom: Dp = 0.dp,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = start, end = end, top = top, bottom = bottom),
        horizontalArrangement = Arrangement.Start
    ) {
        StartingText(text = startText, modifier = Modifier.weight(1f), start = 5.dp)
        StartingText(
            text = endText, modifier = Modifier.weight(1f), start = 5.dp, style = bold14Text500,
        )
    }
}

@Composable
fun FixedHeaderBottomScreen(
    showDoubleButton: Boolean = false, showHyperText: Boolean = false, showBottom: Boolean = false,
    modifier: Modifier = Modifier, showCheckBox: Boolean = false, checkboxState: Boolean = false,
    checkBoxText: String = stringResource(id = R.string.accept), onClick2: (() -> Unit)? = null,
    buttonText: String = stringResource(id = R.string.accept), isSelfScrollable: Boolean = false,
    title: String = stringResource(R.string.accept), backGroundColorChange: Boolean = true,
    buttonText2: String = stringResource(id = R.string.accept), onClick: (() -> Unit)? = null,
    onHambergurClick: (() -> Unit)? = null, onCheckBoxChange: ((Boolean) -> Unit)? = null,
    onNotificationClick: (() -> Unit)? = null, pageContent: @Composable () -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Box {
            Box {
                HeaderCard {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.back_icon),
                            contentDescription = stringResource(R.string.hamburger_menu),
                            modifier = Modifier
                                .clickable { onHambergurClick?.let { onHambergurClick() } }
                                .padding(15.dp)
                                .size(width = 30.dp, height = 30.dp)
                        )
                        Text("")
                        /*Image(
                            painter = painterResource(R.drawable.notifications),
                            contentDescription = stringResource(R.string.hamburger_menu),
                            modifier = Modifier
                                .clickable { onNotificationClick?.let { onNotificationClick() } }
                                .padding(15.dp)
                                .size(width = 20.dp, height = 20.dp)
                        )*/
                    }
                    RegisterText(
                        text = title, bottom = 60.dp, textColor = Color.White
                    )
                }
            }
            Box(modifier = Modifier.padding(top = 170.dp)) {
                HeaderCard(
                    topStart = 10.dp, topEnd = 10.dp, bottomStart = 0.dp, bottomEnd = 0.dp,
                    start = 20.dp, end = 20.dp, top = 0.dp,
                    cardColor = Color.White, borderColor = Color.White
                ) {
                    Spacer(modifier = Modifier.padding(16.dp))
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            HeaderCard(
                bottomStart = 10.dp, bottomEnd = 10.dp, start = 20.dp, end = 20.dp,
                top = 0.dp,
                bottom = 10.dp,
                cardColor = Color.White, borderColor = Color.White
            ) {
                if (isSelfScrollable) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
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
        }

        if (showBottom) {
            if (showHyperText) {
                HyperlinkText(onSahamatiClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sahamati.org.in/"))
                    context.startActivity(intent)
                },
                    onRbiClick = {
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://rbi.org.in/"))
                        context.startActivity(intent)
                    })
            }
            if (showCheckBox) {
                CheckBoxText(
                    textColor = softSteelGray,
                    style = normal12Text400,
                    boxState = checkboxState,
                    text = checkBoxText,
                    onCheckedChange = { isChecked ->
                        onCheckBoxChange?.invoke(isChecked)
                    }
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, top = 3.dp, bottom = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button (
                    onClick = { onClick?.let { onClick() } },
                    colors = ButtonDefaults.buttonColors(newWhiteColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = buttonText,
                        color = newRedColor,
                        fontSize = 16.sp,
                        modifier = modifier.padding(5.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                if (showDoubleButton){
                    Button(
                        onClick = { onClick2?.let { onClick2() } },
                        colors = ButtonDefaults.buttonColors(newBlueColor),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = buttonText2,
                            color = newWhiteColor,
                            fontSize = 16.sp,
                            modifier = modifier.padding(5.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
