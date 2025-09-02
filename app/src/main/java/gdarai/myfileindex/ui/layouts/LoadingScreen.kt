package gdarai.myfileindex.ui.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gdarai.myfileindex.StateManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import gdarai.myfileindex.getData
import gdarai.myfileindex.processData
import gdarai.myfileindex.tryLoadLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoadingScreen(
    onToConfig: () -> Unit,
    onLoadingFinish: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        scope.launch(Dispatchers.IO) {
            StateManager.loaded = false
            try {
                getData() // Fetch data
            } catch (e: Exception) {
                e.printStackTrace()
                tryLoadLocal()
            }
            processData() // Process data
            StateManager.applyNewFilters() // Set first results

            delay(2000)
            StateManager.loaded = true
            onLoadingFinish()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement
                .spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextBox("Loading Data")
            LoadingSpinner()

            LazyColumn {
                items(StateManager.log) { logEntry ->
                    Text(text = logEntry)
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onToConfig() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Settings, contentDescription = "Config")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Config")
            }
        }
    }
}

@Composable
fun LoadingSpinner() {
    Box {
        CircularProgressIndicator() // Defaults to MaterialTheme colors
    }
}

@Composable
fun TextBox(text: String) {
    Box {
        Text(
            text = text
        )
    }
}