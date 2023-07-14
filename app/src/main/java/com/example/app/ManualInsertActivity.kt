@file:OptIn(ExperimentalLayoutApi::class)

package com.example.app

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ManualInsertActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                ) {
                    MyHeader()
                    Greeting()
                }
            }
        }
    }
}

data class FormUiState(
        val barcode: String = "",
        val uriBarcode: Uri? = null,
        val bitmapBarcode: Bitmap? = null,
        val uriExtra: List<Uri>? = null,
        val bitmapExtra: List<Bitmap>? = null,
)

class FormViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FormUiState())
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()

    fun updateBarcode(barcode: String) {
        _uiState.value = _uiState.value.copy(barcode = barcode)
    }

    fun updateUriBarcode(uriBarcode: Uri?) {
        _uiState.value = _uiState.value.copy(uriBarcode = uriBarcode)
    }

    fun updateBitmapBarcode(bitmapBarcode: Bitmap?) {
        _uiState.value = _uiState.value.copy(bitmapBarcode = bitmapBarcode)
    }

    fun updateUriExtra(uriExtra: List<Uri>?) {
        _uiState.value = _uiState.value.copy(uriExtra = uriExtra)
    }

    fun updateBitmapExtra(bitmapExtra: List<Bitmap>?) {
        _uiState.value = _uiState.value.copy(bitmapExtra = bitmapExtra)
    }
}

@Composable
fun Greeting(formViewModel: FormViewModel = viewModel()) {

    val formUiState by formViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
                    uri: Uri? ->
                formViewModel.updateUriBarcode(uri)
            }

    val multiLauncher =
            rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetMultipleContents()
            ) { uris: List<Uri>? -> formViewModel.updateUriExtra(uris) }

    FlowColumn(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FlowColumn(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp),
        ) {
            OutlinedTextField(
                    value = formUiState.barcode,
                    onValueChange = { formViewModel.updateBarcode(it) },
                    modifier = Modifier,
                    label = { Text("Código de barras") },
                    placeholder = { Text("Ingrese el código de barras") }
            )
        }
        FlowColumn(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp),
        ) {
            Column() {
                Button(
                        onClick = { launcher.launch("image/*") },
                        shape = RectangleShape,
                ) { Text(text = "Seleccione una imagen") }
            }

            Column() {
                formUiState.uriBarcode?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        var bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                        formViewModel.updateBitmapBarcode(bitmap)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        var bitmap = ImageDecoder.decodeBitmap(source)
                        formViewModel.updateBitmapBarcode(bitmap)
                    }
                }

                formUiState.bitmapBarcode?.let { btm ->
                    Image(
                            bitmap = btm.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp)
                    )
                }
            }
        }

        FlowColumn(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp),
        ) {
            Button(
                    onClick = { multiLauncher.launch("image/*") },
                    shape = RectangleShape,
            ) { Text(text = "Seleccione extra imagenes") }

            ImageList(formViewModel, formUiState)
        }

        FlowColumn(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp),
        ) {
            Column() {
                Button(
                        onClick = {},
                        shape = RectangleShape,
                ) { Text(text = "Enviar") }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme { Greeting() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHeader() {
    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("NSP Courier") },
                        colors =
                                TopAppBarDefaults.smallTopAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                ),
                )
            },
            contentColor = MaterialTheme.colorScheme.primary,
    ) {}
}

@Composable
fun ImageList(formViewModel: FormViewModel, formUiState: FormUiState) {
    val context = LocalContext.current

    var listBitmap: MutableList<Bitmap>? = mutableListOf()
    formUiState.uriExtra?.forEach {
        if (Build.VERSION.SDK_INT < 28) {
            var bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            listBitmap?.add(bitmap)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, it)
            var bitmap = ImageDecoder.decodeBitmap(source)
            listBitmap?.add(bitmap)
        }
    }

    formViewModel.updateBitmapExtra(listBitmap)

    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(formUiState.bitmapExtra?.size ?: 0) { index ->
            var image = formUiState.bitmapExtra?.get(index)
            Image(
                    bitmap = image!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp).padding(5.dp)
            )
        }
    }
}
