package com.ulagos.myapplication.tmb

import ApiService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.ulagos.myapplication.R
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        spmajors = findViewById(R.id.sp_majors)
        getAllMajors(0, 10)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://apirest.servery2k.link/")
        .client(okHttpClient) // Utiliza el cliente OkHttp con el interceptor
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private fun getAllMajors(page: Int, limit: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getMajors(page, limit, key)
                }

                if (response.isSuccessful) {
                    val majorListResponse = response.body()
                    majorListResponse?.let {
                        val majors = it.data.map { majorData ->
                            Major(majorData.message, majorData.data)
                        }

                        val spinnerAdapter = ArrayAdapter<Major>(
                            applicationContext,
                            android.R.layout.simple_spinner_item,
                            majors
                        )

                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spmajors.adapter = spinnerAdapter
                    }
                } else {
                    // Manejar el error de la solicitud HTTP
                    // Puedes mostrar un mensaje de error o realizar otras acciones según sea necesario
                }
            } catch (e: Exception) {
                // Manejar errores de red u otras excepciones
            }
        }
    }

}