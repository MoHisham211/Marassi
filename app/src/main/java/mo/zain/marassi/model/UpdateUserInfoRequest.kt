package mo.zain.marassi.model

import UserData
import com.google.gson.annotations.SerializedName

data class UpdateUserInfoRequest(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("fullname") val fullname: String,
    @SerializedName("UserValidate") val userValidate: Boolean,
    @SerializedName("photo") val photo: Any?,
    @SerializedName("IDCard") val idCard: Any?,
    @SerializedName("phone") val phone: String,
    @SerializedName("government") val government: Any?,
    @SerializedName("ids") val ids: String,
    @SerializedName("country") val country: String
)

data class UpdateUserInfoResponse(
    @SerializedName("message") val message: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: UserData // Assuming UserData is the response data class
)