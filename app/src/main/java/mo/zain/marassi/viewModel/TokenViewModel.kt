package mo.zain.marassi.viewModel

import androidx.lifecycle.ViewModel
import mo.zain.marassi.di.PaymobClient
import mo.zain.marassi.di.RetrofitClient
import mo.zain.marassi.model.paymob.OrderRequest
import mo.zain.marassi.model.paymob.OrderResponse
import mo.zain.marassi.model.paymob.PaymentRequest
import mo.zain.marassi.model.paymob.PaymentResponse
import mo.zain.marassi.model.paymob.TokenRequest
import mo.zain.marassi.model.paymob.TokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenViewModel : ViewModel()  {
    fun postToken(tokenRequest: TokenRequest, onResult: (Boolean, TokenResponse?, String) -> Unit) {

        PaymobClient.apiService.postToken(tokenRequest).enqueue(object :
            Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val registrationResponse = response.body()
                    if (registrationResponse != null ) {
                        onResult(true, registrationResponse, "Registration successful")
                    } else {
                        onResult(false, null, "Registration failed")
                    }
                } else {
                    onResult(false, null, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                onResult(false, null, "Error: ${t.message}")
            }
        })
    }


    fun postOrder(token:String,orderRequest: OrderRequest,
                  onResult: (Boolean, OrderResponse?, String) -> Unit) {

        PaymobClient.apiService.postOrder("Bearer "+token
            ,orderRequest).enqueue(object :
            Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>,
                                    response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    val registrationResponse = response.body()
                    if (registrationResponse != null ) {
                        onResult(true, registrationResponse, "Registration successful")
                    } else {
                        onResult(false, null, "Registration failed")
                    }
                } else {
                    onResult(false, null, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                onResult(false, null, "Error: ${t.message}")
            }
        })
    }





    fun payment(token:String,paymentRequest: PaymentRequest,
                  onResult: (Boolean, PaymentResponse?, String) -> Unit) {

        PaymobClient.apiService.postPaymentKey("Bearer "+token
            ,paymentRequest).enqueue(object :
            Callback<PaymentResponse> {
            override fun onResponse(call: Call<PaymentResponse>,
                                    response: Response<PaymentResponse>) {
                if (response.isSuccessful) {
                    val registrationResponse = response.body()
                    if (registrationResponse != null ) {
                        onResult(true, registrationResponse, "Registration successful")
                    } else {
                        onResult(false, null, "Registration failed")
                    }
                } else {
                    onResult(false, null, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                onResult(false, null, "Error: ${t.message}")
            }
        })
    }


}