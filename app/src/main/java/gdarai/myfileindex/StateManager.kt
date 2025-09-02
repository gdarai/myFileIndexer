package gdarai.myfileindex

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import java.io.File

data class DataEntry(val query: String, val file: String, val size: String)

object StateManager {
    var loaded = false
    var log = mutableStateListOf<String>()
        private set

    var indexFileLines: Array<String>? = null
    var processedLines = mutableStateListOf<DataEntry>()
    private val filteredLines =  mutableStateListOf<DataEntry>()
    val selection = mutableStateListOf<DataEntry>()

    var selectedSizes: BooleanArray = booleanArrayOf()
    var selectedIndices: BooleanArray = booleanArrayOf()
    var sourceUrl: String = "http://10.0.1.16:8123/local/IndexFile.txt"
    val searchQuery = mutableStateOf("")
    val searchMode = mutableStateOf(0)

    fun addLogEntry(entry: String) {
        log.add(entry)
    }

    private fun buildRegexFromFilters(): Regex {
        // Collect the indices of `true` values in the filter
        val allowedSizes = selectedSizes.toTypedArray().mapIndexedNotNull { index, isAllowed ->
            if (isAllowed) index.toString() else null
        }

        val allowedIdx = selectedIndices.toTypedArray().mapIndexedNotNull { index, isAllowed ->
            if (isAllowed) Config.Indices[index].toString() else null
        }

        // If no numbers are allowed, return a regex that matches nothing
        var sizeReg = ""
        if (allowedSizes.isNotEmpty()) sizeReg = "(${allowedSizes.joinToString("|")})"

        var idxReg = ""
        if (allowedIdx.isNotEmpty()) idxReg = "(${allowedIdx.joinToString("|")})"

        Log.d("FF", "REG:$idxReg:$sizeReg:")
        if(sizeReg == "" || idxReg == "") return Regex("^$")
        return Regex("^${idxReg}${sizeReg}.*")
    }

    fun applyNewFilters() {
        addLogEntry("Applying new filters")
        filteredLines.clear()
        val regex = buildRegexFromFilters()

        processedLines.forEach { s ->
            if(s.query.matches(regex)) {
                filteredLines.add(DataEntry(s.query.substring(2), s.file, s.size))
            }
        }
        applyNewQuery()
    }

    fun applyNewQuery() {
        addLogEntry("Applying new query")
        selection.clear()

        val regex = Regex("^.*${searchQuery.value}.*")

        when (searchMode.value) {
            0 -> {
                filteredLines.forEach { s ->
                    if(s.query.matches(regex)) selection.add(s)
                }
            }
            1 -> {
                val keySet = mutableSetOf<String>()
                filteredLines.forEach { s ->
                    if(s.query.matches(regex)) {
                        val path = File(s.query).parent ?: ""
                        val pathName = File(s.file).parent ?: ""
                        if(!keySet.contains(path)) {
                            keySet.add(path)
                            selection.add(DataEntry(path, pathName, ""))
                        }
                    }
                }
            }
        }

        addLogEntry("Selection is now ready")
    }

    fun getmySelectedSizes(): BooleanArray {
        return selectedSizes
    }

    fun setmySelectedSizes(newValues: BooleanArray) {
        selectedSizes = newValues
        applyNewFilters()
    }

    fun getmySelectedIdx(): BooleanArray {
        return selectedIndices
    }

    fun setmySelectedIdx(newValues: BooleanArray) {
        selectedIndices = newValues
        applyNewFilters()
    }

}