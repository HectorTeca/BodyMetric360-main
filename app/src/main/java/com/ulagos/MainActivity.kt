package com.ulagos

import ApiServiceTyC
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.widget.Button
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.ulagos.myapplication.R
import com.ulagos.myapplication.imc.ImcActivity
import com.ulagos.myapplication.tmb.postActivity
import kotlinx.coroutines.CoroutineScope
import android.util.Log
import android.widget.TextView
import com.ulagos.myapplication.tmb.ApiResponse
import com.ulagos.myapplication.tmb.TermsData
import com.ulagos.myapplication.tmb.TyCResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    private val apiService: ApiServiceTyC = Retrofit.Builder()
        .baseUrl("https://apirest.servery2k.link/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiServiceTyC::class.java)

    private val key = "123e4567-e89b-12d3-a456-426614174000" // Ajusta según tu clave API
    //private lateinit var btnImc: Button
    private lateinit var btnTmb: Button
    private lateinit var btnInformacion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        btnInformacion = findViewById(R.id.btnInformacion)
//        btnInformacion.setOnClickListener {
//            obtenerYMostrarTerminos()
//        }
        initComponent()
        initListener()

    }

    fun initComponent() {
        btnTmb = findViewById(R.id.btnTmb)
    }

    fun initListener() {
       // btnImc.setOnClickListener { navigateToImc() }
        btnTmb.setOnClickListener { obtenerYMostrarTerminos() }
    }

    private fun obtenerYMostrarTerminos() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response: Response<TyCResponse> = withContext(Dispatchers.IO) {
                    apiService.getTyC(start = 0, limit = 10, apiKey = key)
                }

                if (response.isSuccessful) {
                    val termsResponse = response.body()

                    termsResponse?.let { response ->
                        val termsStringBuilder = StringBuilder()

                        response.data?.let { data ->
                            for (termData in data) {
                                termsStringBuilder.append("<b>${termData.title}</b><br/>${termData.description}<br/><br/>")
                            }
                        }
                        // Inflar el layout del popup
                        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val popupView: View = inflater.inflate(R.layout.popup_layout, null)

                        // Configurar el PopupWindow y mostrarlo
                        val popupWindow = PopupWindow(
                            popupView,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            true
                        )
                        popupWindow.setBackgroundDrawable(resources.getDrawable(android.R.color.white))

                        // Encontrar el TextView dentro del layout del popup
                        val textViewTerminos: TextView = popupView.findViewById(R.id.terms)


                        // Establecer el texto en el TextView
                        textViewTerminos.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(termsStringBuilder.toString(), Html.FROM_HTML_MODE_LEGACY)
                        } else {
                            @Suppress("DEPRECATION")
                            Html.fromHtml(termsStringBuilder.toString())
                        }
                        // Mostrar el PopupWindow en el centro de la pantalla
                        popupWindow.showAtLocation(
                            findViewById(android.R.id.content),
                            Gravity.CENTER,
                            0,
                            0
                        )
                        val btnCancel: Button = popupView.findViewById(R.id.btnCancel)
                        btnCancel.setOnClickListener {
                            popupWindow.dismiss()
                        }

                        val btnAccept: Button = popupView.findViewById(R.id.btnAccept)
                        btnAccept.setOnClickListener {
                            navigateToTmb()
                            popupWindow.dismiss()
                        }
                    }
                } else {
                    Log.e("ObtenerTerminos", "Error en la solicitud HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ObtenerTerminos", "Error en la solicitud: ${e.message}")
            }
        }
    }


    private fun navigateToImc() {
        val intent = Intent(this, ImcActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToTmb() {
        val intent = Intent( this, postActivity::class.java)
        startActivity(intent)
    }

}