package com.absar.shopabs.components

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.absar.shopabs.ui.theme.CardGrey
import com.absar.shopabs.ui.theme.DarkGrey
import com.absar.shopabs.ui.theme.ElectricPurple
import com.absar.shopabs.ui.theme.White
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun AddressSection() {
    val user = Firebase.auth.currentUser
    var address by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchedEffect(user?.uid) {
        if (user != null) {
            Firebase.firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { doc ->
                    address = doc.getString("address") ?: ""
                }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkGrey,
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkGrey)
                    .statusBarsPadding()
                    .padding(8.dp)
            ) {
                BackButton(
                    onBack = { dispatcher?.onBackPressed() },
                    tint = ElectricPurple
                )
                Text(
                    "Address",
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardGrey,
                    contentColor = White
                ),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Address") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = ElectricPurple,
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = { isEditing = false },
                                modifier = Modifier.padding(end = 8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White
                                ),
                                border = BorderStroke(1.dp, ElectricPurple)
                            ) {
                                Text("Cancel", color = Color.Black)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                onClick = {
                                    if (user != null) {
                                        Firebase.firestore.collection("users")
                                            .document(user.uid)
                                            .update("address", address)
                                            .addOnSuccessListener {
                                                isEditing = false
                                            }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(ElectricPurple)
                            ) {
                                Text("Save")
                            }
                        }
                    } else {
                        Text(
                            text = address.ifEmpty { "No address saved" },
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 16.sp,
                            color = White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = { isEditing = true },
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(ElectricPurple)
                        ) {
                            Text("Edit Address")
                        }
                    }
                }
            }
        }
    }
}
