package com.ulagos.myapplication.tmb

import ApiService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.slider.RangeSlider
import com.ulagos.myapplication.R
import com.ulagos.myapplication.tmb.Major
import com.ulagos.myapplication.tmb.MajorData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class postActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService // Asegúrate de inicializar esto

    private val key = "123e4567-e89b-12d3-a456-426614174000" // Ajusta según tu clave API
    private lateinit var spmajors: Spinner
    private lateinit var spgeneros: Spinner
    private lateinit var tvAge: TextView
    private lateinit var btnMinusAge: Button
    private lateinit var btnPlusAge: Button
    private lateinit var tvWeight: TextView
    private lateinit var btnMinusWeight: Button
    private lateinit var btnPlusWeight: Button
    private lateinit var spMajors: Spinner
    private lateinit var tvHeight: TextView
    private lateinit var spFood: Spinner
    private lateinit var tvWater: EditText
    private lateinit var tvSleepH: TextView
    private lateinit var btnMinusSleep: Button
    private lateinit var btnPlusSleep: Button
    private lateinit var spSleepQuality: Spinner
    private lateinit var spActivity: Spinner
    private lateinit var btSend: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        // Inicializa el Spinner después de setContentView
        spmajors = findViewById(R.id.sp_majors)
        spgeneros = findViewById(R.id.sp_generos)
        tvAge = findViewById(R.id.tvAge)
        btnMinusAge = findViewById(R.id.btnMinus)
        btnPlusAge = findViewById(R.id.btnPlus)
        tvWeight = findViewById(R.id.tvWeight)
        btnMinusWeight = findViewById(R.id.btnMinus1)
        btnPlusWeight = findViewById(R.id.btnPlus2)
        spMajors = findViewById(R.id.sp_majors)
        tvHeight = findViewById(R.id.tvHeight)
        spFood = findViewById(R.id.sp_food)
        tvWater = findViewById(R.id.TvWater)
        tvSleepH = findViewById(R.id.tvSleepH)
        btnMinusSleep = findViewById(R.id.btnMinus3)
        btnPlusSleep = findViewById(R.id.btnPlus4)
        spSleepQuality = findViewById(R.id.sp_SleepQuality)
        spActivity = findViewById(R.id.sp_Activity)
        btSend = findViewById(R.id.bt_Send)

        // Inicializa apiService aquí
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        apiService = Retrofit.Builder()
            .baseUrl("https://apirest.servery2k.link/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        getAllMajors(0, 10)
    }


        private fun getAllMajors(page: Int, limit: Int) {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        apiService.getMajors(page, limit, key)
                    }

                    if (response.isSuccessful) {
                        val majorListResponse = response.body()
                        majorListResponse?.let {
                            val majors = it.data
                            // Lista de cadenas directamente
                            Log.d("SpinnerData", "Majors: $majors")

                            val spinnerAdapter = ArrayAdapter(
                                applicationContext,
                                android.R.layout.simple_spinner_item,
                                majors
                            )
                            Log.d("SpinnerData", "Majors size: ${majors.size}")


                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spmajors.adapter = spinnerAdapter
                        }
                    } else {
                        // Manejar el error de la solicitud HTTP
                        Log.e("SpinnerData", "Error en la solicitud HTTP: ${response.code()}")
                    }
                } catch (e: Exception) {
                    // Manejar errores de red u otras excepciones
                    Log.e("SpinnerData", "Error en la solicitud: ${e.message}")
                }
            }
        }

    private fun enviarDatos() {
        val datos = DatosEnviar(
            gender = spgeneros.selectedItem.toString(),
            age = tvAge.text.toString().toInt(),
            major = spMajors.selectedItem.toString(),
            weight = tvWeight.text.toString().toDouble(),
            height = tvHeight.text.toString().toDouble(),
            dietQuality = spFood.selectedItem.toString(),
            waterIntake = tvWater.text.toString().toInt(),
            sleepHours = tvSleepH.text.toString().toInt(),
            sleepQuality = spSleepQuality.selectedItem.toString(),
            physicalActivity = spActivity.selectedItem.toString()
        )

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.enviarDatos(datos, key)
                }

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        // Manejar la respuesta según tus necesidades
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Manejar el error de la solicitud HTTP
                    Log.e("EnviarDatos", "Error en la solicitud HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otras excepciones
                Log.e("EnviarDatos", "Error en la solicitud: ${e.message}")
            }
        }
    }
    }


