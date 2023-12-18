import com.ulagos.myapplication.tmb.ApiResponse
import com.ulagos.myapplication.tmb.DatosEnviar
import com.ulagos.myapplication.tmb.MajorListResponse
import com.ulagos.myapplication.tmb.TyCResponse
import com.ulagos.myapplication.tmb.UserData
import com.ulagos.myapplication.tmb.UserListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceTyC {
    @GET("bodymetric360/terms")
    suspend fun getTyC(
        @Query("start") start: Int,
        @Query("limit") limit: Int,
        @Header("X-API-KEY") apiKey: String
    ): Response<TyCResponse>
}

