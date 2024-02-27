package mo.zain.marassi.model

import UserData
import com.google.gson.annotations.SerializedName

data class LogOutData (
    @SerializedName("message") val message: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: UserData
)