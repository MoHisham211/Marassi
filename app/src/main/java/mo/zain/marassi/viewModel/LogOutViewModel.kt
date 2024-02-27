package mo.zain.marassi.viewModel

import androidx.lifecycle.ViewModel
import mo.zain.marassi.di.RetrofitClient
import mo.zain.marassi.model.LogOutData
import mo.zain.marassi.model.LoginData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogOutViewModel : ViewModel() {
    fun logOut(token: String, onResult: (Boolean, LogOutData?, String) -> Unit) {
        RetrofitClient.apiService.logOutUser("Token "+token).enqueue(object : Callback<LogOutData> {
            override fun onResponse(call: Call<LogOutData>, response: Response<LogOutData>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        onResult(true, loginResponse, "LogOut successful")
                    } else {
                        onResult(false, null, "LogOut failed")
                    }
                } else {
                    onResult(false, null, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LogOutData>, t: Throwable) {
                onResult(false, null, "Error: ${t.message}")
            }
        })
    }
}