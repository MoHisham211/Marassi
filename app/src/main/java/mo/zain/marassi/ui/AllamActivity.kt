package mo.zain.marassi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import mo.zain.marassi.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException

class AllamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allam)

        updateUserInfo("c44bcd0e07717cf07ca3448482161307953e5e60")

    }

    fun updateUserInfo(token: String) {
        val url = "https://datamanager686.pythonanywhere.com/api/update_user_info/"

        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", "omn@example.com")
            .addFormDataPart("phone", "01002284852")
            .addFormDataPart("fullname", "lamyaa mo Allam")
            .addFormDataPart(
                "IDCard",
                "IDCard.png",
                RequestBody.create("image/png".toMediaTypeOrNull(), File("/storage/emulated/0/DCIM/Screenshots/hi.jpg"))
            )
            .addFormDataPart(
                "photo",
                "photo.png",
                RequestBody.create("image/png".toMediaTypeOrNull(), File("/storage/emulated/0/DCIM/Screenshots/hi.jpg"))
            )
            .build()

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Token $token")
            .put(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                println("An error occurred: ${e.message}")
                Log.d("Mohamed","An error occurred: ${e.message}")
                //Toast.makeText(this@AllamActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                println(responseBody)
                Log.d("Mohamed","${responseBody}")
                //Toast.makeText(this@AllamActivity, ""+responseBody, Toast.LENGTH_SHORT).show()
            }
        })
    }

}