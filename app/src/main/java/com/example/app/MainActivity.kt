package com.example.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.app.databinding.ActivityMainBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.camera.CameraManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnScanner.setOnClickListener { initScanner() }
        //binding.btnScanner.setOnClickListener { goToChooseStateActivity() }
        //CameraManager
    }

    /**
     * Inicia la camara para escanear el codigo de barras
     */
    private fun initScanner() {
        IntentIntegrator(this).initiateScan()
    }

    /**
     * Se llama cuando el escaner ha detectado algo
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                Toast.makeText(this, "El valor escaneado es: " + result.contents, Toast.LENGTH_LONG).show()
                goToChooseStateActivity()
            }
            else {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun goToChooseStateActivity() {
        val intent = Intent(this, ChooseStateActivity::class.java).apply {
            putExtra("Hola", "Tenemos")
        }
        startActivity(intent)
    }
}