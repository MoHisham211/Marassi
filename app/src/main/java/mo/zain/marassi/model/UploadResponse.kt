package mo.zain.marassi.model

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("message") val message: String,
    @SerializedName("success") val success: Boolean
)