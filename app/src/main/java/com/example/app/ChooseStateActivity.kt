package com.example.app

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.net.Uri
import android.provider.MediaStore
import java.io.InputStream

class ChooseStateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_state)

        val content = intent.getStringExtra("content")
        val imagePath = intent.getStringExtra("imagePath")
        val phone = intent.getStringExtra("phone")
        val extraImagesPath = intent.getStringExtra("extra")

        // Insertamos el contenido del codigo de barras en el TextView
        findViewById<TextView>(R.id.textCodeBar).apply {
            text = content

            if (extraImagesPath != null) {
                Toast.makeText(context, "Extras: " + extraImagesPath, Toast.LENGTH_SHORT).show()
            }

        }

        // Insertamos la imagen
        findViewById<ImageView>(R.id.imageView).apply {
            val image = BitmapFactory.decodeFile(imagePath)
            // If the path comes from the "start" button
            if (image != null) {
                setImageBitmap(image)
            }
            // If the path comes from the "manual insert" button
            else {
                val uri = Uri.parse(imagePath)
                if (Build.VERSION.SDK_INT < 28) {
                    var bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    setImageBitmap(bitmap)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    var bitmap = ImageDecoder.decodeBitmap(source)
                    setImageBitmap(bitmap)
                }
            }
        }

        val data: MutableMap<String, String?> = mutableMapOf(
            "content" to content,
            "imagePath" to imagePath,
            "phone" to phone,
            "extra" to extraImagesPath
        )

        // Accion del boton de ENTREGADO
        findViewById<Button>(R.id.btnDelivered).setOnClickListener {
            goToDeliveredStateActivity(data)
        }

        // Accion del boton de RECHAZADO
        findViewById<Button>(R.id.btnRefused).setOnClickListener {
            goToRefusedStateActivity(data)
        }

        // Accion del boton de VOLVER
        findViewById<Button>(R.id.btnTakePictureAgain).setOnClickListener { goToMainActivity() }
    }

    override fun onBackPressed() {
        goToMainActivity()
        finish()
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
