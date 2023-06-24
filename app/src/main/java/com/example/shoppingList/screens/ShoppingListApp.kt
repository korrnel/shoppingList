package com.example.shoppingList.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.shoppingList.R
import com.example.shoppingList.data.ShoppingListModelItem
import kotlinx.coroutines.launch




@Composable
fun ResultScreen(listItems: List<ShoppingListModelItem>,
                 onItemToggle: (Item : ShoppingListModelItem, Boolean) -> Unit,
                 onItemEdit: (Item: ShoppingListModelItem) -> Unit,
                 onItemRemove:  (Item: ShoppingListModelItem) -> Unit
                ) {
    LaunchedEffect(Unit) {
        // on open...
    }
    val sortedItems = listItems.sortedWith( compareBy<ShoppingListModelItem> { !it.isActive }.thenBy { it.name })
    LazyColumn {
        items(sortedItems) { item->

            Row(modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
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
                Text(text = AnnotatedString(item.name),
                    modifier =
                    Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .clickable { onItemToggle(item, !item.isActive) }
                  )
                Spacer(modifier = Modifier.weight(1f,true))
                Switch(checked = item.isActive, onCheckedChange = { onItemToggle(item, it) })
                IconButton(onClick = { onItemEdit(item) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { onItemRemove(item) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }


            }
        }
    }
}

@Composable
fun ShoppingListApp(viewModel: ShopplingListViewModel) {
    val itemState = viewModel.itemsState.observeAsState()
    val loadingState = viewModel.loadingState.observeAsState()
    val errorMessageState = viewModel.errorMessageState.observeAsState()
    val editingItemState = viewModel.editingItemState.observeAsState()

    val scope = rememberCoroutineScope()


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Button(onClick = {
                    scope.launch {
                        viewModel.clearList()
                    }
                }) {
                    Text(text = "Clear")
                }
                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = {
                    scope.launch {
                            viewModel.fetchItems()
                                  }
                        }) {
                    Text(text = "Refresh")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    scope.launch {
                        viewModel.editingItemState.value= ShoppingListModelItem(true,"New",0,0)
                    }
                }) {
                    Text(text = "Add")
                }
                if (loadingState.value!!) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterVertically))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessageState.value != null) {
                Text(text = errorMessageState.value!!)
            }

            Log.d("Edit is what? ", editingItemState.value.toString())
            if (editingItemState.value != null) {
                Log.d("Edit is on", editingItemState.value.toString())
                EditItemName(editingItemState.value!!,
                    onSaveClick = { viewModel.updateItemName(editingItemState.value!!, it.text) },
                    onCancelClick = { viewModel.cancelEditingItem() })
                BackHandler(true) { viewModel.cancelEditingItem() }

            } else {

            Spacer(modifier = Modifier.height(16.dp))

            ResultScreen(
                itemState.value.orEmpty(),
                onItemToggle = { item, enabled ->
                    val updatedItems = viewModel.itemsState.value.orEmpty().toMutableList()
                    val updatedItem = item.copy(isActive  = enabled)
                    updatedItems[updatedItems.indexOf(item)] = updatedItem
                    viewModel.itemsState.value = updatedItems
                },
                onItemEdit = { item -> viewModel.setEditingItem(item) },
                onItemRemove = {item -> viewModel.removeItem(item) })
            }
        }


    }

}


@Composable
fun EditItemName(editingItemState: ShoppingListModelItem,
                 onSaveClick : (TextFieldValue) -> Unit,
                 onCancelClick: () -> Unit) {
    var text by remember { mutableStateOf(TextFieldValue(editingItemState.name)) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onCancelClick() }) {
            Icon(Icons.Default.Close, contentDescription = "Close")
        }

        TextField(value = text, onValueChange = { newText ->
                                                    text = newText } )
        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { onSaveClick(text) }) {
            Icon(Icons.Default.Check, contentDescription = "Save")
        }

    }

}


