package com.example.shoppinglist.screens

import android.app.LauncherActivity.ListItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppinglist.R
import com.example.shoppinglist.data.ShoppingListModel
import com.example.shoppinglist.data.ShoppingListModelItem

@Composable
fun ShoppingListApp() {
        Surface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val shoppingListVM: ShoppingListViewModel = viewModel()

                ShoppingList(shoppingListVM.listUiState)
            }
}



@Composable
fun ShoppingList(listUiState: ListUiState)
    {
        when (listUiState) {
            is ListUiState.Loading -> LoadingScreen()
            is ListUiState.Success -> ResultScreen(listUiState.shoppingListModel)
            is ListUiState.Error -> ErrorScreen()
        }
    }

    @Composable
    fun ErrorScreen() {
        Text("error!!!")
    }
    @Composable
    fun LoadingScreen() {
        Text("Loading")
    }

    @Composable
    fun ResultScreen(listItems : List<ShoppingListModelItem>) {
        LazyColumn {
            items(listItems.size) {
               Row() {

                   Image (
                        painter = painterResource(id = R.drawable.image_part_002),
                        "image",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.size(10.dp,0.dp))

                    Text("${listItems.get(it).name}")

                }
            }
        }
}
