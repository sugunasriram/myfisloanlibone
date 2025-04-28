package com.github.sugunasriram.myfisloanlibone.fis_code.views.igm

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.components.CenterProgress
import com.github.sugunasriram.myfisloanlibone.fis_code.components.DropDownTextField
import com.github.sugunasriram.myfisloanlibone.fis_code.components.FixedTopBottomScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.components.InputField
import com.github.sugunasriram.myfisloanlibone.fis_code.components.RegisterText
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateSignInPage
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToIssueListScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.ImageUpload
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.ImageUploadBody
import com.github.sugunasriram.myfisloanlibone.fis_code.network.model.igm.Images
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.appBlack
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.errorRed
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.normal20Text500
import com.github.sugunasriram.myfisloanlibone.fis_code.ui.theme.primaryBlue
import com.github.sugunasriram.myfisloanlibone.fis_code.utils.CommonMethods
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.igm.CreateIssueViewModel
import java.io.ByteArrayOutputStream

@Composable
fun CreateIssueScreen(
    navController: NavHostController, orderId: String, providerId: String, orderState: String,
    fromFlow: String
) {
    val context = LocalContext.current
    val createIssuePLViewModel: CreateIssueViewModel = viewModel()

    val shortDesc: String? by createIssuePLViewModel.shortDesc.observeAsState("")
    val longDesc: String? by createIssuePLViewModel.longDesc.observeAsState("")
    val onShortDescError: String? by createIssuePLViewModel.shortDescError.observeAsState("")
    val longDescError: String? by createIssuePLViewModel.longDescError.observeAsState("")

    val issueListLoading by createIssuePLViewModel.issueListLoading.collectAsState()
    val issueListLoaded by createIssuePLViewModel.issueListLoaded.collectAsState()
    val issueCategoriesList by createIssuePLViewModel.issueCategories.collectAsState()
    val subIssueLoading by createIssuePLViewModel.subIssueLoading.collectAsState()
    val subIssueLoaded by createIssuePLViewModel.subIssueLoaded.collectAsState()
    val subIssueCategoryList by createIssuePLViewModel.subIssueCategory.collectAsState()
    val imageUploading by createIssuePLViewModel.imageUploading.collectAsState()
    val imageUploaded by createIssuePLViewModel.imageUploaded.collectAsState()
    val imageUploadResponse by createIssuePLViewModel.imageUploadResponse.collectAsState()
    val issueCreating by createIssuePLViewModel.issueCreating.collectAsState()
    val issueCreated by createIssuePLViewModel.issueCreated.collectAsState()
    val categoryError by createIssuePLViewModel.categoryError.collectAsState()
    val subCategoryError by createIssuePLViewModel.subCategoryError.collectAsState()
    val showImageNotUploadedError by createIssuePLViewModel.showImageNotUploadedError.collectAsState()

    val navigationToSignIn by createIssuePLViewModel.navigationToSignIn.collectAsState()

    val showInternetScreen by createIssuePLViewModel.showInternetScreen.observeAsState(false)
    val showTimeOutScreen by createIssuePLViewModel.showTimeOutScreen.observeAsState(false)
    val showServerIssueScreen by createIssuePLViewModel.showServerIssueScreen.observeAsState(false)
    val unexpectedErrorScreen by createIssuePLViewModel.unexpectedError.observeAsState(false)
    val unAuthorizedUser by createIssuePLViewModel.unAuthorizedUser.observeAsState(false)


    val categoryFocus = FocusRequester()
    val subCategoryFocus = FocusRequester()
    val shortDescFocus = FocusRequester()
    val longDescFocus = FocusRequester()

    val loanState = if (orderState.contains("Loan SANCTIONED", ignoreCase = true)) "SANCTIONED"
    else if (orderState.contains(" Loan CONSENT_REQUIRED", ignoreCase = true)) "CONSENT_REQUIRED"
    else if (orderState.contains("Loan Disbursed", ignoreCase = true)) "DISBURSED"
    else "INITIATED"

    val categoryErrorMessage = stringResource(R.string.please_select_category)
    val subCategoryErrorMessage = stringResource(R.string.please_select_sub_category)
    val shortDescErrorMessage = stringResource(R.string.please_enter_short_description)
    val longDescErrorMessage = stringResource(R.string.please_enter_long_description)
    val properShortDescErrorMessage = stringResource(R.string.please_enter_proper_short_description)
    val properLongDescErrorMessage = stringResource(R.string.please_enter_proper_long_description)
    var categorySelectedText by remember { mutableStateOf("") }
    var categoryExpand by remember { mutableStateOf(false) }
    val categoryList: List<String>
    val onCategoryDismiss: () -> Unit = { categoryExpand = false }
    val onCategorySelected: (String) -> Unit = { selectedText ->
        categorySelectedText = selectedText
        createIssuePLViewModel.getIssueWithSubCategories(context, selectedText)
        createIssuePLViewModel.updateCategoryError(null)
    }

    var subCategorySelectedText by remember { mutableStateOf("") }
    var subCategoryExpand by remember { mutableStateOf(false) }
    var subCategoryList by remember { mutableStateOf(listOf<String>()) }
    val onSubCategoryDismiss: () -> Unit = { subCategoryExpand = false }
    val onSubCategorySelected: (String) -> Unit =
        { selectedText -> subCategorySelectedText = selectedText
            createIssuePLViewModel.updateSubCategoryError(null)
        }

    LaunchedEffect(categorySelectedText) {
        if (categorySelectedText.isNotEmpty()) {
            createIssuePLViewModel.getIssueWithSubCategories(context, categorySelectedText)
            subCategorySelectedText = ""
            createIssuePLViewModel.removeImage()
            createIssuePLViewModel.clearShortDesc()
            createIssuePLViewModel.onLongDescriptionChanged("")
        }
    }

    LaunchedEffect(subIssueCategoryList) {
        val updatedSubCategoryList =
            subIssueCategoryList?.data?.subCategories?.mapNotNull { it?.issueSubCategory }
                ?: emptyList()
        subCategoryList = updatedSubCategoryList
    }

    when {
        navigationToSignIn -> navigateSignInPage (navController)
        showInternetScreen -> CommonMethods().ShowInternetErrorScreen(navController)
        showTimeOutScreen -> CommonMethods().ShowTimeOutErrorScreen(navController)
        showServerIssueScreen -> CommonMethods().ShowServerIssueErrorScreen(navController)
        unexpectedErrorScreen -> CommonMethods().ShowUnexpectedErrorScreen(navController)
        unAuthorizedUser -> CommonMethods().ShowUnAuthorizedErrorScreen(navController)
        else -> {
            if (issueListLoading || subIssueLoading || issueCreating) {
                CenterProgress()
            } else {
                if (issueCreated) {
                    navigateToIssueListScreen(
                        navController = navController, orderId = "12345", fromFlow = fromFlow,
                        providerId = "12345", loanState = "No Need", fromScreen = "Create Issue"
                    )
                } else {
                    if (issueListLoaded || subIssueLoaded) {
                        val updatedCategoryList =
                            issueCategoriesList?.data?.mapNotNull { it?.name } ?: emptyList()
                        categoryList = updatedCategoryList

                        FixedTopBottomScreen(
                            navController = navController, isSelfScrollable = false,
                            showBottom = true,
                            buttonText = stringResource(id = R.string.submit),
                            onBackClick = { navController.popBackStack() },
                            onClick = {
                                onCrateIssueButtonClick(
                                    categoryFocus = categoryFocus,
                                    subCategoryFocus = subCategoryFocus,
                                    imageUploadResponse = imageUploadResponse,
                                    context = context,
                                    shortDesc = shortDesc,
                                    longDescFocus = longDescFocus,
                                    longDesc = longDesc,
                                    orderId = orderId,
                                    categorySelectedText = categorySelectedText,
                                    createIssuePLViewModel = createIssuePLViewModel,
                                    subCategorySelectedText = subCategorySelectedText,
                                    providerId = providerId,
                                    loanState = loanState,
                                    shortDescFocus = shortDescFocus,
                                    fromFlow = fromFlow,
                                    categoryErrorMessage = categoryErrorMessage,
                                    subCategoryErrorMessage = subCategoryErrorMessage,
                                    shortDescErrorMessage = shortDescErrorMessage,
                                    longDescErrorMessage = longDescErrorMessage,
                                    properLongDescErrorMessage = properLongDescErrorMessage,
                                    properShortDescErrorMessage = properShortDescErrorMessage
                                )
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.tell_us_about_the_problem_you_are_facing),
                                modifier = Modifier.padding(
                                    start = 20.dp, end = 20.dp, top = 20.dp, bottom = 30.dp
                                ),
                                textAlign = TextAlign.Start, style = normal20Text500,
                                color = primaryBlue
                            )
                            Spacer(modifier = Modifier.height(26.dp))
                            RegisterText(
                                text = stringResource(id = R.string.select_the_appropriate_issue),
                                textColor = appBlack, style = normal20Text500, bottom = 5.dp,
                                textAlign = TextAlign.Start, boxAlign = Alignment.TopStart,
                                start = 20.dp, top = 20.dp,
                            )

                            /* Category */
                            DropDownTextField(
                                selectedText = categorySelectedText,
                                hint = stringResource(id = R.string.category),
                                expand = categoryExpand,
                                setExpand = { categoryExpand = it },
                                itemList = categoryList,
                                onDismiss = onCategoryDismiss,
                                onItemSelected = onCategorySelected,
                                focus = categoryFocus,
                                error = categoryError,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            /* Sub Category */
                            DropDownTextField(
                                selectedText = subCategorySelectedText, expand = subCategoryExpand,
                                hint = stringResource(id = R.string.sub_category),
                                setExpand = { subCategoryExpand = it },
                                itemList = subCategoryList,
                                onDismiss = onSubCategoryDismiss,
                                onItemSelected = onSubCategorySelected,
                                focus = subCategoryFocus,
                                error = subCategoryError
                            )

                            IssuesWithImage(
                                imageUploaded = imageUploaded, imageUploading = imageUploading,
                                context = context, createIssuePLViewModel = createIssuePLViewModel,
                                showImageNotUploadedError = showImageNotUploadedError
                            )
                            DiscriptionFeilds(
                                shortDesc = shortDesc, shortDescFocus = shortDescFocus,
                                onShortDescError = onShortDescError, longDescFocus = longDescFocus,
                                createIssuePLViewModel = createIssuePLViewModel,
                                longDesc = longDesc, longDescError = longDescError
                            )
                        }
                    } else {
                        createIssuePLViewModel.getIssueCategories(context)
                    }
                }
            }
        }
    }
}

@Composable
fun IssuesWithImage(
    imageUploaded: Boolean, imageUploading: Boolean, createIssuePLViewModel: CreateIssueViewModel,
    context: Context,showImageNotUploadedError : Boolean,
) {
    Spacer(modifier = Modifier.height(8.dp))
    UploadImageCard(
        imageLabel = if (showImageNotUploadedError) "Please upload an image" else "Image upload *",
        imageUploaded = imageUploaded,
        imageUploading = imageUploading,
        context = context,
        createIssueViewModel = createIssuePLViewModel,
        cardDataColor = if (showImageNotUploadedError) errorRed else Color.Gray
    )

    RegisterText(
        text = stringResource(id = R.string.describe_the_issue), bottom = 5.dp,
        textColor = appBlack, style = normal20Text500, top = 20.dp, textAlign = TextAlign.Start,
        start = 20.dp, boxAlign = Alignment.TopStart,
    )
}

fun onCrateIssueButtonClick(
    categoryFocus: FocusRequester,
    subCategoryFocus: FocusRequester,
    imageUploadResponse: ImageUpload?,
    shortDescFocus: FocusRequester,
    longDescFocus: FocusRequester,
    shortDesc: String?,
    longDesc: String?,
    context: Context,
    categorySelectedText: String,
    createIssuePLViewModel: CreateIssueViewModel,
    orderId: String,
    subCategorySelectedText: String,
    providerId: String,
    loanState: String,
    fromFlow: String,
    categoryErrorMessage: String,
    subCategoryErrorMessage: String,
    shortDescErrorMessage: String,
    longDescErrorMessage: String,
    properShortDescErrorMessage: String,
    properLongDescErrorMessage: String
) {
    val images = imageUploadResponse?.data ?: emptyList()

    if(categorySelectedText.isEmpty()){
            categoryFocus.requestFocus()
        createIssuePLViewModel.updateCategoryError(categoryErrorMessage)
    }else if(subCategorySelectedText.isEmpty()){
            subCategoryFocus.requestFocus()
        createIssuePLViewModel.updateSubCategoryError(subCategoryErrorMessage)
    } else if(images.isEmpty()){
        createIssuePLViewModel.updateImageNotUploadedErrorMessage()
    }else if(shortDesc.isNullOrEmpty()){
        shortDescFocus.requestFocus()
        createIssuePLViewModel.updateShortDescError(shortDescErrorMessage)
    }else if(hasOnlyInteger(shortDesc.trim())){
        shortDescFocus.requestFocus()
        createIssuePLViewModel.updateShortDescError(properShortDescErrorMessage)
    }else if (longDesc.isNullOrEmpty()){
        longDescFocus.requestFocus()
        createIssuePLViewModel.updateLongDescError(longDescErrorMessage)
    }else if(hasOnlyInteger(longDesc.trim())){
        longDescFocus.requestFocus()
        createIssuePLViewModel.updateLongDescError(properLongDescErrorMessage)
    }else{
        createIssuePLViewModel.updateValidation(
            shortDesc = shortDesc, longDesc = longDesc,
            categorySelectedText = categorySelectedText,
            subCategorySelectedText = subCategorySelectedText,
            image = images, context = context, orderId = orderId,
            providerId = providerId, orderState = loanState,
            fromFlow = fromFlow
        )

    }

}

private fun hasOnlyInteger(input: String): Boolean {
    return input.matches(Regex("^-?\\d+$"))
}

@Composable
fun DiscriptionFeilds(
    shortDesc: String?, shortDescFocus: FocusRequester, onShortDescError: String?,
    longDescFocus: FocusRequester, createIssuePLViewModel: CreateIssueViewModel, longDesc: String?,
    longDescError: String?
) {
    shortDesc?.let { shortDesc ->
        InputField(
            inputText = shortDesc, top = 0.dp, start = 20.dp,
            hint = stringResource(id = R.string.short_description_star),
            end = 20.dp, modifier = Modifier.focusRequester(shortDescFocus),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onNext = { longDescFocus.requestFocus() }),
            onValueChange = {
                createIssuePLViewModel.onShortDescChanged(it)
                createIssuePLViewModel.updateShortDescError(null)
            },
            error = onShortDescError
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    InputField(
        inputText = longDesc.orEmpty(), top = 10.dp, start = 20.dp,
        hint = stringResource(id = R.string.long_description_star), end = 20.dp,
        modifier = Modifier.focusRequester(longDescFocus),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        onValueChange = {
            createIssuePLViewModel.onLongDescriptionChanged(it)
            createIssuePLViewModel.updateLongDescError(null)
        },
        error = longDescError
    )

    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
fun UploadImageCard(
    imageLabel: String, imageUploaded: Boolean, imageUploading: Boolean, context: Context,
    createIssueViewModel: CreateIssueViewModel,cardDataColor : Color
) {

    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var imagesData by remember { mutableStateOf<List<Images>>(emptyList()) }

    // Launcher to pick multiple images from the gallery
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            selectedImageUris = uris.take(3) // Limit to 3 images
            imagesData = uris.map { uri ->
                uri.let {
                    // Convert the image to Base64
                    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    // Assuming bitmapToBase64() is defined elsewhere
                    val base64String = bitmapToBase64(bitmap)
                    val mimeType = context.contentResolver.getType(it) ?: "image/jpeg"
                    Log.d("UploadImageBase64", "mimeType: $base64String")
                    Images(mimetype = mimeType, base64 = base64String)
                }
            }

            // Trigger the image upload API call once imagesData is ready
            if (imagesData.isNotEmpty() && !imageUploading) {
                createIssueViewModel.imageUpload(
                    imageUploadBody = ImageUploadBody(images = imagesData), context = context
                )
            }
        }
    )

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp, bottom = 5.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
        Text(
            text = stringResource(id = R.string.upload_the_issue_photo),
            color = appBlack,
            style = normal20Text500, textAlign = TextAlign.Start,
        )
        Image(
            painter = painterResource(R.drawable.edit_image),
            contentDescription = null,
            modifier = Modifier.clickable {
                launcher.launch("image/*")
            }
        )
    }

    // Render UI based on states
    when {
        imageUploading -> {
            CenterProgress()
        }

        imageUploaded -> {
            // Display uploaded images
            Card(
                border = BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp),
                elevation = 2.dp,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    .size(width = 250.dp, height = 100.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (selectedImageUris.isNotEmpty()) {
                        // Display selected images in a grid or row layout
                        Column(modifier = Modifier.fillMaxSize()) {
                            val rows = selectedImageUris.chunked(3) // Display 3 images per row
                            rows.forEach { row ->
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    row.forEach { uri ->
                                        val bitmap = MediaStore.Images.Media.getBitmap(
                                            context.contentResolver, uri
                                        )
                                        Image(
                                            bitmap = bitmap.asImageBitmap(),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        else -> {
            // Initial state, show upload option
            Card(
                border = BorderStroke(1.dp, color = cardDataColor), shape = RoundedCornerShape(8.dp),
                elevation = 2.dp,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    .size(width = 250.dp, height = 100.dp)
                    .clickable { launcher.launch("image/*") }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.UploadFile, contentDescription = null,
                            tint = cardDataColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = imageLabel, style = MaterialTheme.typography.body2,
                            color = cardDataColor
                        )
                    }
                }
            }
        }
    }
}


fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
    val newWidth: Int
    val newHeight: Int

    if (bitmap.width > bitmap.height) {
        newWidth = maxWidth
        newHeight = (newWidth / aspectRatio).toInt()
    } else {
        newHeight = maxHeight
        newWidth = (newHeight * aspectRatio).toInt()
    }

    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val resizedBitmap = resizeBitmap(bitmap, 800, 600)
    val byteArrayOutputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}

