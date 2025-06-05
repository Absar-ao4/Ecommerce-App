package com.absar.shopabs.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.absar.shopabs.AppUtil
import com.absar.shopabs.GlobalNavigation
import com.absar.shopabs.model.ProductModel
import com.absar.shopabs.ui.theme.CardGrey
import com.absar.shopabs.ui.theme.ElectricPurple
import com.absar.shopabs.ui.theme.White

@Composable
fun ProductItemView(modifier: Modifier=Modifier, product:ProductModel)
{
    val context= LocalContext.current

    Card(
        modifier=modifier
            .padding(8.dp)
            .clickable {
                GlobalNavigation.navController.navigate("product-details/"+product.id)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardGrey),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column(
            modifier=Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.title,
                modifier=Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )

            Text(text = product.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp),
                color= White
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Column {
                    Text(text = "₹"+product.price,
                        fontSize = 10.sp,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        color= White
                    )
                    Spacer(modifier=Modifier.height(1.dp))
                    Text(text = "₹"+product.actualPrice,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color= White
                    )
                }

                Spacer(modifier=Modifier.weight(1f))

                IconButton(onClick = {AppUtil.addItemToCart(product.id,context)}) {
                    Icon(imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        tint = ElectricPurple
                    )
                }

            }

        }
    }
}