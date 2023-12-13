package com.ulagos.myapplication.tmb

data class ApiResponse(
    val message: String,
    val data: UserData
)


data class UserData(
    val id: Int? = null,
    val nombre: String,
    val descripcion: String
)

data class DatosEnviar(
    val gender: String,
    val age: Int,
    val major: String,
    val weight: Double,
    val height: Double,
    val dietQuality: String,
    val waterIntake: Int,
    val sleepHours: Int,
    val sleepQuality: String,
    val physicalActivity: String
)




