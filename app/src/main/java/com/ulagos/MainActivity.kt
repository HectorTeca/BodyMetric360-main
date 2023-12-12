package com.ulagos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ulagos.myapplication.R
import com.ulagos.myapplication.imc.ImcActivity
import com.ulagos.myapplication.tmb.TmbActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var btnImc: Button
    private lateinit var btnTmb: Button
    private lateinit var btnTyc: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponent()
        initListener()

        val botonMostrarTerminos = findViewById<Button>(R.id.btnTyc)

        botonMostrarTerminos.setOnClickListener {
            // Cuando se hace clic en el botón, realizar la solicitud y mostrar los términos
            obtenerYMostrarTerminos()
        }
    }

    private fun obtenerYMostrarTerminos() {
        val apiKey = TmbActivity.API_KEY

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val terminosResponse = TmbActivity.apiService.getTyc(apiKey)

                if (terminosResponse.isSuccessful) {
                    val apiResponse = terminosResponse.body()

                    apiResponse?.let {
                        // Concatenamos los términos y sus descripciones
                        val terminosConcatenados = it.data.joinToString("\n\n") { termListData ->
                            termListData.data.joinToString("\n") { termData ->
                                "${termData.title}: ${termData.description}"
                            }
                        }

                        // Actualizar la UI en el hilo principal
                        withContext(Dispatchers.Main) {
                            mostrarTerminosEnDialogo(terminosConcatenados)
                        }
                    }
                }
            } catch (e: Exception) {
                // Manejar el error
            }
        }
    }

    private fun mostrarResultado(title: String, description: String) {
        val alertDialog = AlertDialog.Builder(this@MainActivity)
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton("Aceptar", null)
            .show()
    }

    private fun mostrarError(message: String) {
        val alertDialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .show()
    }

    fun initComponent() {
        btnImc = findViewById(R.id.btnImc)
        btnTmb = findViewById(R.id.btnTmb)
    }

    fun initListener() {
        btnImc.setOnClickListener { navigateToImc() }
        btnTmb.setOnClickListener { navigateToTmb() }
    }

    private fun navigateToImc() {
        val intent = Intent(this, ImcActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToTmb() {
        val intent = Intent(this, TmbActivity::class.java)
        startActivity(intent)
    }

    private fun mostrarTerminosEnDialogo(terminos: String) {
        AlertDialog.Builder(this)
            .setTitle("Términos y Condiciones")
            .setMessage(terminos)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}