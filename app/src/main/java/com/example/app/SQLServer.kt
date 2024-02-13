package com.example.app

import android.os.StrictMode
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class SQLServer {

    //private val server: String = "190.119.170.4:1433"

    private val server: String = "200.60.111.146:1433"

    private val database: String = "dataNSP"

    private val user: String = "james"

    private val password: String = "lerolero79"

    private var connection: Connection? = null

    fun connect() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://$server;databaseName=$database;user=$user;password=$password")
            println("Me conecté a la base de datos :D")
        }
        catch (e: SQLException) {
            println("Error al conectar a la base de datos :D")
            e.printStackTrace()
            throw e
        }
    }

    fun executeStoredProcedure(content: String?, state: String?, mode: String?, phone: String?, imei: String?): Boolean? {
        if (connection == null)
            return false
        var query = connection?.prepareCall("{ call APP_Actualizar (?, ?, ?, ?, ?) }")
        query?.setString(1, content)
        query?.setString(2, state)
        query?.setString(3, mode)
        query?.setString(4, phone)
        query?.setString(5, imei)
        return query?.execute()
    }

    fun disconnect() {
        connection?.close()
    }
}