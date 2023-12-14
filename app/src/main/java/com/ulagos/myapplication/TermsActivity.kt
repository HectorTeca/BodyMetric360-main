package com.ulagos.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ulagos.myapplication.tmb.TmbActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        obtenerYMostrarTerminos()
    }

    private fun obtenerYMostrarTerminos() {
        val apiKey = TmbActivity.API_KEY

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val terminosResponse = TmbActivity.apiService.getTyc(apiKey)

                if (terminosResponse.isSuccessful) {
                    val apiResponse = terminosResponse.body()

                    apiResponse?.let {
                        val terminosConcatenados = it.data?.filterNotNull()?.joinToString("\n\n") { termListData ->
                            termListData.data?.filterNotNull()?.joinToString("\n") { termData ->
                                "${termData.title ?: "Title null"}: ${termData.description ?: "Description null"}"
                            } ?: ""
                        } ?: ""

                        withContext(Dispatchers.Main) {
                            mostrarTerminos(terminosConcatenados)
                        }
                    }
                }
            } catch (e: Exception) {
                // Manejar el error
                mostrarError("Error al obtener los términos: ${e.message}")
            }
        }
    }

    private fun mostrarTerminos(terminos: String) {
        // Llama a findViewById dentro del bloque withContext(Dispatchers.Main)
        val textViewTerminos = findViewById<TextView>(R.id.textViewTerminos)
        textViewTerminos.text = terminos
    }

    private fun mostrarError(message: String) {
        // Muestra un diálogo de error o realiza alguna otra acción de manejo de errores
        // según tus necesidades.
        Log.e("TermsActivity", message)
    }
}