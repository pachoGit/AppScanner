package com.example.app

import android.os.StrictMode
import android.widget.Toast
import it.sauronsoftware.ftp4j.FTPClient
import java.io.File

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
        }
        catch (e: Exception) {
            println("No se logro conectar al FTP")
            e.printStackTrace()
        }
    }

    fun sendImage(imagePath: String?, name: String?) {
        try {
            var image = File(imagePath)
            if (image.exists()) {
                println("Si existe el archivo original")
            }
            var imageRename = File(image.parent, name)
            if (imageRename.exists()) {
                println("Para el renombre ya existe")
            }
            val success = image.renameTo(imageRename)
            if (success) {
                println("Se renombro correctamente")
            }
            else {
                println("ERROR: No se renombro")
            }
            ftpClient.upload(imageRename)
        }
        catch (e: Exception) {
            println("Imagen no enviada")
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            ftpClient.disconnect(true)
        }
        catch (e: Exception) {
            println("Error al desconectar del FTP")
            e.printStackTrace()
        }
    }
}