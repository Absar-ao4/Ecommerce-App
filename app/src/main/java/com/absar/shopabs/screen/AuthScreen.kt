package com.absar.shopabs.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.absar.shopabs.R
import com.absar.shopabs.ui.theme.CardGrey
import com.absar.shopabs.ui.theme.DarkGrey
import com.absar.shopabs.ui.theme.ElectricPurple
import com.absar.shopabs.ui.theme.SoftViolet

@Composable
fun AuthScreen(modifier: Modifier = Modifier, navController: NavHostController) {

    val darkGray = Color(0xFF2B2B2B)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to CardGrey,
                        0.2f to DarkGrey,
                        0.8f to DarkGrey,
                        1.0f to DarkGrey
                    )
                )
            )

    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.newlogo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Start your shopping journey now",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Best Ecom App with best prices",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Login",
                    fontSize = 22.sp,
                    color = darkGray,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = { navController.navigate("signup") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(2.dp, ElectricPurple)
            ) {
                Text(
                    text = "SignUp",
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
