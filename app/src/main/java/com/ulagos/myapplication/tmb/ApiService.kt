import com.ulagos.myapplication.tmb.ApiResponse
import com.ulagos.myapplication.tmb.TermData
import com.ulagos.myapplication.tmb.TermListData
import com.ulagos.myapplication.tmb.TermsApiResponse
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

interface ApiService {
    @GET("user/{userId}")
    suspend fun getUser(
        @Path("userId") userId: String,
        @Header("X-API-KEY") apiKey: String
    ): Response<ApiResponse>

    @PUT("user/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: String,
        @Body userData: UserData,
        @Header("X-API-KEY") apiKey: String
    ): Response<ApiResponse>

    @POST("user")
    suspend fun addUser(
        @Body newUser: UserData,
        @Header("X-API-KEY") apiKey: String
    ): Response<ApiResponse>

    @DELETE("user/{userId}")
    suspend fun deleteUser(
        @Path("userId") userId: String,
        @Header("X-API-KEY") apiKey: String
    ): Response<ApiResponse>

    // Método para obtener una lista de usuarios
    @GET("users")
    suspend fun getUsers(
        @Query("start") start: Int,
        @Query("limit") limit: Int,
        @Header("X-API-KEY") apiKey: String
    ): Response<UserListResponse>

    // Método para obtener los terminos y condiciones de uso
    @GET("terms")
    suspend fun getTyc(
        @Header("X-API-KEY") apiKey: String
    ): Response<TermsApiResponse>
}



