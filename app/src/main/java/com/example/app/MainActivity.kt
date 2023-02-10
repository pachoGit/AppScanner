package com.example.app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
    }

    /**
     * Inicia la camara para escanear el codigo de barras
     */
    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setBeepEnabled(true) // Habilitar el sonido al escanear
        integrator.setBarcodeImageEnabled(true) // Habilitar que se guarde la foto
        integrator.initiateScan()
    }

    /**
     * Se llama cuando el escaner ha detectado algo
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val data: Map<String, String> = mapOf("content" to result.contents, "imagePath" to result.barcodeImagePath)
                goToChooseStateActivity(data)
                //Toast.makeText(this, "El valor escaneado es: " + result.contents + result.barcodeImagePath, Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, "No se logró obtener el contenido", Toast.LENGTH_LONG).show()
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun goToChooseStateActivity(data: Map<String, String>) {
        val intent = Intent(this, ChooseStateActivity::class.java).apply {
            data.map { (key, value) ->
                putExtra(key, value)
            }
        }
        startActivity(intent)
    }

}