package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation.addpayment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.BuildConfig
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.R
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.ui.theme.colorBorderGrey
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.ui.theme.colorButtonGrey
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.ui.theme.colorOrange
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.ui.theme.colorOrange2
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation.viewmodel.AddPaymentStates
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation.viewmodel.PaymentsViewModel
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation.viewmodel.UIEvent
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun AddPaymentScreen(
    navigateToSavedPayments: () -> Unit = {},
    showSnackbar: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            AppBarComponent {
                navigateToSavedPayments()
            }
            AmountComponent()
            PeopleComponent()
            TipComponent()
            TotalCalculationComponent()
        }
        SavePaymentComponent(showSnackbar, navigateToSavedPayments)
    }
}

@Composable
fun AppBarComponent(navigateToSavedPayments: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_tip_jar),
            contentDescription = "",
            modifier = Modifier.align(Alignment.Center)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_history),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    navigateToSavedPayments()
                }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AmountComponent() {
    val viewModel: PaymentsViewModel = hiltViewModel()
    Text(
        text = "Enter Amount",
        modifier = Modifier.padding(top = 33.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )

    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .height(80.dp)
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = colorBorderGrey
            )
            .fillMaxWidth()
            .height(82.dp)
    ) {
        Text(
            text = "$",
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterStart),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        TextField(
            value = viewModel.inputPaymentDtoObservable.value.amount.toString(),
            onValueChange = {
                viewModel.callEvent(UIEvent.ChangeAmount(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 42.sp
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "0.0",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = colorBorderGrey
                )
            }
        )
    }
}

@Preview
@Composable
fun PeopleComponent() {
    val viewModel: PaymentsViewModel = hiltViewModel()
    Text(
        text = "How many people?",
        modifier = Modifier.padding(top = 33.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )

    Box(
        modifier = Modifier
            .padding(top = 33.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        OutlinedButton(
            border = BorderStroke(1.dp, color = colorButtonGrey),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .height(71.dp)
                .width(71.dp),
            onClick = {
                viewModel.callEvent(UIEvent.DecreaseNumPersons)
            }
        ) {
            Text(
                text = "-", fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = colorOrange
            )
        }
        Text(
            text = viewModel.inputPaymentDtoObservable.value.numPersons.toString(),
            modifier = Modifier
                .align(Alignment.Center),
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold
        )
        OutlinedButton(
            border = BorderStroke(1.dp, color = colorButtonGrey),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(71.dp)
                .width(71.dp),
            onClick = {
                viewModel.callEvent(UIEvent.IncreaseNumPersons)
            }
        ) {
            Text(
                text = "+", fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = colorOrange
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TipComponent() {
    val viewModel: PaymentsViewModel = hiltViewModel()
    Text(
        text = "% TIP",
        modifier = Modifier.padding(top = 33.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )

    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .height(80.dp)
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = colorBorderGrey
            )
            .fillMaxWidth()
            .height(82.dp)
    ) {
        Text(
            text = "%",
            modifier = Modifier
                .padding(end = 20.dp)
                .align(Alignment.CenterEnd),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        TextField(
            value = viewModel.inputPaymentDtoObservable.value.tipPercent.toString(),
            onValueChange = {
                viewModel.callEvent(UIEvent.ChangeTipPercentage(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 42.sp
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "0",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = colorBorderGrey
                )
            }
        )
    }
}

@Preview
@Composable
fun TotalCalculationComponent() {
    val viewModel: PaymentsViewModel = hiltViewModel()
    Box(
        modifier = Modifier
            .padding(top = 33.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = "Total Tip",
            modifier = Modifier
                .align(Alignment.CenterStart),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "$${viewModel.inputCalculationsObservable.value.totalTipAmount}",
            modifier = Modifier
                .align(Alignment.CenterEnd),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }

    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = "Per Person",
            modifier = Modifier
                .align(Alignment.CenterStart),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "$${viewModel.inputCalculationsObservable.value.perPersonTipAmount}",
            modifier = Modifier
                .align(Alignment.CenterEnd),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun BoxScope.SavePaymentComponent(
    showSnackbar: (String) -> Unit = {},
    navigateToSavedPayments: () -> Unit = {}
) {
    val viewModel: PaymentsViewModel = hiltViewModel()
    val addPaymentState = rememberUpdatedState(newValue = viewModel.addPaymentStateObservable.value)
    LaunchedEffect(addPaymentState.value) {
        when (addPaymentState.value) {
            is AddPaymentStates.Error -> {
                showSnackbar((addPaymentState.value as AddPaymentStates.Error).errorMessage)
            }
            is AddPaymentStates.Added -> {
                //show animation and move to payments list screen
                navigateToSavedPayments()
            }
            is AddPaymentStates.Reset -> {
                //nothing
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .align(alignment = Alignment.BottomCenter)
            .padding(start = 24.dp, end = 24.dp)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .width(31.dp)
                    .height(31.dp)
                    .border(
                        width = 1.dp,
                        color = if (viewModel.inputPaymentDtoObservable.value.isAttachPhotoEnabled) {
                            colorOrange
                        } else {
                            colorBorderGrey
                        },
                        shape = RoundedCornerShape(7.dp)
                    )
                    .clickable {
                        viewModel.callEvent(UIEvent.ToggleTakePhotoCheckbox)
                    }
            ) {
                Image(
                    painterResource(id = R.drawable.check_tick),
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.Center),
                    colorFilter = ColorFilter.tint(
                        if (viewModel.inputPaymentDtoObservable.value.isAttachPhotoEnabled) {
                            colorOrange
                        } else {
                            colorBorderGrey
                        }
                    )
                )
            }
            Text(
                text = "Take photo of receipt",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        val context = LocalContext.current
        val file = context.createImageFile()
        val uri = FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            BuildConfig.APPLICATION_ID + ".provider", file
        )
        var capturedImageUri by remember {
            mutableStateOf<Uri>(Uri.EMPTY)
        }
        val cameraLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
                if (it) {
                    capturedImageUri = uri
                    //got the captured image
                    viewModel.savePaymentWithImage(capturedImageUri)
                } else {
                    viewModel.onTakePhotoError()
                }
            }
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                cameraLauncher.launch(uri)
            } else {
                viewModel.onTakePhotoError()
            }
        }
        Button(
            onClick = {
                if (viewModel.inputPaymentDtoObservable.value.isAttachPhotoEnabled) {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(uri)
                    } else {
                        // Request a permission
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                } else {
                    viewModel.callEvent(UIEvent.SavePayment)
                }
            },
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, bottom = 24.dp)
                .height(48.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorOrange, colorOrange2
                        ),
                        start = Offset(81.75f, 24f),
                        end = Offset(245.25f, 24f)
                    ), shape = RoundedCornerShape(12.dp)
                )

        ) {
            Text(text = "Save payment", fontSize = 16.sp)
        }
    }
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}
