package gdarai.myfileindex.ui.layouts

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DropdownMultiSelect(
    modifier: Modifier,
    options: List<String>,
    text: String,
    getSelected: () -> BooleanArray,
    setSelected: (BooleanArray) -> Unit,
    getTextAddon: (BooleanArray) -> String,
) {
    var showDialog by remember { mutableStateOf(false) }
    val selectedOptions = remember { mutableStateListOf<Boolean>() }
    val textAddon = remember { mutableStateOf(getTextAddon(getSelected())) }

    // Button to show the dialog
    Button(
        onClick = {
            selectedOptions.clear()
            selectedOptions.addAll(getSelected().toTypedArray())
            showDialog = true
        },
        modifier = modifier,
    ) {
        Text("$text [${textAddon.value}]")
    }

    // The dialog
    if (showDialog) {
        MultiSelectDialog(
            options = options,
            selectedOptions = selectedOptions,
            onDismissRequest = { showDialog = false },
            onConfirm = {
                showDialog = false
                Log.d("SelectedOptions", "($text) Selected options: $selectedOptions")
                val newSelection = selectedOptions.toBooleanArray()
                textAddon.value = getTextAddon(newSelection)
                setSelected(newSelection)
            }
        )
    }
}

@Composable
fun MultiSelectDialog(
    options: List<String>,
    selectedOptions: MutableList<Boolean>,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Options") },
        text = {
            Column {
                options.forEachIndexed { index, option ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        val isSelected = selectedOptions[index]
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                    isChecked ->
                                selectedOptions[index] = isChecked
                            }
                        )
                        Text(option)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}