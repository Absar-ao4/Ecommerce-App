package com.absar.shopabs.pages
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.absar.shopabs.ui.theme.ElectricPurple

@Composable
fun SettingsPage(
    onBack: () -> Unit,
    onContactSupport: () -> Unit = {},
    onOpenPrivacyPolicy: () -> Unit = {},
    appVersion: String = "1.0.0"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = ElectricPurple
                )
            }
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "About",
            color = Color(0xFFBE52F2),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "ShopAbs is a modern shopping app with a smooth UI, secure payments, and real-time updates. The app is currently under demo and the payments can be fulfilled with demo card details and otps feel free to use the app and do give a feedback!\nThank You",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onContactSupport() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "Contact Support",
                tint = Color.White
            )
            Text(
                text = "Contact Support",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Divider(color = Color.Gray.copy(alpha = 0.3f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenPrivacyPolicy() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Privacy Policy",
                tint = Color.White
            )
            Text(
                text = "Privacy Policy",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Divider(color = Color.Gray.copy(alpha = 0.3f))

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "App Version $appVersion",
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
