package com.absar.shopabs.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.absar.shopabs.AppUtil
import com.absar.shopabs.GlobalNavigation
import com.absar.shopabs.ui.theme.CardGrey
import com.absar.shopabs.ui.theme.ElectricPurple
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.node.CameraNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader

@Composable
fun Watch3DViewerWithSlider() {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    var rotation by remember { mutableStateOf(0f) }
    var context= LocalContext.current

    val cameraNode = remember {
        CameraNode(engine).apply {
            position = Position(z = 2.5f)
        }
    }

    val modelNode = remember {
        ModelNode(
            modelInstance = modelLoader.createModelInstance("vintage_watch.glb"),
            scaleToUnits = 1.0f
        )
    }

    Column {
        Card(
            modifier = Modifier
                .size(400.dp)
                .padding(4.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardGrey),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Scene(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(8.dp),
                engine = engine,
                modelLoader = modelLoader,
                cameraNode = cameraNode,
                childNodes = listOf(modelNode)
            )
            Slider(
                value = rotation,
                onValueChange = {
                    rotation = it
                    modelNode.rotation = modelNode.rotation.copy(y = rotation)
                },
                valueRange = 0f..360f,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "3in1 Vintage Watch",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "₹4500",
                            fontSize = 14.sp,
                            style = TextStyle(textDecoration = TextDecoration.LineThrough),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "@1999",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                IconButton(
                    onClick = { AppUtil.addItemToCart("Wq9OwRK6eX4ts7Amd8EE", context) }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        tint = ElectricPurple
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .size(400.dp)
                .padding(4.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardGrey),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Scene(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(8.dp),
                engine = engine,
                modelLoader = modelLoader,
                cameraNode = cameraNode,
                childNodes = listOf(modelNode)
            )
            Slider(
                value = rotation,
                onValueChange = {
                    rotation = it
                    modelNode.rotation = modelNode.rotation.copy(y = rotation)
                },
                valueRange = 0f..360f,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "3in1 Vintage Watch",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "₹4500",
                            fontSize = 14.sp,
                            style = TextStyle(textDecoration = TextDecoration.LineThrough),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "@1999",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                IconButton(
                    onClick = { AppUtil.addItemToCart("Wq9OwRK6eX4ts7Amd8EE", context) }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        tint = ElectricPurple
                    )
                }
            }

        }
    }
}
