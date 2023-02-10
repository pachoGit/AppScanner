package com.example.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject

class DeliveredStateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivered_state)

        val content = intent.getStringExtra("content")
        val imagePath = intent.getStringExtra("imagePath")
        val state = "0" //intent.getStringExtra("state")

        val textView = findViewById<TextView>(R.id.textCodeBar2).apply {
            text = content
        }

        val data = mutableMapOf<String, String?>("content" to content, "imagePath" to imagePath, "state" to state)

        // Boton del modo SELLO
        findViewById<Button>(R.id.btnStamp).setOnClickListener {
            data["mode"] = "02"
            val jsonData = toJsonData(data)
            Toast.makeText(this, jsonData.toString(), Toast.LENGTH_LONG).show()
        }

        // Boton del modo FIRMA
        findViewById<Button>(R.id.btnSignature).setOnClickListener {
            data["mode"] = "03"
            val jsonData = toJsonData(data)
            Toast.makeText(this, jsonData.toString(), Toast.LENGTH_LONG).show()
        }

        // Boton de REGRESAR
        // Solo elimino el estado seleccionado previamente
        findViewById<Button>(R.id.btnPrevious).setOnClickListener {
            data.remove("state")
            goToChooseStateActivity(data)
        }
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
}