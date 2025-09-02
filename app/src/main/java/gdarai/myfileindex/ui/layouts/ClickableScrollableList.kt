package gdarai.myfileindex.ui.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gdarai.myfileindex.DataEntry
import gdarai.myfileindex.StateManager

@Composable
fun ClickableScrollableList(items: List<DataEntry>) {

    var showDialog by remember { mutableStateOf(false) }
    var dialogItem by remember { mutableStateOf( DataEntry("","","") )}

    fun onItemClick(i: DataEntry) {
        dialogItem = i
        showDialog = true
    }

    if (showDialog) {
        ItemDialog(
            item = dialogItem,
            onConfirm = {
                            showDialog = false
                            StateManager.searchQuery.value = dialogItem.query
                        },
            onDismissRequest = { showDialog = false },
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items.size) { index ->
            val item = items[index]
            BasicText(
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,            // Text color
                    fontSize = 16.sp,                                       // Font size
                    textAlign = TextAlign.Start                             // Optional alignment
                ),
                text = item.file,
                modifier = Modifier
                    .clickable { onItemClick(item) }
                    .fillMaxSize() // Adjust this to fit your design
                    .background(MaterialTheme.colorScheme.surface)   // Background color
                    .padding(8.dp)                // Margin (inner padding)
            )
        }
    }
}

@Composable
fun ItemDialog(
    item: DataEntry,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Item Detail") },
        text = {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ){
                Text(item.file)
                Text(
                    color=MaterialTheme.colorScheme.surface,
                    text=item.query
                )
                Text(item.size)
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Copy")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Ok")
            }
        }
    )
}