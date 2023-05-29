package com.example.shoppinglist.screens

import android.app.LauncherActivity.ListItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
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

@Preview
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

                   Text(modifier = Modifier.align(alignment = Alignment.CenterVertically),text="${listItems.get(it).name}")

                   Spacer(modifier = Modifier.weight(1f,true))

                   Switch(
                           checked = !listItems.get(it).isActive,
                           onCheckedChange = { listItems.get(i).isActive = !listItems.get(i).isActive },

                    )

                }
            }
        }
}
