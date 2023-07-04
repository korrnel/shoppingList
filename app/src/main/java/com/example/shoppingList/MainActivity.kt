package com.example.shoppingList

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.shoppingList.data.ShoppingListModelItem
import com.example.shoppingList.screens.ShoppingListApp
import com.example.shoppingList.screens.ShopplingListViewModel
import com.example.shoppingList.ui.theme.ShoppingListTheme
import com.google.gson.Gson


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: ShopplingListViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // load previous state
        this.viewModel = ShopplingListViewModel()
        this.viewModel.itemsState.value=loadItemsFromLocalStorage()
        setContent {
            ShoppingListTheme {
                    ShoppingListApp(viewModel = this.viewModel)

            }
        }
    }
    fun loadItemsFromLocalStorage(): List<ShoppingListModelItem> {
        this.sharedPreferences = getSharedPreferences("ShoppingList", MODE_PRIVATE)
        val savedList = sharedPreferences.getString("ShoppingL", "")
        return if (savedList?.isNotEmpty() == true) {
            Gson().fromJson(savedList, Array<ShoppingListModelItem>::class.java).toList()
        } else {
            emptyList()
        }
    }

    override fun onPause() {
        super.onPause()
        // Convert the list of custom items to a JSON string
        val itemListJson = Gson().toJson(viewModel.itemsState.value)
        val editor = sharedPreferences.edit()
        editor.putString("ShoppingL", itemListJson)
        Log.d(TAG, "saved")
        editor.apply()
    }
}

