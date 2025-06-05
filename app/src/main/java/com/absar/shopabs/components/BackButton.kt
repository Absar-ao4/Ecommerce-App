package com.absar.shopabs.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.absar.shopabs.ui.theme.ElectricPurple

@Composable
fun BackButton(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = ElectricPurple
) {
    IconButton(
        onClick = onBack,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back",
            tint = tint
        )
    }
}
