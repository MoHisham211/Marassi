import com.google.gson.annotations.SerializedName
import mo.zain.marassi.model.LogOutData
import mo.zain.marassi.model.LoginData
import mo.zain.marassi.model.LoginResponse
import mo.zain.marassi.model.UpdateUserInfoResponse
import mo.zain.marassi.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("register/")
    fun registerUser(@Body userData: UserData): Call<RegistrationResponse>

    @POST("login/")
    fun loginUser(@Body loginData: LoginData): Call<LoginResponse>

    @POST("logout/")
    fun logOutUser(@Header("Authorization") token:String): Call<LogOutData>

    /*@PUT("update_user_info/")
    fun updateProfile(@Header("Authorization")token:String ,@Body userData: UserData):Call<UpdateUserInfoResponse>*/

    @Multipart
    @POST("hisham/")
    fun uploadPhoto(
        @Header("Authorization") authorization: String,
        @Part photo: MultipartBody.Part
    ): Call<UploadResponse>

    @Multipart
    @PUT("update_user_info/")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part IDCard: MultipartBody.Part,
    ): Call<UpdateUserInfoResponse>




    @PUT("update_user_info/")
    fun updateProfileInfo(@Header("Authorization")token:String
                          ,@Body userData: UserData):Call<UpdateUserInfoResponse>


    @Multipart
    @PUT("update_user_infoo/")
    fun updateProfileoo(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part IDCard: MultipartBody.Part,
        @Part("email") email:String

    ): Call<UpdateUserInfoResponse>



}
