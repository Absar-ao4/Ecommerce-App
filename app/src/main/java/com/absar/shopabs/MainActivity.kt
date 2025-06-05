package com.absar.shopabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.absar.shopabs.AppUtil.moveCartToOrdersAndClear
import com.absar.shopabs.AppUtil.removeFromCart
import com.absar.shopabs.model.ProductModel
import com.absar.shopabs.ui.theme.ShopAbsTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.razorpay.PaymentResultListener

class MainActivity : ComponentActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopAbsTheme {
                @Suppress("UNUSED_PARAMETER")
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    innerPadding
                    AppNavigation(Modifier)
                }
            }
        }
        window.statusBarColor = com.absar.shopabs.ui.theme.DarkGrey.toArgb()
    }

    override fun onPaymentSuccess(p0: String?) {
        AppUtil.showToast(this, "Payment Successful")
        moveCartToOrdersAndClear(this)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        AppUtil.showToast(this,"Payment Failed")
    }
}

