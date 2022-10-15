package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ChooseStateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_state)

        val message = intent.getStringExtra("Hola")
        // Capture the layout's TextView and set the string as its text
        val textView = findViewById<TextView>(R.id.textCodeBar).apply {
            text = message
        }

    }
}