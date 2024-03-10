package mo.zain.marassi.viewModel

import androidx.lifecycle.ViewModel
import mo.zain.marassi.di.RetrofitClient
import mo.zain.marassi.model.GetAllRequests
import mo.zain.marassi.model.SeaPortItems
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PortsViewModel :ViewModel() {
    fun getAllPorts(token: String,onResult: (Boolean, SeaPortItems?, String) -> Unit){
        RetrofitClient.apiService.getAllPorts("Token "+token).enqueue(object :
            Callback<SeaPortItems> {
            override fun onResponse(call: Call<SeaPortItems>, response: Response<SeaPortItems>) {
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

            override fun onFailure(call: Call<SeaPortItems>, t: Throwable) {
                onResult(false, null, "Error: ${t.message}")
            }
        })
    }


    fun getAllRequests(token: String,onResult: (Boolean, GetAllRequests?, String) -> Unit){
        RetrofitClient.apiService.getAllRequests("Token "+token).enqueue(object :
            Callback<GetAllRequests> {
            override fun onResponse(call: Call<GetAllRequests>, response: Response<GetAllRequests>) {
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

            override fun onFailure(call: Call<GetAllRequests>, t: Throwable) {
                onResult(false, null, "Error: ${t.message}")
            }
        })
    }


}