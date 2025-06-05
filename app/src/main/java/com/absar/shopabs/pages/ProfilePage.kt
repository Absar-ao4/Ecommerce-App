package com.absar.shopabs.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.absar.shopabs.R
import com.absar.shopabs.GlobalNavigation
import com.absar.shopabs.ui.theme.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    val user = Firebase.auth.currentUser
    var userName by remember { mutableStateOf("Guest") }
    var showEditProfile by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(userName) }
    val email = user?.email ?: ""

    LaunchedEffect(user?.uid) {
        user?.uid?.let { uid ->
            Firebase.firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    doc.getString("name")?.let { name ->
                        userName = name.split(" ").first()
                        editedName = name
                    }
                }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGrey)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = "Profile Avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(White)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = userName,
                    color = White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = email,
                    color = LightGray,
                    fontSize = 14.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStatItem(icon = Icons.Default.ShoppingCart, title = "Orders", value = "12")
            ProfileStatItem(icon = Icons.Default.Favorite, title = "Favourite", value = "7")
            ProfileStatItem(icon = Icons.Default.Star, title = "Reviews", value = "4.8")
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            ProfileActionButton(
                icon = Icons.Default.Edit,
                text = "Edit Profile",
                onClick = { showEditProfile = true }
            )
            ProfileActionButton(
                icon = Icons.Default.ShoppingBag,
                text = "Orders",
                onClick = { GlobalNavigation.navController.navigate("ord") }
            )
            ProfileActionButton(
                icon = Icons.Default.LocationOn,
                text = "Addresses",
                onClick = { GlobalNavigation.navController.navigate("address") }
            )
            ProfileActionButton(
                icon = Icons.Default.Notifications,
                text = "Notifications",
                onClick = { GlobalNavigation.navController.navigate("notify") }
            )
            ProfileActionButton(
                icon = Icons.Default.Settings,
                text = "Settings",
                onClick = { /* TODO */ }
            )
        }

        Button(
            onClick = {
                Firebase.auth.signOut()
                GlobalNavigation.navController.navigate("auth")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ElectricPurple,
                contentColor = White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Logout", fontSize = 16.sp)
        }
    }

    if (showEditProfile) {
        EditProfileDialog(
            currentName = editedName,
            currentEmail = email,
            onDismiss = { showEditProfile = false },
            onSave = { newName ->
                user?.uid?.let { uid ->
                    Firebase.firestore.collection("users").document(uid)
                        .update("name", newName)
                        .addOnSuccessListener {
                            userName = newName.split(" ").first()
                            editedName = newName
                        }
                }
            }
        )
    }
}

@Composable
fun EditProfileDialog(
    currentName: String,
    currentEmail: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardGrey
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Edit Profile",
                        color = White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name", color = White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedLabelColor = LightGray,
                        unfocusedLabelColor = LightGray,
                        focusedIndicatorColor = White,
                        unfocusedIndicatorColor = LightGray
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedLabelColor = LightGray,
                        unfocusedLabelColor = LightGray,
                        focusedIndicatorColor = White,
                        unfocusedIndicatorColor = LightGray
                    ),
                    enabled = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onSave(name)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricPurple,
                        contentColor = White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save Changes", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun ProfileStatItem(icon: ImageVector, title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = White,
            modifier = Modifier.size(32.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(value, color = White, fontWeight = FontWeight.Bold)
        Text(title, color = LightGray, fontSize = 12.sp)
    }
}

@Composable
fun ProfileActionButton(icon: ImageVector, text: String, onClick: () -> Unit) {

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        border = BorderStroke(.5.dp, ElectricPurple),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = CardGrey,
            contentColor = White
        ),
        shape = RoundedCornerShape(8.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.padding(end = 16.dp),
                tint = White
            )
            Text(text, fontSize = 16.sp)
        }
    }
}
