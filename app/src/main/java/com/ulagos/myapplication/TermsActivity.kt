package com.ulagos.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class TermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        val terminos = intent.getStringExtra("terminos")
        mostrarTerminos(terminos)
    }

    private fun mostrarTerminos(terminos: String?) {
        val textViewTerminos = findViewById<TextView>(R.id.textViewTerminos)
        textViewTerminos.text = terminos ?: "No se pudieron cargar los términos y condiciones."
    }
}