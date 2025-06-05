package com.absar.shopabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.absar.shopabs.components.AddressSection
import com.absar.shopabs.components.NotificationScreen
import com.absar.shopabs.pages.CategoryProductsPage
import com.absar.shopabs.pages.CheckOutPage
import com.absar.shopabs.pages.OrdersPage
import com.absar.shopabs.pages.ProductDetailsPage
import com.absar.shopabs.pages.SettingsPage
import com.absar.shopabs.screen.AuthScreen
import com.absar.shopabs.screen.HomeScreen
import com.absar.shopabs.screen.LoginScreen
import com.absar.shopabs.screen.ProductSearchScreen
import com.absar.shopabs.screen.SignUpScreen
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier=Modifier)
{
    val navController = rememberNavController()
    GlobalNavigation.navController=navController
    val isLoggedIn=Firebase.auth.currentUser!=null
    var firstPage=if(isLoggedIn)"home" else "auth"

    NavHost(navController=navController, startDestination = firstPage) {

        composable("auth"){
            AuthScreen(modifier,navController)
        }
        composable("login"){
            LoginScreen(modifier,navController)
        }
        composable("signup"){
            SignUpScreen(modifier,navController)
        }
        composable("home"){
            HomeScreen(modifier,navController)
        }
        composable("category-products/{categoryId}"){
            CategoryProductsPage(modifier, categoryId=it.arguments?.getString("categoryId")?:"")
        }
        composable("product-details/{productId}"){
            ProductDetailsPage(modifier, productId=it.arguments?.getString("productId")?:"")
        }
        composable("checkout"){
            CheckOutPage(modifier)
        }
        composable("address"){
            AddressSection()
        }
        composable("notify"){
            NotificationScreen()
        }
        composable("ord"){
            OrdersPage()
        }
        composable("search") {
            ProductSearchScreen(onBack = { navController.popBackStack() })
        }
        composable("set"){
            SettingsPage(
                onBack = { navController.popBackStack() },
                onContactSupport = { },
                onOpenPrivacyPolicy = { },
                appVersion = "1.0.0"
            )
        }

    }

}

object GlobalNavigation{
    lateinit var navController : NavHostController
}