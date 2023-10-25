package com.example.shoppingList.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppingList.data.ShoppingListModelItem
import kotlinx.coroutines.launch

@Preview
@Composable
fun TopButtons()
{
    val XX = ShopplingListViewModel()
    TopButtons(viewModel = XX)
}
@Composable
fun TopButtons(viewModel: ShopplingListViewModel) {
    val scope = rememberCoroutineScope()
    val loadingState = viewModel.loadingState.observeAsState()
    Row {

         Button(onClick = {
            scope.launch {
              viewModel.clearConfirmationState.setValue(true)
            }
        }) {
            Text(text = "Clear")
        }
        Spacer(modifier = Modifier.width(16.dp))
/*
        Button(onClick = {
            scope.launch {
               viewModel.fetchItems()
            }
        }) {
            Text(text = "Refresh")
        }
        Spacer(modifier = Modifier.width(16.dp))
        */
        Button(onClick = {
            scope.launch {
                 viewModel.editingItemState.value= ShoppingListModelItem(true,"",0,0)
            }
        }) {
            Text(text = "Add")
        }
        if (loadingState.value!!) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}


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
               /* Image (
                    painter = painterResource(id = R.drawable.image_part_002),
                    "image",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .align(alignment = Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.size(10.dp,0.dp))
                */
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
fun ConfirmDialog (
    onDismiss: (() -> Unit)?,
    onConfirm: () -> Unit
){
    AlertDialog(
        onDismissRequest = { onDismiss },
        title = { Text(text = "Confirmation") },
        text = { Text(text = "Are you sure you want to perform this action?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            onDismiss?.let {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "No")
                }
            }
        }
    )
}
@Composable
fun EditItemName(editingItemState: ShoppingListModelItem,
                 onSaveClick : (TextFieldValue) -> Unit,
                 onCancelClick: () -> Unit) {
    var text by remember { mutableStateOf(TextFieldValue(editingItemState.name)) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    DisposableEffect(Unit) {
        // Set focus on TextField when the screen opens
        focusRequester.requestFocus()

        // Release focus when the screen is no longer active
        onDispose {
            focusManager.clearFocus()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onCancelClick() }) {
            Icon(Icons.Default.Close, contentDescription = "Close")
        }
        TextField(
            value = text
            , onValueChange = { newText ->
                text = newText }  , modifier = Modifier
                .focusable(true)
                .focusRequester(focusRequester)

        )
        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { onSaveClick(text) }) {
            Icon(Icons.Default.Check, contentDescription = "Save")
        }

    }

}



