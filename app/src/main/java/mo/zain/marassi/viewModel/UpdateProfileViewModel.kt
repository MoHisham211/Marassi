package mo.zain.marassi.viewModel

import UserData
import androidx.lifecycle.ViewModel
import mo.zain.marassi.di.RetrofitClient
import mo.zain.marassi.model.UpdateUserInfoResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part
import retrofit2.http.Query


class UpdateProfileViewModel :ViewModel(){

    fun updateProfileUser(
        token: String,
        photo: MultipartBody.Part,
        IDCard: MultipartBody.Part,
        onResult: (Boolean, UpdateUserInfoResponse?, String) -> Unit
    ) {
        val authorizationHeader = "Token $token"

        RetrofitClient.apiService.updateProfile(
            authorizationHeader,
            photo,IDCard).enqueue(object : Callback<UpdateUserInfoResponse> {
            override fun onResponse(
                call: Call<UpdateUserInfoResponse>,
                response: Response<UpdateUserInfoResponse>
            ) {
                if (response.isSuccessful) {
                    val updateUserInfo = response.body()
                    if (updateUserInfo != null && updateUserInfo.success) {
                        onResult(true, updateUserInfo, "Update successful")
                    } else {
                        onResult(false, null, "Update failed")
                    }
                } else {
                    val errorMessage = "Error:------ ${response.code()} ${response.message()}"
                    onResult(false, null, errorMessage)
                }
            }

            override fun onFailure(call: Call<UpdateUserInfoResponse>, t: Throwable) {
                val errorMessage = "Error:----- ${t.message}"
                onResult(false, null, errorMessage)
            }
        })
    }

    fun updateProfileInfo(token:String,userData: UserData, onResult: (Boolean, UpdateUserInfoResponse?, String) -> Unit) {
        RetrofitClient.apiService.updateProfileInfo("Token "+token,userData).enqueue(object :
            Callback<UpdateUserInfoResponse> {
            override fun onResponse(call: Call<UpdateUserInfoResponse>, response: Response<UpdateUserInfoResponse>) {
                if (response.isSuccessful) {
                    val updateUserInfo = response.body()
                    if (updateUserInfo != null && updateUserInfo.success) {
                        onResult(true, updateUserInfo, "Update successful")
                    } else {
                        onResult(false, null, "Update failed")
                    }
                } else {
                    onResult(false, null, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UpdateUserInfoResponse>, t: Throwable) {
                onResult(false, null, "Error: ${t.message}")
            }
        })
    }

}

