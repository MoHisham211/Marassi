package mo.zain.marassi.ui.fragment.main

import UserData
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.makeramen.roundedimageview.RoundedImageView
import mo.zain.marassi.R
import mo.zain.marassi.helper.SharedPreferencesHelper
import mo.zain.marassi.ui.AuthenticationActivity
import mo.zain.marassi.viewModel.LogOutViewModel
import mo.zain.marassi.viewModel.LoginViewModel
import mo.zain.marassi.viewModel.PhotoUploadViewModel
import mo.zain.marassi.viewModel.UpdateProfileViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ProfileFragment : Fragment() {

    lateinit var floatingActionLogout: FloatingActionButton
    var saveToken: SharedPreferences? = null
    private lateinit var viewModel: LogOutViewModel
    lateinit var fullName: EditText
    lateinit var userEmail: EditText
    lateinit var phoneNum: EditText
    lateinit var btnEdit: Button
    lateinit var camera_icon: ImageView
    lateinit var rounded_image_view: RoundedImageView
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    var bitmap:Bitmap ?= null
    var fileAfterBitMapp:File ?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        saveToken = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
        val token = saveToken!!.getString("Token", "")

        fullName = view.findViewById(R.id.fullName)
        userEmail = view.findViewById(R.id.email)
        phoneNum = view.findViewById(R.id.phoneNum)
        btnEdit = view.findViewById(R.id.editBtn)
        floatingActionLogout = view.findViewById(R.id.floatingActionLogout)
        camera_icon = view.findViewById(R.id.camera_icon)
        rounded_image_view = view.findViewById(R.id.rounded_image_view)
        val includeLayout = view.findViewById<View>(R.id.include)
        val iconImageView = includeLayout.findViewById<ImageView>(R.id.right_icon)

        val userData: UserData? = SharedPreferencesHelper.getUserData(requireContext())

        if (userData != null) {

            fullName.setText(userData!!.fullname)
            userEmail.setText(userData.email)
            phoneNum.setText(userData.phone)
        } else {

        }


        floatingActionLogout.setOnClickListener {
            logOutUser(token!!)
        }

        btnEdit.setOnClickListener {

//            updateData(
//                token!!, UserData(
//                    userData!!.username, "", userEmail.text.toString(), phoneNum.text.toString(),
//                    "", fullName.text.toString()
//                )
//            )
            updateUserInfo(token!!,fileAfterBitMapp!!)
        }

        camera_icon.setOnClickListener {
            chooseImage()
        }

        iconImageView.setOnClickListener {
            //updateUserInfo(token!!,bitmap)
        }

        return view
    }


    private fun logOutUser(token: String) {

        viewModel = ViewModelProvider(this).get(LogOutViewModel::class.java)

        viewModel.logOut(token) { isSuccess, logOutResponse, message ->
            if (isSuccess) {
                // Registration successful, handle accordingly
                Toast.makeText(requireContext(), "Success " + logOutResponse!!.message, Toast.LENGTH_SHORT).show()
                saveToken!!.edit().remove("Token").apply()

                val intent = Intent(requireContext(), AuthenticationActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()

            } else {

                Toast.makeText(requireContext(), "Error " + logOutResponse, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun chooseImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_IMAGE_REQUEST
            )
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseImage()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                fileAfterBitMapp=bitmapToFile(bitmap,requireContext());
                //photoName=bitmapToString(bitmap)
                //updateUserInfo("c44bcd0e07717cf07ca3448482161307953e5e60",bitmap)
                rounded_image_view.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun bitmapToFile(bitmap: Bitmap?, context: Context): File {
        val file = File(context.cacheDir, "temp_image.png")
        file.createNewFile()

        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 20, outputStream)
        val byteArray = outputStream.toByteArray()

        val fos = FileOutputStream(file)
        fos.write(byteArray)
        fos.flush()
        fos.close()

        return file
    }



    fun updateUserInfo(token: String, file:File) {
        val url = "https://datamanager686.pythonanywhere.com/api/update_user_info/"

        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", userEmail.text.toString())
            .addFormDataPart("phone", phoneNum.text.toString())
            .addFormDataPart("fullname", fullName.text.toString())
//            .addFormDataPart(
//                "IDCard",
//                "IDCard.png",
//                RequestBody.create("image/png".toMediaTypeOrNull(), file)
//            )
            .addFormDataPart(
                "photo",
                "photo.png",
                RequestBody.create("image/png".toMediaTypeOrNull(), file)
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
                Log.d("Hi","An error occurred: ${e.message}")
                //Toast.makeText(requireContext(), "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("Hi","${responseBody}")
                //Toast.makeText(requireContext(), ""+responseBody, Toast.LENGTH_SHORT).show()
            }
        })
    }


}
