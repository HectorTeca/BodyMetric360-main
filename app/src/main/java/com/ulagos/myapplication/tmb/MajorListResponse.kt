package com.ulagos.myapplication.tmb

data class Major(
    val id: Int?, // Ajusta según el tipo real de id en tu API
    val descripcion: String
)


data class MajorListResponse (
    val message: String,
    val data: List<MajorData>
)

data class MajorData(
    val majors: List<String>
)


