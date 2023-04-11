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
import android.content.pm.PackageManager
import android.Manifest
import android.content.Context
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import android.os.Build

import android.Manifest.permission.READ_PHONE_NUMBERS
import android.Manifest.permission.READ_PHONE_STATE
import android.Manifest.permission.READ_SMS
import android.util.Log
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var phone: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadPhoneNumber()
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
                val data: Map<String, String> = mapOf("content" to result.contents, "imagePath" to result.barcodeImagePath, "phone" to this.phone)
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
    override fun onBackPressed() {
        finishAffinity()
    }

    private fun goToChooseStateActivity(data: Map<String, String>) {
        val intent = Intent(this, ChooseStateActivity::class.java).apply {
            data.map { (key, value) ->
                putExtra(key, value)
            }
        }
        startActivity(intent)
    }

    private fun loadPhoneNumber() {
        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // Permission check

            // Create obj of TelephonyManager and ask for current telephone service
            val telephonyManager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            this.phone = telephonyManager.line1Number
            println("Phone" + this.phone)
        } else {
            // Ask for permission
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE), 101)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                val telephonyManager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
                if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                this.phone = telephonyManager.line1Number
                println("Phone" + this.phone)
            }
            else -> throw IllegalStateException("Unexpected value: $requestCode")
        }
    }
}