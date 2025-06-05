package com.absar.shopabs.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.absar.shopabs.AppUtil
import com.absar.shopabs.GlobalNavigation
import com.absar.shopabs.components.CustomRippleButton
import com.absar.shopabs.model.ProductModel
import com.absar.shopabs.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
 import com.absar.shopabs.ui.theme.DarkGrey
 import com.absar.shopabs.ui.theme.CardGrey
 import com.absar.shopabs.ui.theme.ElectricPurple
 import com.absar.shopabs.ui.theme.SoftViolet
 import com.absar.shopabs.ui.theme.White
 import com.absar.shopabs.ui.theme.LightGray
 import com.absar.shopabs.ui.theme.DividerColor
import com.google.firebase.Firebase

@Composable
fun CheckOutPage(modifier: Modifier = Modifier) {

    val userModel = remember { mutableStateOf(UserModel()) }
    val productList = remember { mutableStateListOf<ProductModel>() }
    val userAddress = remember { mutableStateOf("") }

    var context= LocalContext.current

    val subTotal = remember { mutableStateOf(0f) }
    val discount = remember { mutableStateOf(0f) }
    val tax = remember { mutableStateOf(0f) }
    val total = remember { mutableStateOf(0f) }
    var enablePayButton = remember { mutableStateOf(false) }

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId != null) {
        Firebase.firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    if (doc.contains("address")) {
                        val address = doc.getString("address")
                        if (!address.isNullOrBlank()) {
                            enablePayButton.value = true
                        } else {
                            enablePayButton.value = false
                        }
                    } else {
                        enablePayButton.value = false
                    }
                }
            }
    }


    fun calculate() {
        subTotal.value = 0f
        productList.forEach {
            if (it.actualPrice.isNotEmpty()) {
                val qty = userModel.value.cartItems[it.id] ?: 0
                subTotal.value = subTotal.value + it.actualPrice.toFloat() * qty
            }
        }
        discount.value = subTotal.value * 0.1f
        tax.value = subTotal.value * 0.13f
        total.value = "%.2f".format(subTotal.value - discount.value + tax.value).toFloat()
    }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            com.google.firebase.Firebase.firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    userModel.value = doc.toObject(UserModel::class.java) ?: UserModel()
                    userAddress.value = doc.getString("address") ?: "No address saved"

                    val productIds = userModel.value.cartItems.keys.toList()
                    if (productIds.isNotEmpty()) {
                        com.google.firebase.Firebase.firestore.collection("data")
                            .document("stock").collection("products")
                            .whereIn("id", productIds)
                            .get()
                            .addOnSuccessListener { task ->
                                val resultProducts = task.toObjects(ProductModel::class.java)
                                productList.clear()
                                productList.addAll(resultProducts)
                                calculate()
                            }
                    }
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGrey)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier=Modifier.height(24.dp))
            Text(
                text = "Checkout",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = DividerColor)
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .background(CardGrey)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Shipping Address",
                        style = MaterialTheme.typography.titleMedium,
                        color = White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    IconButton(
                        onClick = { GlobalNavigation.navController.navigate("address") }
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = White
                        )
                    }
                }
                Text(
                    text = userAddress.value,
                    color = SoftViolet,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = DividerColor)
            Spacer(modifier = Modifier.height(16.dp))
            RowCheckOutItems("Subtotal", value = subTotal.value.toString())
            Spacer(modifier = Modifier.height(8.dp))
            RowCheckOutItems("Discount", value = discount.value.toString())
            Spacer(modifier = Modifier.height(8.dp))
            RowCheckOutItems("Tax", value = tax.value.toString())
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = DividerColor)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Amount to pay",
                textAlign = TextAlign.Center,
                color = LightGray
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "₹" + total.value.toString(),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = White
            )
            Spacer(modifier = Modifier.height(16.dp))
             CustomRippleButton(
                 onClick = {
                     if(enablePayButton.value)
                     AppUtil.startPayment(total.value)
                     else
                           AppUtil.showToast(context = context,"Need An Address")},
                 text = "Pay Now",
                 rippleColor = Color(0xFF00FF00)
             )
        }
    }
}

@Composable
fun RowCheckOutItems(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = White
        )
        if (title == "Subtotal")
            Text(
                text = "₹" + value,
                fontSize = 18.sp,
                color = White
            )
        if (title == "Discount")
            Text(
                text = " - ₹" + value,
                fontSize = 18.sp,
                color = White
            )
        if (title == "Tax")
            Text(
                text = " + ₹" + value,
                fontSize = 18.sp,
                color = White
            )
        if (title == "Amount to pay:")
            Text(
                text = " + ₹" + value,
                fontSize = 18.sp,
                color = White
            )
    }
}
