package com.example.app

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Bundle
import android.os.Build
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.LinearLayout
import android.net.Uri
import android.provider.MediaStore
import java.io.InputStream

class ChooseStateActivity : AppCompatActivity() {

    private lateinit var imageContainer: LinearLayout // Contenedor de imágenes

    private var selectedImages = mutableListOf<Uri>() // Lista de imágenes seleccionadas

    private var data = mutableMapOf<String, String?>();

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

        data = mutableMapOf(
            "content" to content,
            "imagePath" to imagePath,
            "phone" to phone,
            "extra" to extraImagesPath
        )

        // Busca las vistas por su ID
        imageContainer = findViewById(R.id.imageContainer)

        // Insertamos las imagenes que vienen de la otra vista, si es que existen
        val extra = extraImagesPath?.split(", ")
        if (extra != null) {
            val a = extra.map  { Uri.parse(it) }
            selectedImages = a.toMutableList()
            displaySelectedImages()
        }


        findViewById<Button>(R.id.btnAddExtra).setOnClickListener {
            openFilePicker.launch("image/*")
        }


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


    // Callback para manejar la selección de imágenes desde el gestor de archivos
    private val openFilePicker =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri>? ->
            uris?.let {
                selectedImages.clear()
                selectedImages.addAll(uris)
                displaySelectedImages()
                data["extra"] = selectedImages.joinToString() { it.toString() }
            }
        }

    // Método para mostrar las imágenes seleccionadas en el HorizontalScrollView
    private fun displaySelectedImages() {
        imageContainer.removeAllViews() // Borra las vistas previas

        for (imageUri in selectedImages) {
            val imageView = ImageView(this)
            imageView.setImageURI(imageUri)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            imageView.layoutParams = params
            imageContainer.addView(imageView)
        }
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
