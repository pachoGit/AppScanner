package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class DeliveredStateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivered_state)

        val content = intent.getStringExtra("content")
        val imagePath = intent.getStringExtra("imagePath")
        val state = intent.getStringExtra("state")

        val textView = findViewById<TextView>(R.id.textCodeBar2).apply {
            text = content
        }

        val data = mutableMapOf<String, String?>("content" to content, "imagePath" to imagePath, "state" to state)

        // Boton del modo SELLO
        findViewById<Button>(R.id.btnStamp).setOnClickListener {
            data["mode"] = "02"
            Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show()
        }

        // Boton del modo FIRMA
        findViewById<Button>(R.id.btnSignature). setOnClickListener {
            data["mode"] = "03"
            Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show()
        }
    }

    //private fun showData()
}