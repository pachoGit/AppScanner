package com.example.app

import android.database.SQLException

class QueryService {

    private val sql = SQLServer()

    private val ftp = FTP()

    // TODO/FIXME - if exists more images? :|
    private val listId: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    constructor() {

    }

    public fun executeQuery(data: MutableMap<String, String?>) {
        try {
            sql.connect()
            sql.executeStoredProcedure(
                data["content"],
                data["state"],
                data["mode"],
                data["phone"],
                "")

            sql.disconnect()
            ftp.connect()
            // Send the principal imagen
            ftp.sendImage(data["imagePath"], data["content"] + ".jpg")
            // Send the extra images
            val extraImages = data["extra"]
            var index = 0
            if (extraImages != null) {
                val extraImagesPath = extraImages.split(",").toList()
                extraImagesPath.forEach {
                    ftp.sendImage(it, data["content"] + listId[index++] + ".jpg")
                }
            }

            ftp.disconnect()
        } catch (e: SQLException) {
            throw e
        }
    }
}
