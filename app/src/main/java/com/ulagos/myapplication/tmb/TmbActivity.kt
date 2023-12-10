package com.ulagos.myapplication.tmb

import ApiService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ulagos.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TmbActivity : AppCompatActivity() {

    // Propiedades para la paginación
    private var currentPage = 1
    private val pageSize = 5  // Vamos a cargar de 5 en 5

    private lateinit var editTextId: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonSend: Button
    private lateinit var spinnerAction: Spinner
    private lateinit var buttonGetAll: Button
    private lateinit var buttonPrevious: Button
    private lateinit var buttonNext: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter

    private val key = "123e4567-e89b-12d3-a456-426614174000"  // Tu UUID aleatorio


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


    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmb)

        initComponent()
        initListener()
    }

    private fun initComponent() {
        // Inicialización de los componentes EditText y Button
        editTextId = findViewById(R.id.editTextId)
        editTextName = findViewById(R.id.editTextName)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonSend = findViewById(R.id.buttonSend)

        spinnerAction = findViewById(R.id.spinnerAction)
        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAction.adapter = adapter
        }

        recyclerView = findViewById(R.id.recyclerViewUsers)
        usersAdapter = UsersAdapter(emptyList())

        buttonGetAll = findViewById(R.id.buttonGetAll)
        buttonPrevious = findViewById(R.id.buttonPrevious)
        buttonNext = findViewById(R.id.buttonNext)
    }

    private fun initListener() {
        buttonSend.setOnClickListener {
            val selectedAction = spinnerAction.selectedItem.toString()
            when (selectedAction) {
                "Consultar" -> getUser()
                "Editar" -> updateUser()
                "Agregar" -> addUser()
                "Eliminar" -> deleteUser()
            }
        }

        buttonGetAll.setOnClickListener {
            getAllUsers(currentPage, pageSize)
        }

        buttonPrevious.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                getAllUsers(currentPage, pageSize)
            }
        }

        buttonNext.setOnClickListener {
            // Aquí debes tener alguna lógica para no intentar cargar páginas que no existen
            currentPage++
            getAllUsers(currentPage, pageSize)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = usersAdapter
    }

    private fun getUser() {
        val userId = editTextId.text.toString()

        // Verifica si el ID del usuario está vacío
        if (userId.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un ID de usuario.", Toast.LENGTH_SHORT).show()
            return
        }

        // Iniciar una corutina en el contexto de la UI
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Hacer la llamada de forma suspensa en un hilo de fondo
                val response = withContext(Dispatchers.IO) {
                    apiService.getUser(userId, key)
                }

                // Manejar la respuesta
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        editTextId.setText(apiResponse.data.id.toString())
                        editTextName.setText(apiResponse.data.nombre)
                        editTextDescription.setText(apiResponse.data.descripcion)
                    }
                    showToast("Datos obtenidos con exito")
                } else {
                    // Manejar los casos de error
                    showToast("Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                // Manejar cualquier excepción que se haya lanzado durante la llamada de red
                showToast("Error en la solicitud: ${e.message}")
            }
        }
    }

    private fun updateUser() {
        val userId = editTextId.text.toString()
        val userName = editTextName.text.toString()
        val userDescription = editTextDescription.text.toString()

        // Verifica que los campos necesarios no estén vacíos
        if (userId.isEmpty() || userName.isEmpty() || userDescription.isEmpty()) {
            Toast.makeText(
                this,
                "Todos los campos son necesarios para la edición",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val userData =
            UserData(id = userId.toInt(), nombre = userName, descripcion = userDescription)

        // Aquí, realiza la llamada a la API para actualizar
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.updateUser(userId, userData, key)
                }

                // Maneja la respuesta de la API
                if (response.isSuccessful) {
                    // Manejo de éxito
                    showToast("Registro actualizado con exito")
                    resetForm()  // Resetea el formulario después de obtener los datos
                } else {
                    // Manejar los casos de error
                    showToast("Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                // Manejo de excepción
                showToast("Error en la solicitud: ${e.message}")
            }
        }
    }

    private fun addUser() {
        val userName = editTextName.text.toString()
        val userDescription = editTextDescription.text.toString()

        if (userName.isEmpty() || userDescription.isEmpty()) {
            showToast("Nombre y descripción son necesarios para agregar")
            return
        }

        val newUser = UserData(nombre = userName, descripcion = userDescription)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.addUser(newUser, key)
                }

                if (response.isSuccessful) {
                    resetForm()
                    showToast("Usuario agregado con éxito")
                } else {
                    showToast("Error al agregar usuario: ${response.code()}")
                }
            } catch (e: Exception) {
                showToast("Error en la solicitud: ${e.message}")
            }
        }
    }

    private fun deleteUser() {
        val userId = editTextId.text.toString()
        if (userId.isEmpty()) {
            showToast("ID del usuario es necesario para eliminar")
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.deleteUser(userId, key)
                }

                if (response.isSuccessful) {
                    resetForm()
                    showToast("Usuario eliminado con éxito")
                } else {
                    showToast("Error al eliminar usuario: ${response.code()}")
                }
            } catch (e: Exception) {
                showToast("Error en la solicitud: ${e.message}")
            }
        }
    }

    private fun getAllUsers(page: Int, limit: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getUsers(page, limit, key)
                }

                if (response.isSuccessful) {
                    val userListResponse = response.body()
                    userListResponse?.let {
                        // Actualiza tu RecyclerView con la lista de usuarios
                        updateRecyclerView(it.data)
                    }
                } else {
                    showToast("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                showToast("Error en la solicitud: ${e.message}")
            }
        }
    }
    private fun updateRecyclerView(users: List<UserData>) {
        usersAdapter.updateUsers(users)
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun resetForm() {
        editTextId.setText("")
        editTextName.setText("")
        editTextDescription.setText("")
    }

    private fun mostrarDialogoInformacion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Información Adicional")
        builder.setMessage("Aquí va la información adicional que deseas mostrar.")
        builder.setPositiveButton("Entendido") { dialog, _ ->
            // Puedes hacer algo cuando el usuario hace clic en el botón "Entendido"
            dialog.dismiss()
        }
        builder.show()
    }
}