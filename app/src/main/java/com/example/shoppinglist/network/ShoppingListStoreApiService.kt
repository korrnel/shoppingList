package com.example.shoppinglist.network

import com.example.shoppinglist.data.ShoppingListModel
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
    @GET("v1/c9959d26-e599-4d7d-b27c-e504646bc729")
//   @GET("v1/5f1903c3-f3fc-47f1-a939-36f4f33acd34")
//    @GET("v1/68ee9eb3-98ca-4898-9b96-9c71a4d5b901")
    suspend fun getList(): ShoppingListModel
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ShoppingListApi {
    val retrofitService: ShoppingListStoreApiService by lazy {
        retrofit.create(ShoppingListStoreApiService::class.java)
    }
}

