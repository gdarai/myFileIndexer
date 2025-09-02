package gdarai.myfileindex

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileNotFoundException
import java.text.Normalizer

suspend fun getData() {

    tryLoadLocalConfig();

    try {
        withContext(Dispatchers.IO) {

            StateManager.addLogEntry("Fetching index ...")

            val client = OkHttpClient()
            val request = Request.Builder()
                .url(StateManager.sourceUrl)
                .build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string() // Read the response as a string
                responseBody?.let {
                    StateManager.indexFileLines = it.lines().toTypedArray()
                }
                StateManager.addLogEntry("Fetching finished")

                val file = File(MainActivity.appContext.filesDir, "IndexFile.txt")
                file.writeText(responseBody ?: "")
                StateManager.addLogEntry("Local backup created")

            } else {
                // Some error
                StateManager.addLogEntry("Fetching failed: ${response.code}")
                tryLoadLocal()
            }
        }
    } catch (e: Exception) {
        StateManager.addLogEntry("Fetching failed: ${e.message}")
        tryLoadLocal()
    }

}

fun tryLoadLocal() {
    StateManager.addLogEntry("Loading local backup")

    try {
        val file = File(MainActivity.appContext.filesDir, "IndexFile.txt")
        val fileContent = file.readText()
        StateManager.indexFileLines = fileContent.lines().toTypedArray()
        StateManager.addLogEntry("Local backup loaded")

    } catch (e: FileNotFoundException) {
        StateManager.addLogEntry("Local backup not found")
    }
}

fun trySaveLocalConfig() {
    StateManager.addLogEntry("Saving config")

    try {
        val file = File(MainActivity.appContext.filesDir, "ConfigFile.txt")
        file.writeText(StateManager.sourceUrl+"\n")
        StateManager.addLogEntry("Config saved")
    } catch (e: FileNotFoundException) {
        StateManager.addLogEntry("Saving config failed")
    }
}

fun tryLoadLocalConfig() {
    StateManager.addLogEntry("Loading config")

    try {
        val file = File(MainActivity.appContext.filesDir, "ConfigFile.txt")
        val fileContent = file.readText().lines();
        StateManager.sourceUrl = fileContent[0].replace("\n", "")
        StateManager.addLogEntry("Config loaded")

    } catch (e: FileNotFoundException) {
        StateManager.sourceUrl = "http://10.0.1.16:8123/local/IndexFile.txt"
        StateManager.addLogEntry("Config not found")
    }
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
suspend fun processData() {

    withContext(Dispatchers.IO) {

        StateManager.addLogEntry("Parsing index file")

        var idChar = '%'
        val idxList = mutableListOf<String>()
        val idList = mutableListOf<Char>()
        var skipped = 0
        var count = 0
        val normRegex = Regex("\\p{M}")

        StateManager.indexFileLines?.takeIf { it.isNotEmpty() }?.forEach { item ->

            val end = item.indexOf("|", 1)
            if(end < 0) {
                skipped += 1
                return@forEach
            }

            if (item.startsWith("|")) {

                if(idChar != '%') {
                    StateManager.addLogEntry("Parsed $count entries")
                    count = 0
                }

                val newIndex = item.substring(1, end)
                StateManager.addLogEntry("Parsing index: $newIndex")

                idxList.add(newIndex)
                idChar = ('a' + (idxList.size - 1))
                idList.add(idChar)
                count = 0

            } else {

                if(idChar == '%') {
                    skipped += 1
                    return@forEach
                }
                count += 1
                val path = item.substring(0, end)
                val size = item.substring(end+1).toInt()
                var sizeIdx = Config.Sizes.indexOfFirst { it > size }
                if (sizeIdx < 0 || sizeIdx >= Config.Sizes.size) { sizeIdx = Config.Sizes.size - 1 }

                val normalized = Normalizer.normalize(path, Normalizer.Form.NFD)
                val query = normalized.lowercase().replace(normRegex, "")

                StateManager.processedLines.add(DataEntry("$idChar$sizeIdx$query", path, "$size MB"))
            }
        }

        StateManager.addLogEntry("Parsing index file finished")
        StateManager.addLogEntry("Skipped $skipped wrong entries")

        StateManager.selectedIndices = BooleanArray(idList.size) { false }
        Config.Indices = idList.toTypedArray()
        Config.IndNames = idxList.toTypedArray()

        StateManager.addLogEntry("Internal DB ready")
        StateManager.loaded = true
    }
}