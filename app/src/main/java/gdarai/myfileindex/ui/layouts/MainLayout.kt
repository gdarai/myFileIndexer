package gdarai.myfileindex.ui.layouts

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gdarai.myfileindex.Config
import gdarai.myfileindex.StateManager

@Composable
fun MainLayout() {

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.navigationBars.asPaddingValues())
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = {
                    StateManager.searchMode.value = (StateManager.searchMode.value + 1) % Config.Modes.size
                }) {
                    Icon(
                        imageVector = Config.Modes[StateManager.searchMode.value],
                        contentDescription = "Mode"
                    )
                }

                OutlinedTextField(
                    value = StateManager.searchQuery.value,
                    onValueChange = { newText -> StateManager.searchQuery.value = newText.lowercase() },
                    label = { Text("Query (can *)") },
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { StateManager.applyNewQuery() }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Large Text Box (Filling remaining space)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                ClickableScrollableList(
                    items=StateManager.selection
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement
                    .spacedBy(16.dp, Alignment.CenterHorizontally),
            ) {
                DropdownMultiSelect(
                    modifier = Modifier.weight(1f),
                    options = Config.SizeNames.toList(),
                    text = "F. Size",
                    getSelected = StateManager::getmySelectedSizes,
                    setSelected = StateManager::setmySelectedSizes,
                    getTextAddon = ::getCntTextAddon,
                )

                DropdownMultiSelect(
                    modifier = Modifier.weight(1f),
                    options = Config.IndNames.toList(),
                    text = "Sources",
                    getSelected = StateManager::getmySelectedIdx,
                    setSelected = StateManager::setmySelectedIdx,
                    getTextAddon = ::getCntTextAddon,
                )
            }
        }
    }
}

fun getCntTextAddon(selected: BooleanArray): String {
    val trueCount = selected.count { it }
    return ""+trueCount+"/"+selected.size
}
