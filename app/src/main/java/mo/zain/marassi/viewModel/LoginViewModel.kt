package mo.zain.marassi.viewModel

import androidx.lifecycle.ViewModel
import mo.zain.marassi.di.RetrofitClient
import mo.zain.marassi.model.LoginData
import mo.zain.marassi.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    fun loginUser(loginData: LoginData, onResult: (Boolean, LoginResponse?, String) -> Unit) {
        RetrofitClient.apiService.loginUser(loginData).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        onResult(true, loginResponse, "Login successful")
                    } else {
                        onResult(false, null, "Login failed")
                    }
                } else {
                    onResult(false, null, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onResult(false, null, "Error: ${t.message}")
            }
        })
    }
}