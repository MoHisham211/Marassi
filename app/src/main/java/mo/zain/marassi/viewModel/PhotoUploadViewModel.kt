package mo.zain.marassi.viewModel

import androidx.lifecycle.ViewModel
import mo.zain.marassi.di.RetrofitClient
import mo.zain.marassi.model.UploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoUploadViewModel : ViewModel() {
    fun uploadPhoto(token: String, photoFile: MultipartBody.Part, onResult: (Boolean, UploadResponse?, String) -> Unit) {
        RetrofitClient.apiService.uploadPhoto("Token $token", photoFile).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    val uploadResponse = response.body()
                    if (uploadResponse != null && uploadResponse.success) {
                        onResult(true, uploadResponse, "Photo uploaded successfully")
                    } else {
                        onResult(false, null, "Failed to upload photo")
                    }
                } else {
                    onResult(false, null, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                onResult(false, null, "Error: ${t.message}")
            }
        })
    }
}