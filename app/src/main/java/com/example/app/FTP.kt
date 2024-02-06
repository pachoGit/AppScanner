package com.example.app

import android.content.Context
import android.net.Uri
import android.os.StrictMode
import it.sauronsoftware.ftp4j.FTPClient
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FTP {

    private val server: String = "190.119.170.4"

    private val user: String = "Diomedes2022"

    private val password: String = "NSP22@@terminado//.."

    private var ftpClient = FTPClient()

    fun connect() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            ftpClient.connect(server)
            ftpClient.login(user, password)
        } catch (e: Exception) {
            println("No se logro conectar al FTP")
            e.printStackTrace()
            throw e
        }
    }

    fun sendImage(imagePath: String?, name: String?, context: Context) {
        try {
            var image = getImage(imagePath!!, context)
            if (image == null) {
                println("Imagen no existe")
                return
            }

            if (image.exists()) {
                println("Si existe el archivo original")
            } else {
                println("No existe el archivo original")
            }

            var imageRename = File(image.parent, name)

            if (imageRename.exists()) {
                println("Para el renombre ya existe")
            }

            val success = image.renameTo(imageRename)
            if (success) {
                // This is ok when get imagen with the scanner
                println("Se renombro correctamente")
            } else {
                println("ERROR: No se renombro")
            }

            println("Enviando: " + imageRename.name);
            println("Es archivo: " + imageRename.isFile);

            ftpClient.upload(imageRename)
        } catch (e: Exception) {
            println("Imagen no enviada: " + e.message)
            e.printStackTrace()

        }
    }

    fun disconnect() {
        try {
            ftpClient.disconnect(true)
        } catch (e: Exception) {
            println("Error al desconectar del FTP")
            e.printStackTrace()
        }
    }

    fun getImage(imagePath: String, context: Context): File? {
        var image: File?
        try {
            image = File(imagePath)
            if (!image.exists()) {
                // image = File(Uri.parse(imagePath).path)
                image = getFileFromUri(context, Uri.parse(imagePath))
                println("Usando metodo Uri para imagen")
            }
            return image
        } catch (e: Exception) {
            throw e
        }
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream?.use {
            val file =
                    File(
                            context.cacheDir,
                            "temp_file"
                    ) // Archivo temporal en el directorio de caché
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024) // Tamaño del búfer de lectura
                var read: Int
                while (it.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return file
        }
        return null
    }
}
