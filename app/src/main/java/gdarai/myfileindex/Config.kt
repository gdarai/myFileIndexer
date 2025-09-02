package gdarai.myfileindex

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

object Config {
    val Sizes: IntArray = intArrayOf(1, 500, 2000, 50000)
    val SizeNames: Array<String> = arrayOf("do 1MB", "do 500 MB", "do 2 GB", "velke")

    var Indices: Array<Char> = emptyArray<Char>()
    var IndNames: Array<String> = arrayOf()

    val Modes: Array<ImageVector> = arrayOf(
        Icons.Default.Star,
        Icons.Default.Home,
    )

    fun InitState() {
        StateManager.selectedSizes = booleanArrayOf(false, true, true, true)
        Indices = emptyArray()
        IndNames = arrayOf()
    }
}