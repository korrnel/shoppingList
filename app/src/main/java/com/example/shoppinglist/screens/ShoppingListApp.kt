package com.example.shoppinglist.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.R
import com.example.shoppinglist.data.ShoppingListModelItem
import kotlinx.coroutines.launch


@Composable
fun ResultScreen(listItems: List<ShoppingListModelItem>,
                 onItemToggle: (Item : ShoppingListModelItem, Boolean) -> Unit,
) {
    LaunchedEffect(Unit) {
        // on open...
    }
    //if (editingItemState.value != null) {
        // todo Edit item
   // }


    LazyColumn {
        items(listItems) { item->
            Row(modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
             ) {
                Image (
                    painter = painterResource(id = R.drawable.image_part_002),
                    "image",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .align(alignment = Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.size(10.dp,0.dp))
                ClickableText(text = AnnotatedString(item.name), modifier = Modifier.align(alignment = Alignment.CenterVertically), onClick = {onItemToggle(item, !item.isActive)})
                Spacer(modifier = Modifier.weight(1f,true))
                Switch(checked = item.isActive, onCheckedChange = { onItemToggle(item, it) })

            /*  IconButton(onClick = { editingItemState.value = listItems.get(it) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
*/
            }
        }
    }
}

@Composable
fun ShoppingListApp(viewModel: ShopplingListViewModel) {
    val itemState = viewModel.itemsState.observeAsState()
    val loadingState = viewModel.loadingState.observeAsState()
    val errorMessageState = viewModel.errorMessageState.observeAsState()
    val scope = rememberCoroutineScope()


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Button(onClick = {
                    scope.launch {
                            viewModel.fetchItems()
                                  }
                        }) {
                    Text(text = "Refresh")
                }
                Spacer(modifier = Modifier.width(16.dp))
                if (loadingState.value!!) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterVertically))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessageState.value != null) {
                Text(text = errorMessageState.value!!)
            }
            ResultScreen(itemState.value.orEmpty(),
                onItemToggle = { item, enabled ->
                    val updatedItems = viewModel.itemsState.value.orEmpty().toMutableList()
                    val updatedItem = item.copy(isActive  = enabled)
                    updatedItems[updatedItems.indexOf(item)] = updatedItem
                    viewModel.itemsState.value = updatedItems
                },)
        }


    }

}