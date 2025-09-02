package gdarai.myfileindex

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import gdarai.myfileindex.ui.layouts.ConfigScreen
import gdarai.myfileindex.ui.layouts.LoadingScreen
import gdarai.myfileindex.ui.layouts.MainLayout
import gdarai.myfileindex.ui.theme.MyFileIndexTheme

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        appContext = this

        Config.InitState()

        setContent {
            MyFileIndexTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    val appState = remember { mutableStateOf("loading") }
    val loadingDone = remember { mutableStateOf(true) }

    when (appState.value) {
        "loading" -> LoadingScreen(
            onToConfig = { appState.value = "config" },
            onLoadingFinish = {
                if(appState.value.equals("loading")) appState.value = "main"
            })
        "config" -> ConfigScreen(onConfigFinish = {
            trySaveLocalConfig()
            appState.value = "loading"
        })
        "main" -> MainLayout()
        else -> {
            // Optional fallback for unexpected states
            Text("Unknown state: $appState.value")
        }
    }
}


