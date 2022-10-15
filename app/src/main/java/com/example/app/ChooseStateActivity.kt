package com.example.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ChooseStateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_state)

        val content = intent.getStringExtra("content")
        val imagePath = intent.getStringExtra("imagePath")

        // Insertamos el contenido del codigo de barras en el TextView
        val textView = findViewById<TextView>(R.id.textCodeBar).apply {
            text = content
        }

        val data: MutableMap<String, String?> = mutableMapOf("content" to content, "imagePath" to imagePath)

        // Accion del boton de ENTREGADO
        findViewById<Button>(R.id.btnDelivered).setOnClickListener {
            data["state"] = "0"
            goToDeliveredStateActivity(data)
        }

        // Accion del boton de RECHAZADO
        findViewById<Button>(R.id.btnRefused).setOnClickListener {
            data["state"] = "1"
            goToRefusedStateActivity(data)
        }

        // Accion del boton de VOLVER
        findViewById<Button>(R.id.btnTakePictureAgain).setOnClickListener { goToMainActivity() }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun goToDeliveredStateActivity(data: MutableMap<String, String?>) {
        val intent = Intent(this, DeliveredStateActivity::class.java).apply {
            data.map { (key, value) ->
                putExtra(key, value)
            }
        }
        startActivity(intent)
    }

    private fun goToRefusedStateActivity(data: MutableMap<String, String?>) {
        val intent = Intent(this, RefusedStateActivity::class.java).apply {
            data.map { (key, value) ->
                putExtra(key, value)
            }
        }
        startActivity(intent)
    }
}