package com.example.shoppinglist.screens

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.shoppinglist.data.ShoppingListModelItem
import com.example.shoppinglist.network.ShoppingListApi

import retrofit2.HttpException
import java.io.IOException

class ShopplingListViewModel : ViewModel() {

    val itemsState = MutableLiveData<List<ShoppingListModelItem>>(emptyList())
    val loadingState = MutableLiveData(false)
    val errorMessageState = MutableLiveData<String?>(null)

suspend fun fetchItems() {
        loadingState.value = true
        itemsState.value = emptyList()


        try {
                itemsState.value = ShoppingListApi.retrofitService.getList()
                if (itemsState.value!!.isEmpty()) {
                    errorMessageState.value = "No result, list truncated"
                }
            } catch (e: IOException) {
                errorMessageState.value = e.message
                Log.e(ContentValues.TAG, "getList IO: ${e.message}")

            } catch (e: HttpException) {
                errorMessageState.value = e.message
                Log.e(ContentValues.TAG, "getList HTTP: ${e.message}")
            } catch (e: Exception) {
                errorMessageState.value = e.message
                Log.e(ContentValues.TAG, "getList : ${e.message}")

            } finally {
                loadingState.value = false
            }
        }


}


