package de.benkralex.socius.data.import_export.common

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

fun exportContacts(
    context: Context,
    fileLines: List<String>,
    fileType: String = "text/plain",
    fileName: String = "contacts_export.txt",
) {
    val content = fileLines.joinToString(separator = "\n")
    val tempFile = File(context.cacheDir, fileName).apply {
        writeText(content)
    }

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = fileType
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareIntent, ""))
}