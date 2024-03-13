package mo.zain.marassi.di

import mo.zain.marassi.api.PayMobService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PaymobClient {

    private const val BASE_URL = "https://accept.paymob.com/api/"

    val apiService: PayMobService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(PayMobService::class.java)
    }

}