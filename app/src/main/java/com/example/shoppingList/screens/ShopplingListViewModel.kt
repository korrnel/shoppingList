package com.example.shoppingList.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppingList.data.ShoppingListModelItem
import com.example.shoppingList.network.ShoppingListApi
import retrofit2.HttpException
import java.io.IOException

class ShopplingListViewModel : ViewModel() {

    val itemsState = MutableLiveData<List<ShoppingListModelItem>>(emptyList())
    val loadingState = MutableLiveData(false)
    val errorMessageState = MutableLiveData<String?>(null)
    val editingItemState = MutableLiveData<ShoppingListModelItem?>(null)
    init {
        //
    }




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
                Log.e(TAG, "getList IO: ${e.message}")

            } catch (e: HttpException) {
                errorMessageState.value = e.message
                Log.e(TAG, "getList HTTP: ${e.message}")
            } catch (e: Exception) {
                errorMessageState.value = e.message
                Log.e(TAG, "getList : ${e.message}")

            } finally {
                errorMessageState.value= null
                loadingState.value = false
            }
        }

    fun setEditingItem(item: ShoppingListModelItem) {
        editingItemState.value = item
        Log.d(TAG,"set")
    }

    fun cancelEditingItem() {
        editingItemState.value = null;
        Log.d(TAG,"cancel")
    }
    fun clearList()
    {
        itemsState.value= emptyList()
    }
    fun updateItemName(item: ShoppingListModelItem, text: String) {
        val updatedItems = itemsState.value.orEmpty().toMutableList()
        val updatedItem = item.copy(name = text)
         try {
             updatedItems[updatedItems.indexOf(item)] = updatedItem
         } // new item
          catch (e : ArrayIndexOutOfBoundsException) {
             updatedItems.add(updatedItem)
         }
        itemsState.value = updatedItems
        editingItemState.value = null
        Log.d(TAG,"saved")
    }
    fun removeItem(item: ShoppingListModelItem) {
        val updatedItems = itemsState.value.orEmpty().toMutableList()
        updatedItems.remove(item)
        itemsState.value = updatedItems

    }


}



