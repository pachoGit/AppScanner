package com.example.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import android.content.pm.PackageManager
import android.Manifest
import android.os.StrictMode
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DeliveredStateActivity : AppCompatActivity() {

    private val sql = SQLServer()

    private val ftp = FTP()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivered_state)
        val content = intent.getStringExtra("content")
        val imagePath = intent.getStringExtra("imagePath")
        val state = "0" //intent.getStringExtra("state")
        val phone = intent.getStringExtra("phone")

        val textView = findViewById<TextView>(R.id.textCodeBar2).apply {
            text = content
        }

        val data = mutableMapOf<String, String?>("content" to content, "imagePath" to imagePath, "state" to state, "phone" to phone)

        // Boton del modo SELLO
        findViewById<Button>(R.id.btnStamp).setOnClickListener {
            data["mode"] = "02"
            val jsonData = toJsonData(data)
            //Toast.makeText(this, jsonData.toString(), Toast.LENGTH_LONG).show()
            executeQuery(data)
        }

        // Boton del modo FIRMA
        findViewById<Button>(R.id.btnSignature).setOnClickListener {
            data["mode"] = "03"
            val jsonData = toJsonData(data)
            //Toast.makeText(this, jsonData.toString(), Toast.LENGTH_LONG).show()
            executeQuery(data)
        }

        // Boton de REGRESAR
        // Solo elimino el estado seleccionado previamente
        findViewById<Button>(R.id.btnPrevious).setOnClickListener {
            data.remove("state")
            goToChooseStateActivity(data)
        }
    }

    override fun onBackPressed() {
        goToMainActivity()
        finish()
    }

    private fun goToChooseStateActivity(data: MutableMap<String, String?>) {
        val intent = Intent(this, ChooseStateActivity::class.java).apply {
            data.map { (key, value) ->
                putExtra(key, value)
            }
        }
        startActivity(intent)
    }

    private fun toJsonData(data: MutableMap<String, String?>): JSONObject {
        val newData = mutableMapOf<Any?, Any?>("data" to data)
        return JSONObject(newData)
    }

    private fun goToEndActivity() {
        val intent = Intent(this, EndActivity::class.java)
        startActivity(intent)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun executeQuery(data: MutableMap<String, String?>) {
        try {
            sql.connect()
            var response = sql.executeStoredProcedure(
                data["content"],
                data["state"],
                data["mode"],
                data["phone"],
                ""
            )
            //Toast.makeText(this, "Se ejecutó la consulta: ", Toast.LENGTH_LONG).show()
            sql.disconnect()

            ftp.connect()
            ftp.sendImage(data["imagePath"], data["content"] + ".jpg")
            ftp.disconnect()

            goToEndActivity()
        } catch (e: SQLException) {
            Toast.makeText(this, "Error Interno: Asegurece de tener acceso a internet", Toast.LENGTH_LONG).show()
            e.printStackTrace()
            goToMainActivity()
        }
    }
}