package com.absar.shopabs.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.absar.shopabs.AppUtil
import com.absar.shopabs.GlobalNavigation
import com.absar.shopabs.components.CartItemView
import com.absar.shopabs.model.UserModel
import com.absar.shopabs.ui.theme.DarkGrey
import com.absar.shopabs.ui.theme.DividerColor
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.absar.shopabs.ui.theme.ElectricPurple
import com.absar.shopabs.ui.theme.White


@Composable
fun CartPage(modifier: Modifier=Modifier){

    val userModel = remember {
        mutableStateOf(UserModel())
    }
    val context = LocalContext.current

    DisposableEffect (Unit) {
        var Listener=Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addSnapshotListener {it,_->
                if(it!=null)
                {
                    val result=it.toObject(UserModel::class.java)
                    if(result!=null)
                        userModel.value= result
                }
            }
        onDispose {
            Listener.remove()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGrey)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Your Cart",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = DividerColor)
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn {
                    items(userModel.value.cartItems.toList(), key = { it.first }) { (productId, qty) ->
                        CartItemView(productId = productId, qty = qty)
                    }
                }
            }
            Button(
                onClick = {
                    try {
                        GlobalNavigation.navController.navigate("checkout")
                    } catch (e: Exception) {
                        AppUtil.showToast(context = context, e.toString())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricPurple,
                    contentColor = White
                )
            ) {
                Text(text = "Checkout")
            }
        }
    }
}

