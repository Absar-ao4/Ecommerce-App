package com.absar.shopabs.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.absar.shopabs.AppUtil
import com.absar.shopabs.viewmodel.AuthViewModel

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel:AuthViewModel=viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var context= LocalContext.current

    var isLoading by remember {
        mutableStateOf(false)
    }


    val darkGray = Color(0xFF2B2B2B)
    val mediumGray = Color(0xFF7D7D7D)
    val accentColor = Color(0xFF1A6A7C)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        darkGray,
                        mediumGray
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Login to ShopAbs",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif,
                            color = darkGray,
                            textAlign = TextAlign.Left
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Enter your credentials to access your account.",
                        style = TextStyle(
                            textAlign = TextAlign.Left,
                            color = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Email",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace,
                            color = darkGray,
                            textAlign = TextAlign.Left
                        )
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = "you@example.com") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Password",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace,
                            color = darkGray,
                            textAlign = TextAlign.Left
                        )
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(text = "********") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            isLoading=true
                            authViewModel.login(email,password){success,errorMessage->
                                if(success) {
                                    isLoading=false
                                    AppUtil.showToast(context,"Welcome to ShopAbs!")
                                    navController.navigate("home")
                                    {
                                        popUpTo("auth"){inclusive=true}
                                    }
                                }
                                else{
                                    isLoading=false
                                    AppUtil.showToast(context,errorMessage?:"Something went wrong")
                                }
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = darkGray)
                    ) {
                        Text(text = if(isLoading)"Loggin in" else "Login", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Don't have an account?",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Left,
                                color = darkGray
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "SignUp!",
                            color = accentColor,
                            modifier = Modifier
                                .clickable { navController.navigate("signup") }
                        )
                    }
                }
            }
        }
    }
}
