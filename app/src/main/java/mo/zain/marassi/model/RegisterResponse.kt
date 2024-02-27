import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("token") val token: String,
    @SerializedName("fullname") val fullname: String
)

data class RegistrationResponse(
    @SerializedName("message") val message: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: UserData
)
