package com.github.sugunasriram.myfisloanlibone.fis_code.utils

import android.os.Environment
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileLogger {
    private const val FOLDER_NAME = "Fs"
    private const val FILE_NAME = "Financial_App.txt"

    fun writeToFile(content: String) {
        val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FOLDER_NAME)
        folder.mkdirs()

        val file = File(folder, FILE_NAME)
        try {
            val logTime = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
            val fileWriter = BufferedWriter(FileOutputStream(file, true).bufferedWriter())
            fileWriter.write(String.format(Locale.getDefault(), "[%s] %s%n", logTime, content))
            fileWriter.write("\t")
            fileWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    
}
