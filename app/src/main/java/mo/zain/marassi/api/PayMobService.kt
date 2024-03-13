package mo.zain.marassi.api

import mo.zain.marassi.model.paymob.OrderRequest
import mo.zain.marassi.model.paymob.OrderResponse
import mo.zain.marassi.model.paymob.PaymentRequest
import mo.zain.marassi.model.paymob.PaymentResponse
import mo.zain.marassi.model.paymob.TokenRequest
import mo.zain.marassi.model.paymob.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PayMobService {

    @POST("auth/tokens")
    fun postToken(@Body tokenRequest: TokenRequest): Call<TokenResponse>



    @POST("ecommerce/orders")
    fun postOrder(@Header("Authorization") token:String,@Body orderRequest: OrderRequest): Call<OrderResponse>



    @POST("acceptance/payment_keys")
    fun postPaymentKey(@Header("Authorization") token:String,@Body paymentRequest: PaymentRequest): Call<PaymentResponse>






}