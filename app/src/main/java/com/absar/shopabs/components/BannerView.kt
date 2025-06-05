package com.absar.shopabs.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

//import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
//import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
//import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType


@Composable
fun BannerView(modifier: Modifier=Modifier)
{
    val accentColor = Color(0xFF1A6A7C)
    var bannerList by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("banners")
            .get().addOnCompleteListener() {
                    bannerList=it.result.get("urls") as List<String>
            }
    }
    Column (modifier = modifier){
        val pagerState = rememberPagerState(0) {
            bannerList.size
        }

        HorizontalPager(state = pagerState, pageSpacing = 24.dp) {
            AsyncImage(
                model = bannerList.get(it),
                contentDescription = "Banner Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
        }
//        Spacer(modifier=modifier.height(10.dp))
//        DotsIndicator(
//            dotCount = bannerList.size,
//            type = ShiftIndicatorType(DotGraphic(
//                color =accentColor ,
//                size = 6.dp
//            )),
//            pagerState = pagerState
//            )
        DotsIndicator(
            pageCount = bannerList.size,
            currentPage = pagerState.currentPage
        )


    }


}

@Composable
fun DotsIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    selectedColor: Color = Color.White,
    unselectedColor: Color = Color.LightGray,
    dotSize: Dp = 10.dp
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { page ->
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(color = if (page == currentPage) selectedColor else unselectedColor)
            )
        }
    }
}
