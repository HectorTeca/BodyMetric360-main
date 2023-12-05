package com.ulagos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ulagos.myapplication.R
import com.ulagos.myapplication.imc.ImcActivity
import com.ulagos.myapplication.tmb.TmbActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnImc: Button
    private lateinit var btnTmb: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponent()
        initListener()
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
        val intent = Intent( this, TmbActivity::class.java)
        startActivity(intent)
    }
}