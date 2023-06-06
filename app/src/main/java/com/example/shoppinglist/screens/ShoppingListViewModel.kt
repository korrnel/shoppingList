package com.example.shoppinglist.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ShoppingListModel
import com.example.shoppinglist.data.ShoppingListModelItem
import com.example.shoppinglist.network.ShoppingListApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface ListUiState {
    data class Success(val shoppingListModel: List<ShoppingListModelItem>) : ListUiState
    object Error : ListUiState
    object Loading : ListUiState
}

class ShoppingListViewModel: ViewModel() {
    var listUiState: ListUiState by mutableStateOf(ListUiState.Loading)
        private set


    init {
        getList()
    }

    private fun getList() {
        viewModelScope.launch {
            listUiState = try {
                var result : List<ShoppingListModelItem> = ShoppingListApi.retrofitService.getList()
                if (result.isNullOrEmpty()) {
                    ListUiState.Error
                } else {
                    ListUiState.Success(result)
                }
            } catch (e: IOException) {
                Log.e(TAG, "getList IO: ${e.message}")
                ListUiState.Error
            } catch (e: HttpException) {
                Log.e(TAG, "getList HTTP: ${e.message}")
                ListUiState.Error
            }

        }
    }



}