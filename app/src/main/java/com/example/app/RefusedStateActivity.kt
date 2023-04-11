package com.example.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.sql.SQLException

class RefusedStateActivity : AppCompatActivity() {

    private val sql = SQLServer()

    private val ftp = FTP()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refused_state)

        val content = intent.getStringExtra("content")
        val imagePath = intent.getStringExtra("imagePath")
        val state = "1" //intent.getStringExtra("state")
        val phone = intent.getStringExtra("phone")

        val textView = findViewById<TextView>(R.id.textCodeBar3).apply {
            text = content
        }

        val data = mutableMapOf<String, String?>("content" to content, "imagePath" to imagePath, "state" to state, "phone" to phone)

        // Boton del modo SE MUDO
        findViewById<Button>(R.id.btnMoved).setOnClickListener {
            data["mode"] = "01"
            //Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show()
            executeQuery(data)
        }

        // Boton del modo CERRADO
        findViewById<Button>(R.id.btnClosed).setOnClickListener {
            data["mode"] = "04"
            //Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show()
            executeQuery(data)
        }

        // Boton del modo RECHAZADO
        findViewById<Button>(R.id.btnRejected).setOnClickListener {
            data["mode"] = "07"
            //Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show()
            executeQuery(data)
        }

        // Boton del modo DIRECCION ERRATA
        findViewById<Button>(R.id.btnWrongAddress).setOnClickListener {
            data["mode"] = "10"
            //Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show()
            executeQuery(data)
        }

        // Boton de REGRESAR
        // Solo elimino el estado seleccionado previamente
        findViewById<Button>(R.id.btnPrevious2).setOnClickListener {
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
            var response = sql.executeStoredProcedure(data["content"], data["state"], data["mode"], data["phone"], "")
            //Toast.makeText(this, "Se ejecutó la consulta", Toast.LENGTH_LONG).show()
            sql.disconnect()

            ftp.connect()
            ftp.sendImage(data["imagePath"], data["content"] + ".jpg")
            ftp.disconnect()

            goToEndActivity()
        }
        catch (e: SQLException) {
            Toast.makeText(this, "Internal Error", Toast.LENGTH_LONG).show()
            e.printStackTrace()
            goToMainActivity()
        }
    }
}