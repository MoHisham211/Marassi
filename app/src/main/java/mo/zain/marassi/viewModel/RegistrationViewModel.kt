
import androidx.lifecycle.ViewModel
import mo.zain.marassi.di.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel : ViewModel() {
    fun registerUser(userData: UserData, onResult: (Boolean, RegistrationResponse?, String) -> Unit) {
        RetrofitClient.apiService.registerUser(userData).enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                if (response.isSuccessful) {
                    val registrationResponse = response.body()
                    if (registrationResponse != null && registrationResponse.success) {
                        onResult(true, registrationResponse, "Registration successful")
                    } else {
                        onResult(false, null, "Registration failed")
                    }
                } else {
                    onResult(false, null, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                onResult(false, null, "Error: ${t.message}")
            }
        })
    }
}
