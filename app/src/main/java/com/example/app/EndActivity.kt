package com.example.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class EndActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)

        val textView = findViewById<TextView>(R.id.textView6).apply {
            text = "ENVIO EXITOSO"
        }
        // Accion del boton de VOLVER
        findViewById<Button>(R.id.btnHome).setOnClickListener { goToMainActivity() }
    }

    override fun onBackPressed() {
        goToMainActivity()
        finish()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}