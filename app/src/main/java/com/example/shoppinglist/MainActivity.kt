package com.example.shoppinglist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.example.shoppinglist.data.ShoppingListModelItem
import com.example.shoppinglist.screens.ShoppingListApp
import com.example.shoppinglist.screens.ShopplingListViewModel
import com.example.shoppinglist.ui.theme.ShoppingListTheme
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
        Log.d("Hee", "saved")
        editor.apply()
    }
}

