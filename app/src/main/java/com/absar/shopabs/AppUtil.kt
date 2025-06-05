package com.absar.shopabs

import android.app.Activity
import android.app.Notification.MessagingStyle.Message
import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.SetOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.razorpay.Checkout
import org.json.JSONObject
import kotlin.math.roundToInt



object AppUtil {
    fun showToast(context: Context,message: String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

    fun addItemToCart(productId:String,context: Context){
        val userDoc= Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if(it.isSuccessful)
            {
                val currentCart=it.result.get("cartItems") as? Map<String,Long> ?: emptyMap()
                val currentQuantity=currentCart[productId]?:0
                val updatedQuantity=currentQuantity+1;

                val updatedCart = mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart)
                    .addOnCompleteListener {
                        if(it.isSuccessful)
                        {
                            showToast(context,"Item added to cart!")
                        }
                        else{
                            showToast(context,"Failed in adding item to cart!")
                        }
                    }
            }
        }

    }


    fun addItemToFav(productId: String, context: Context) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnSuccessListener { doc ->
            val currentFav = doc.get("favItems") as? Map<String, Long> ?: emptyMap()
            val updatedFav = currentFav.toMutableMap()
            updatedFav[productId] = 1L

            userDoc.set(mapOf("favItems" to updatedFav), SetOptions.merge())
                .addOnSuccessListener {
                    showToast(context, "Item added to favourites!")
                }
                .addOnFailureListener {
                    showToast(context, "Failed in adding item to favourites!")
                }
        }.addOnFailureListener {
            showToast(context, "Failed to access user data!")
        }
    }

    fun removeFromCart(productId:String,context: Context,removeAll:Boolean=false){
        val userDoc= Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if(it.isSuccessful)
            {
                val currentCart=it.result.get("cartItems") as? Map<String,Long> ?: emptyMap()
                val currentQuantity=currentCart[productId]?:0
                val updatedQuantity=currentQuantity-1;

                val updatedCart =
                    if(updatedQuantity<=0||removeAll)
                        mapOf("cartItems.$productId" to FieldValue.delete())
                    else
                        mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart)
                    .addOnCompleteListener {
                        if(it.isSuccessful)
                        {
                            showToast(context,"Item removed from cart!")
                        }
                        else{
                            showToast(context,"Failed in removing item from cart!")
                        }
                    }
            }
        }
    }

    fun removeFromFav(productId: String, context: Context, removeAll: Boolean = false) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnSuccessListener { doc ->
            val currentFav = doc.get("favItems") as? Map<String, Long> ?: emptyMap()
            val updatedFav = currentFav.toMutableMap()
            updatedFav.remove(productId)

            if (updatedFav.isEmpty()) {
                userDoc.update(mapOf("favItems" to FieldValue.delete()))
                    .addOnSuccessListener {
                        showToast(context, "Item removed from favourites!")
                    }
                    .addOnFailureListener {
                        showToast(context, "Failed in removing item from favourites!")
                    }
            } else {
                userDoc.update(mapOf("favItems.$productId" to FieldValue.delete()))
                    .addOnSuccessListener {
                        showToast(context, "Item removed from favourites!")
                    }
                    .addOnFailureListener {
                        showToast(context, "Failed in removing item from favourites!")
                    }
            }
        }
    }


    fun startPayment(amount:Float){
        val checkout=Checkout()
        checkout.setKeyID(razorPayApiKey())

        val options=JSONObject()
        options.put("name","ShopAbs")
        options.put("description","")
        options.put("amount",(amount*100).roundToInt())
        options.put("currency","INR")
        checkout.open(GlobalNavigation.navController.context as Activity,options)
    }


    fun moveCartToOrdersAndClear(context: Context) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) return
        val userDoc = Firebase.firestore.collection("users").document(userId)

        userDoc.get().addOnSuccessListener { doc ->
            val cartItems = doc.get("cartItems") as? Map<String, Long> ?: emptyMap()
            val address = doc.getString("address") ?: ""
            if (cartItems.isNotEmpty()) {
                val orderData = mapOf(
                    "items" to cartItems,
                    "address" to address,
                    "timestamp" to System.currentTimeMillis()
                )
                userDoc.collection("orders")
                    .add(orderData)
                    .addOnSuccessListener {
                        userDoc.update("cartItems", FieldValue.delete())
                            .addOnSuccessListener {
                                AppUtil.showToast(context, "Order placed!")
                            }
                            .addOnFailureListener {
                                AppUtil.showToast(context, "Order placed, but failed to clear cart.")
                            }
                    }
                    .addOnFailureListener {
                        AppUtil.showToast(context, "Failed to create order.")
                    }
            } else {
                AppUtil.showToast(context, "Cart is empty.")
            }
        }
    }


}