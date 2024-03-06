package mo.zain.marassi.viewModel

import androidx.lifecycle.ViewModel
import mo.zain.marassi.di.RetrofitClient
import mo.zain.marassi.model.AllSeaportsResponse
import mo.zain.marassi.model.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PortsViewModel :ViewModel() {
    fun getAllPorts(token: String,onResult: (Boolean, AllSeaportsResponse?, String) -> Unit){
        RetrofitClient.apiService.getAllPorts("Token "+token).enqueue(object :
            Callback<AllSeaportsResponse> {
            override fun onResponse(call: Call<AllSeaportsResponse>, response: Response<AllSeaportsResponse>) {
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

            override fun onFailure(call: Call<AllSeaportsResponse>, t: Throwable) {
                onResult(false, null, "Error: ${t.message}")
            }
        })
    }

}