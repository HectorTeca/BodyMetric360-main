package com.ulagos.myapplication.tmb

data class ApiResponse(
    val message: String,
    val data: UserData,
    val tyc: terminos
)

data class UserData(
    val id: Int? = null,
    val nombre: String,
    val descripcion: String
)

data class terminos(
    val title: String,
    val description: String
)

