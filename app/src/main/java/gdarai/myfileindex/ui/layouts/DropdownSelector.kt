package gdarai.myfileindex.ui.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun DropdownSelector() {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("Select an option") }

    IconButton(onClick = { /* Handle cogwheel click */ }) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        listOf("Option 1", "Option 2", "Option 3").forEach { option ->
            DropdownMenuItem(
                onClick = {
                    selectedItem = option
                    expanded = false
                },
                text = { Text(option) },
                modifier = TODO(),
                leadingIcon = TODO(),
                trailingIcon = TODO(),
                enabled = TODO(),
                colors = TODO(),
                contentPadding = TODO()
            )
        }
    }
}