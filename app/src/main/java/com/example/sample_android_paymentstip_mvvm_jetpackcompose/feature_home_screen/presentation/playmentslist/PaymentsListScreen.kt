package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation.playmentslist

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.R
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.ui.theme.colorBorderGrey
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.ui.theme.colortextGrey
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.ui.theme.orangeBg
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.PaymentRecordValidatorImpl
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.PaymentsCalculator
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.data.models.local.SavedPaymentEntity
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation.viewmodel.PaymentsViewModel
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation.viewmodel.UIEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun PaymentsListScreen(navigateBack: () -> Unit = {}, showSnackbar: (String) -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            ActionBar(navigateBack)
            TotalAmounts()
            AllPayments(showSnackbar)
        }
    }
}

@Composable
fun ColumnScope.AllPayments(showSnackbar: (String) -> Unit) {
    val viewModel: PaymentsViewModel = hiltViewModel()
    val paymentsDto = viewModel.paymentsDtoObservable.value
    PaymentsList(showSnackbar, paymentsDto.list)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PaymentsList(showSnackbar: (String) -> Unit, list: List<SavedPaymentEntity>) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .matchParentSize()
                .padding(bottom = 32.dp)

        ) {
            val calculator: PaymentsCalculator = PaymentsCalculator(PaymentRecordValidatorImpl())
            this.items(list.size) {
                PaymentItemUI(list[it], calculator, showSnackbar)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaymentItemUI(
    data: SavedPaymentEntity,
    calculator: PaymentsCalculator,
    showSnackbar: (String) -> Unit = {}
) {
    val viewModel: PaymentsViewModel = hiltViewModel()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 24.dp, end = 24.dp)
            .wrapContentHeight()
            .combinedClickable(onClick = {
                showSnackbar("Double click to delete")
            }, onDoubleClick = {
                viewModel.callEvent(UIEvent.DeletePayment(data))
            })
    ) {
        Column(
            Modifier
                .wrapContentWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = convertTimestampToDateString(data.timestamp),
                modifier = Modifier,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Row(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = "$${data.amount}",
                    modifier = Modifier,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tip: $${
                        calculator.calculateTipAmount(
                            data.amount,
                            data.tipPercentage
                        )
                    }",
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 16.sp,
                    color = colortextGrey,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        data.imagePath?.let {
            Box(
                modifier = Modifier
                    .height(54.dp)
                    .width(54.dp)
                    .align(Alignment.CenterEnd)
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = rememberImagePainter(Uri.parse(data.imagePath)),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clipToBounds(),
                )
            }
        }
    }
}

@Composable
fun TotalAmounts() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(orangeBg)
    ) {
        val viewModel: PaymentsViewModel = hiltViewModel()
        val paymentsDto = viewModel.paymentsDtoObservable.value
        Text(
            text = "Total amount: $${paymentsDto.totalAmount}",
            modifier = Modifier.padding(top = 4.dp, start = 24.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Total tip: $${paymentsDto.totalTip}",
            modifier = Modifier.padding(top = 4.dp, start = 24.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Total person: ${paymentsDto.totalPerson}",
            modifier = Modifier.padding(top = 4.dp, start = 24.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun ActionBar(navigateBack: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .height(66.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = "",
            modifier = Modifier
                .padding(start = 26.dp)
                .align(Alignment.CenterStart)
                .clickable {
                    navigateBack()
                }
        )
        Text(
            text = "Saved Payments",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier
                .background(color = colorBorderGrey)
                .height(1.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}

fun convertTimestampToDateString(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy MMMM dd", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}
