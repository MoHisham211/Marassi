import mo.zain.marassi.model.ForgetPasswordRequest
import mo.zain.marassi.model.ForgetPasswordResponse
import mo.zain.marassi.model.GetAllRequests
import mo.zain.marassi.model.LogOutData
import mo.zain.marassi.model.LoginData
import mo.zain.marassi.model.LoginResponse
import mo.zain.marassi.model.ProfileResponse
import mo.zain.marassi.model.SeaPortItems
import mo.zain.marassi.model.UpdateUserInfoResponse
import mo.zain.marassi.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

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


    //UpdatePoth
    @Multipart
    @PUT("update_user_info/")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part?,
        @Part IDCard: MultipartBody.Part?,
    ): Call<UpdateUserInfoResponse>


    //update only profile image
    @Multipart
    @PUT("update_user_info/")
    fun updateProfileImage(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part?,
    ): Call<UpdateUserInfoResponse>


    //update only profile Card
    @Multipart
    @PUT("update_user_info/")
    fun updateProfileCard(
        @Header("Authorization") token: String,
        @Part IDCard: MultipartBody.Part?,
    ): Call<UpdateUserInfoResponse>


    @Multipart
    @PUT("update_user_info/")
    fun updatePassport(
        @Header("Authorization") token: String,
        @Part Passport: MultipartBody.Part?,
    ): Call<UpdateUserInfoResponse>


    @Multipart
    @PUT("update_user_info/")
    fun updateOthers(
        @Header("Authorization") token: String,
        @Part Others: MultipartBody.Part?,
    ): Call<UpdateUserInfoResponse>




    //Information
    @PUT("update_user_info/")
    fun updateProfileInfo(@Header("Authorization")token:String
                          ,@Body userData: UserData):Call<UpdateUserInfoResponse>




    @POST("reset-password/")
    fun resetPassword(@Body forgetPasswordRequest: ForgetPasswordRequest)
                            :Call<ForgetPasswordResponse>

    @GET("getuser/")
    fun getProfile(@Header("Authorization")token:String)
                :Call<ProfileResponse>


    @GET("all_seaports/")
    fun getAllPorts(@Header("Authorization")token:String)
            :Call<SeaPortItems>


    @GET("user-port-requests/")
    fun getAllRequests(@Header("Authorization")token:String)
            :Call<GetAllRequests>


}
