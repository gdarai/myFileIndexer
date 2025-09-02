package gdarai.myfileindex.ui.layouts

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gdarai.myfileindex.StateManager

@Composable
fun ConfigScreen(
   onConfigFinish: () -> Unit
) {
    var sourceUrl by remember { mutableStateOf(StateManager.sourceUrl) }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    StateManager.sourceUrl = sourceUrl
                    onConfigFinish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Save",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Save & Restart")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Config",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Label
            Text(
                text = "Source URL",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Input
            OutlinedTextField(
                value = sourceUrl,
                onValueChange = { sourceUrl = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}
