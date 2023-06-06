package com.example.shoppinglist.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppinglist.R
import com.example.shoppinglist.data.ShoppingListModelItem
import com.example.shoppinglist.network.ShoppingListApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


@Composable
fun ResultScreen(listItems : List<ShoppingListModelItem>) {
    val checkedState = remember { mutableStateOf(true)  }

    LazyColumn {
        items(listItems.size) {
            Row(modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            ) {
                val i= it
                Image (
                    painter = painterResource(id = R.drawable.image_part_002),
                    "image",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .align(alignment = Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.size(10.dp,0.dp))

                Text( modifier = Modifier.align(alignment = Alignment.CenterVertically),text="${listItems.get(it).name}")

                TextField(value = listItems.get(it).name, onValueChange = { listItems.get(i).name = it } )



                Spacer(modifier = Modifier.weight(1f,true))

                Switch(
                    checked = listItems.get(it).isActive,
                    onCheckedChange = {  },
                )

            }
        }
    }
}

@Composable
fun ShoppingListApp() {
    val itemsState = remember { mutableStateOf<List<ShoppingListModelItem>>(emptyList()) }
    val loadingState = remember { mutableStateOf(false) }
    val errorMessageState = remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val editingItemState = remember { mutableStateOf<ShoppingListModelItem?>(null) }
    val scope = rememberCoroutineScope()

    fun fetchItems() {
        loadingState.value = true
        itemsState.value = emptyList()
        scope.launch {
            try {
                itemsState.value = ShoppingListApi.retrofitService.getList()
                if (itemsState.value.isEmpty()) {
                    errorMessageState.value = "No result, list truncated"
                }
            } catch (e: IOException) {
                errorMessageState.value = e.message
                Log.e(ContentValues.TAG, "getList IO: ${e.message}")

            } catch (e: HttpException) {
                errorMessageState.value = e.message
                Log.e(ContentValues.TAG, "getList HTTP: ${e.message}")

            } finally {
                loadingState.value = false
            }
        }
    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {


        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Button(onClick = { fetchItems() }) {
                    Text(text = "Refresh")
                }
                Spacer(modifier = Modifier.width(16.dp))
                if (loadingState.value) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterVertically))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessageState.value != null) {
                Text(text = errorMessageState.value!!)
            }
            ResultScreen(itemsState.value)
        }


    }

}