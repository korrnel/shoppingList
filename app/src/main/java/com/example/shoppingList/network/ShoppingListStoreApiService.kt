package com.example.shoppingList.network

import com.example.shoppingList.data.ShoppingListModelItem
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL = "https://mocki.io"

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory(okhttp3.MediaType.get("application/json")))
    .baseUrl(BASE_URL)
    .build()


interface ShoppingListStoreApiService
{
   @GET("v1/68ee9eb3-98ca-4898-9b96-9c71a4d5b901")
   suspend fun getList(): List<ShoppingListModelItem>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ShoppingListApi {
    val retrofitService: ShoppingListStoreApiService by lazy {
        retrofit.create(ShoppingListStoreApiService::class.java)
    }
}

