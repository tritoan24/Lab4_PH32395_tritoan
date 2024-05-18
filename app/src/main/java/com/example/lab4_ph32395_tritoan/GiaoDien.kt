package com.example.lab4_ph32395_tritoan

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class PaymentMethod(
    val name: String, val iconResId: Int, val color: Color
)

class GiaoDien : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()

            var selectedMethod by remember { mutableStateOf<PaymentMethod?>(null) }

            // Tạo 1 list gồm các PaymentMethod component là các item row chứa: Ảnh, tên phương thức, radiobutton tích chọn
            val paymentMethods = listOf(
                PaymentMethod("PayPal", R.drawable.paypal, Color(0xFFFFA726)),
                PaymentMethod("Momo", R.drawable.ic_momo, Color(0xFFF48FB1)),
                PaymentMethod("Zalo Pay", R.drawable.ic_zalopay, Color(0xFF81D4FA)),
                PaymentMethod("Thanh toán trực tiếp", R.drawable.cash, Color(0xFF80CBC4))
            )

            Scaffold(
                topBar = {
                    TopToolbar()
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                },
                bottomBar = {
                    BottomNavigationBar()
                }
            ) {
                PaymentMethodList(
                    paymentMethods = paymentMethods,
                    selectedMethod = selectedMethod,
                    onMethodSelected = { method ->
                        selectedMethod = method
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Selected Payment Method: ${method.name}")
                        }
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopToolbar() {
        TopAppBar(
            title = {
                Text(
                    text = "Thanh Toán",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* Xử lý nút quay lại */ }) {
                    Icon(painterResource(id = R.drawable.ic_back), contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Xử lý hành động khác */ }) {
                    Icon(painterResource(id = R.drawable.menu), contentDescription = "Other Action")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                navigationIconContentColor = Color.Black,
                actionIconContentColor = Color.Black
            )
        )
    }

    @Composable
    fun AddressSection() {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nguyễn Trí Toán", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "số 7,ngõ 322/118 nhân mỹ",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Mỹ Đình - Nam Từ Liêm", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Thành phố Hà Nội",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    @Composable
    fun PaymentMethodList(
        paymentMethods: List<PaymentMethod>,
        selectedMethod: PaymentMethod?,
        onMethodSelected: (PaymentMethod) -> Unit
    ) {
        Column {
            Text(
                text = "Địa chỉ nhận hàng",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )
            AddressSection()

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Vui lòng chọn phương thức thanh toán:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(7.dp)
            )

            LazyColumn {
                items(paymentMethods) { method ->
                    PaymentMethodItem(
                        paymentMethod = method,
                        selected = selectedMethod == method,
                        onSelected = { onMethodSelected(method) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle next step */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.black))
            ) {
                Text(text = "Tiếp theo")
            }
        }
    }
    @Composable
    fun PaymentMethodItem(
        paymentMethod: PaymentMethod,
        selected: Boolean,
        onSelected: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(8.dp, RoundedCornerShape(8.dp)) // Thêm đổ bóng
                .background(paymentMethod.color, RoundedCornerShape(8.dp))
                .clickable { onSelected() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = paymentMethod.iconResId),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = paymentMethod.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            RadioButton(
                selected = selected,
                onClick = { onSelected() }
            )
        }
    }

    @Composable
    fun BottomNavigationBar() {
        val items = listOf(
            BottomNavItem("Trang chủ", R.drawable.ic_home),
            BottomNavItem("Lịch sử", R.drawable.ic_history),
            BottomNavItem("Giỏ hàng", R.drawable.ic_cart),
            BottomNavItem("Hồ sơ", R.drawable.ic_profile)
        )
        var selectedItem by remember { mutableStateOf(0) }

        NavigationBar(
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = item.iconResId),
                            contentDescription = item.title
                        )
                    },
                    label = { Text(item.title) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFFFA726),
                        selectedTextColor = Color(0xFFFFA726),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
            }
        }
    }

    data class BottomNavItem(val title: String, val iconResId: Int)
}
