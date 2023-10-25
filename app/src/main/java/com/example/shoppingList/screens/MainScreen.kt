package com.example.shoppingList.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShoppingListApp(viewModel: ShopplingListViewModel) {
        val itemState = viewModel.itemsState.observeAsState()
        val errorMessageState = viewModel.errorMessageState.observeAsState()
        val editingItemState = viewModel.editingItemState.observeAsState()
        val showClearConfirmation = viewModel.clearConfirmationState.observeAsState()

        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TopButtons(viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                if (errorMessageState.value != null) {
                    Text(text = errorMessageState.value!!)
                }

                if (showClearConfirmation.value==true)
                {
                    ConfirmDialog(onDismiss = {
                         viewModel.clearConfirmationState.setValue(false)
                        },
                        onConfirm = {
                             viewModel.clearList()
                             viewModel.clearConfirmationState.setValue(false)

                        } )
                }
                 else if (editingItemState.value != null) {
                    EditItemName(editingItemState.value!!,
                        onSaveClick = { if (it.text.trim().isNotEmpty()) viewModel.updateItemName(editingItemState.value!!, it.text) },
                        onCancelClick = { viewModel.cancelEditingItem() })
                    BackHandler(true) { viewModel.cancelEditingItem() }

                } else {

                    Spacer(modifier = Modifier.height(16.dp))

                    ResultScreen(
                        itemState.value.orEmpty(),
                        onItemToggle = { item, enabled ->
                            val updatedItems = viewModel.itemsState.value.orEmpty().toMutableList()
                            val updatedItem = item.copy(isActive  = enabled)

                            try {
                                updatedItems[updatedItems.indexOf(item)] = updatedItem
                            } catch (e: ArrayIndexOutOfBoundsException)
                            {

                            }
                            viewModel.itemsState.value = updatedItems
                        },
                        onItemEdit = { item -> viewModel.setEditingItem(item) },
                        onItemRemove = {item -> viewModel.removeItem(item) })
                }
            }


        }

    }
