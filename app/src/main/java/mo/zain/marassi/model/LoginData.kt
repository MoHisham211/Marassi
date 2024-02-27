package mo.zain.marassi.model

import UserData
import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("message") val message: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: UserData
)