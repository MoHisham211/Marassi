package mo.zain.marassi.viewModel

import UserData
import androidx.lifecycle.ViewModel
import mo.zain.marassi.di.RetrofitClient
import mo.zain.marassi.model.UpdateUserInfoResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileViewModel :ViewModel(){

    fun updateProfileUser(token:String, userData: UserData, onResult: (Boolean, UpdateUserInfoResponse?, String) -> Unit) {
        RetrofitClient.apiService.updateProfile("Token "+token, userData).enqueue(object :
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
/*
    fun updateProfileUser(token:String,userData: UserData, onResult: (Boolean, UpdateUserInfoResponse?, String) -> Unit) {
        RetrofitClient.apiService.updateProfile("Token "+token,userData).enqueue(object :
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
    }*/

}