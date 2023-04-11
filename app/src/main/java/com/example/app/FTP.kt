package com.example.app

import android.widget.Toast
import it.sauronsoftware.ftp4j.FTPClient
import java.io.File

class FTP {

    private val server: String = "190.119.170.4"

    private val user: String = "Diomedes2022"

    private val password: String = "NSP22@@terminado//.."

    fun sendImage(imagePath: String?) {
        try {
            var ftpClient = FTPClient()
            ftpClient.connect(server)
            ftpClient.login(user, password)

            ftpClient.upload(File(imagePath))
            ftpClient.disconnect(true)
        }
        catch (e: Exception) {
            println("Iamgen no enviada")
            e.printStackTrace()
        }
    }
}