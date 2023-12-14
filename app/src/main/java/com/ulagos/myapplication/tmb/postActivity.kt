package com.ulagos.myapplication.tmb

import ApiService
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
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
import kotlin.math.roundToInt

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
    private lateinit var slHeight : RangeSlider
    var isDecreasing = false
    var isIncreasing = false
    var isDecreasingW = false
    var isIncreasingW = false
    var isDecreasingH = false
    var isIncreasingH = false


    @SuppressLint("ClickableViewAccessibility")
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
        slHeight = findViewById<RangeSlider>(R.id.slHeight)
        btSend = findViewById(R.id.bt_Send)

        // Spinner para géneros
        val generosArray = resources.getStringArray(R.array.generos)
        val spGeneros = findViewById<Spinner>(R.id.sp_generos)
        val generosAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generosArray)
        generosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spGeneros.adapter = generosAdapter

// Spinner para comida chatarra
        val comidachatarraArray = resources.getStringArray(R.array.comidachatarra)
        val spFood = findViewById<Spinner>(R.id.sp_food)
        val comidachatarraAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, comidachatarraArray)
        comidachatarraAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spFood.adapter = comidachatarraAdapter

// Spinner para calidad de sueño
        val calidadsuenoArray = resources.getStringArray(R.array.calidadsueño)
        val spSleepQuality = findViewById<Spinner>(R.id.sp_SleepQuality)
        val calidadsuenoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calidadsuenoArray)
        calidadsuenoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spSleepQuality.adapter = calidadsuenoAdapter

// Spinner para nivel de actividad
        val nivelactividadArray = resources.getStringArray(R.array.nivelactividad)
        val spActivity = findViewById<Spinner>(R.id.sp_Activity)
        val nivelactividadAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nivelactividadArray)
        nivelactividadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spActivity.adapter = nivelactividadAdapter


        val btnMinus = findViewById<Button>(R.id.btnMinus)
        val btnPlus = findViewById<Button>(R.id.btnPlus)
        val btnMinusW = findViewById<Button>(R.id.btnMinus1)
        val btnPlusW = findViewById<Button>(R.id.btnPlus2)
        val btnMinusH = findViewById<Button>(R.id.btnMinus3)
        val btnPlusH = findViewById<Button>(R.id.btnPlus4)
        val tvAge = findViewById<TextView>(R.id.tvAge)

        btnMinus.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isDecreasing = true
                    startChangingAge()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isDecreasing = false
                }
            }
            true
        }

        btnPlus.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isIncreasing = true
                    startChangingAge()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isIncreasing = false
                }
            }
            true
        }

        btnMinusW.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isDecreasingW = true
                    startChangingWeight()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isDecreasingW = false
                }
            }
            true
        }

        btnPlusW.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isIncreasingW = true
                    startChangingWeight()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isIncreasingW = false
                }
            }
            true
        }

        btnMinusH.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isDecreasingH = true
                    startChangingSleep()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isDecreasingH = false
                }
            }
            true
        }

        btnPlusH.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isIncreasingH = true
                    startChangingSleep()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isIncreasingH = false
                }
            }
            true
        }
        slHeight.addOnChangeListener { slider, _, _ ->
            // Obtén el valor actual del RangeSlider
            val sliderValue = slider.values[0].roundToInt()
            // Convierte el valor a String y actuliza el TextView
            tvHeight.text = sliderValue.toString()
        }

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

        btSend.setOnClickListener {
            // Aquí llamamos la función de envío POST
            enviarDatos()
        }

    }

    private fun startChangingAge() {
        Thread {
            while (isDecreasing || isIncreasing) {
                runOnUiThread {
                    if (isDecreasing) {
                        decreaseAge(tvAge)
                    } else if (isIncreasing) {
                        increaseAge(tvAge)
                    }
                }
                Thread.sleep(100) // Ajusta el retraso según tus necesidades
            }
        }.start()
    }
    private fun decreaseAge(tv: TextView) {
        // Lógica para disminuir la edad
        val currentAge = tv.text.toString().toIntOrNull() ?: 0
        if (currentAge > 0) {
            tv.text = (currentAge - 1).toString()
        }

    }
    private fun increaseAge(tv: TextView) {
        // Lógica para aumentar la edad
        val currentAge = tv.text.toString().toIntOrNull() ?: 0
        if (currentAge < 99) {
            tv.text = (currentAge + 1).toString()
        }
    }
    private fun startChangingWeight() {
        Thread {
            while (isDecreasingW || isIncreasingW) {
                runOnUiThread {
                    if (isDecreasingW) {
                        decreaseWeight(tvWeight)
                    } else if (isIncreasingW) {
                        increaseWeight(tvWeight)
                    }
                }
                Thread.sleep(50) // Ajusta el retraso según tus necesidades
            }
        }.start()
    }
    private fun decreaseWeight(tv: TextView) {
        val currentWeight = tv.text.toString().toIntOrNull() ?: 0
        if (currentWeight > 0) {
            tv.text = (currentWeight - 1).toString()
        }
    }
    private fun increaseWeight(tv: TextView) {
        val currentWeight = tv.text.toString().toIntOrNull() ?: 0
        if (currentWeight < 300) {
            tv.text = (currentWeight + 1).toString()
        }
    }
    private fun startChangingSleep() {
        Thread {
            while (isDecreasingH || isIncreasingH) {
                runOnUiThread {
                    if (isDecreasingH) {
                        decreaseSleep(tvSleepH)
                    } else if (isIncreasingH) {
                        increaseSleep(tvSleepH)
                    }
                }
                Thread.sleep(100) // Ajusta el retraso según tus necesidades
            }
        }.start()
    }
    private fun decreaseSleep(tv: TextView) {
        val currentSleepH = tv.text.toString().toIntOrNull() ?: 0
        if (currentSleepH > 1) {
            tv.text = (currentSleepH - 1).toString()
        }
    }
    private fun increaseSleep(tv: TextView) {
        val currentSleepH= tv.text.toString().toIntOrNull() ?: 0
        if (currentSleepH < 8) {
            tv.text = (currentSleepH + 1).toString()
        }
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
        val selectedPosition = spSleepQuality.selectedItemPosition
        val sleepQualityValue = when (selectedPosition) {
            0 -> "Muy Mala"
            1 -> "Mala"
            2 -> "Regular"
            3 -> "Buena"
            4 -> "Excelente"
            else -> "Desconocido"
        }
        val selectedActivityPosition = spActivity.selectedItemPosition
        val activityLevelValue = when (selectedActivityPosition) {
            0 -> "Sedentario"
            1 -> "Ligeramente Activo"
            2 -> "Moderadamente Activo"
            3 -> "Muy Activo"
            else -> "Desconocido"
        }
        var cups = tvWater.text.toString().toInt()
        if (cups > 8){
            cups = 8
        }
        val datos = DatosEnviar(
            gender = spgeneros.selectedItem.toString(),
            age = tvAge.text.toString().toInt(),
            major = spMajors.selectedItem.toString(),
            weight = tvWeight.text.toString().toDouble(),
            height = tvHeight.text.toString().toDouble()/100,
            dietQuality = spFood.selectedItem.toString(),
            waterIntake = cups,
            sleepHours = tvSleepH.text.toString().toInt(),
            sleepQuality = sleepQualityValue,
            physicalActivity = activityLevelValue
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


