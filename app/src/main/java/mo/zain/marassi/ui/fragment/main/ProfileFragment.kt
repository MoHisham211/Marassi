package mo.zain.marassi.ui.fragment.main

import UserData
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.makeramen.roundedimageview.RoundedImageView
import mo.zain.marassi.R
import mo.zain.marassi.helper.SharedPreferencesHelper
import mo.zain.marassi.ui.AuthenticationActivity
import mo.zain.marassi.viewModel.LogOutViewModel
import mo.zain.marassi.viewModel.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random


class ProfileFragment : Fragment() {

    lateinit var floatingActionLogout: FloatingActionButton
    var saveToken: SharedPreferences? = null
    private lateinit var viewModel: LogOutViewModel
    lateinit var fullName: EditText
    lateinit var userEmail: EditText
    lateinit var phoneNum: EditText
    lateinit var IDCard: TextView
    lateinit var Passport: TextView
    lateinit var Others: TextView

    lateinit var btnEdit: Button
    lateinit var camera_icon: ImageView
    lateinit var rounded_image_view: RoundedImageView

    private val PICK_IMAGE_REQUEST = 1
    var ProfilePhotoAfterBitMapp:File ?=null
    var CardIdPhotoAfterBitMapp:File ?=null
    var PassportAfter:File ?=null
    var OthersAfter:File ?=null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var iconImageView:ImageView
    private lateinit var left_icon:ImageView
    lateinit var progressBar:ProgressBar


    @SuppressLint("MissingInflatedId")
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
        iconImageView = view.findViewById<ImageView>(R.id.right_icon)
        progressBar=view.findViewById(R.id.progressBar)
        left_icon=view.findViewById(R.id.left_icon)
        IDCard=view.findViewById(R.id.IDCard)
        Passport=view.findViewById(R.id.Passport)
        Others=view.findViewById(R.id.Others)



        getProfile(token!!)

        val userData: UserData? = SharedPreferencesHelper.getUserData(requireContext())

        if (userData != null) {
            fullName.setText(userData.fullname)
            userEmail.setText(userData.email)
            phoneNum.setText(userData.phone)
        } else {

        }


        floatingActionLogout.setOnClickListener {
            logOutUser(token!!)
        }

        btnEdit.setOnClickListener {

            left_icon.visibility=View.VISIBLE
            iconImageView.visibility=View.VISIBLE

        }

        camera_icon.setOnClickListener {
            chooseImageProfile(1)
        }

        left_icon.setOnClickListener{

            left_icon.visibility=View.GONE
            iconImageView.visibility=View.GONE
        }
        iconImageView.setOnClickListener {

        updateUserText(token!!, UserData(
                        userData!!.username,
                        "",
                        userEmail.text.toString(),
                        phoneNum.text.toString(),
                        "",
                        fullName.text.toString()))

            //Toast.makeText(requireContext(), "${token!!}", Toast.LENGTH_SHORT).show()
            if(ProfilePhotoAfterBitMapp!=null){
                updateProfileImage(token!!,ProfilePhotoAfterBitMapp!!)
            }
            if (ProfilePhotoAfterBitMapp!=null){
                updateProfileCard(token!!,CardIdPhotoAfterBitMapp!!)
            }
            if (PassportAfter!=null){
                updateProfilePassport(token!!,PassportAfter!!)
            }
            if (OthersAfter!=null){
                updateProfileOthers(token!!,OthersAfter!!)
            }

        }


        IDCard.setOnLongClickListener {
            // Show a toast message when the TextView is long-pressed
            chooseImage(2)
            true // return true to consume the long click event
        }
        Passport.setOnLongClickListener {
            // Show a toast message when the TextView is long-pressed
            chooseImage(3)
            true // return true to consume the long click event
        }
        Others.setOnLongClickListener {
            // Show a toast message when the TextView is long-pressed
            chooseImage(4)
            true // return true to consume the long click event
        }

        //Important --?<-!->-!!-<-!->-!!-<-!->-??-<-!->

        IDCard.setOnClickListener {
            //chooseImage(2)
            try{
                val url = IDCard.text.toString()
                if (!url.equals(null))
                {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }else {
                    Toast.makeText(context, "Press Long Click to edit the IDCard..", Toast.LENGTH_SHORT).show()

                }

            }catch (e:Exception){
                Toast.makeText(context, ""+e.message.toString(), Toast.LENGTH_SHORT).show()
            }

        }
        Passport.setOnClickListener {
           // chooseImage(3)
            try{
                val url = Passport.text.toString()
                if (!url.equals(null)) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }else{
                    Toast.makeText(context, "Press Long Click to edit the Passport..", Toast.LENGTH_SHORT).show()

                }

            }catch (e:Exception){
                Toast.makeText(context, ""+e.message.toString(), Toast.LENGTH_SHORT).show()

            }

        }
        Others.setOnClickListener {
            //chooseImage(4)
            try{
                val url = Others.text.toString()
                if (!url.equals(null)) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }else{
                    Toast.makeText(context, "Press Long Click to edit the Other Files..", Toast.LENGTH_SHORT).show()

                }

            }catch (e:Exception){
                Toast.makeText(context, ""+e.message.toString(), Toast.LENGTH_SHORT).show()

            }

        }

        return view
    }

    @SuppressLint("ResourceType")
    private fun getProfile(token: String) {
        progressBar.visibility=View.VISIBLE

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        profileViewModel.getProfile(token
            ) { isSuccess, registrationResponse, message ->
            if (isSuccess) {

                Glide
                    .with(requireActivity())
                    .load(registrationResponse!!.data.photo)
                    .into(rounded_image_view);


                if(registrationResponse.data.IDCard!=null)
                {
                    IDCard.text=registrationResponse.data.IDCard.toString();
                }
                if(registrationResponse.data.passport!=null)
                {
                    Passport.text=registrationResponse.data.passport.toString();
                }
                if(registrationResponse.data.Others!=null)
                {
                    Others.text=registrationResponse.data.Others.toString();
                }

                fullName.setText(registrationResponse.data.fullname)
                userEmail.setText(registrationResponse.data.email)
                phoneNum.setText(registrationResponse.data.phone)

                // Clearing existing SharedPreferences data
                val editor = saveToken?.edit()
                editor?.remove("userData") // Removing the userData data
                editor?.apply()
                val userData = UserData(registrationResponse.data.username, "", userEmail.text.toString(),
                    phoneNum.text.toString() ,"",fullName.text.toString())
                SharedPreferencesHelper.saveUserData(requireContext(), userData)


//                SharedPreferencesHelper.saveUserData(requireContext(), UserData(registrationResponse!!.data.username
//                    ,"",registrationResponse!!.data.email,registrationResponse!!.data.phone,token!!,registrationResponse.data.fullname))

                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
                left_icon.visibility=View.GONE
                iconImageView.visibility=View.GONE
            } else {
                Toast.makeText(requireContext(), "Error " + registrationResponse, Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
            }
        }

    }

    private fun updateProfileCard(token: String, card: File) {

        progressBar.visibility=View.VISIBLE

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)


        val requestFileCard = card.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val IDCard = MultipartBody.Part.createFormData("IDCard", card.name, requestFileCard)


        profileViewModel.updateProfileCard(token
            ,IDCard) { isSuccess, registrationResponse, message ->
            if (isSuccess) {
                // Registration successful, handle accordingly
                Toast.makeText(requireContext(), "Success ", Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
                left_icon.visibility=View.GONE
                iconImageView.visibility=View.GONE
            } else {
                Toast.makeText(requireContext(), "Error " + registrationResponse, Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
            }
        }
    }


    private fun updateProfilePassport(token: String, card: File) {

        progressBar.visibility=View.VISIBLE

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)


        val requestFileCard = card.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val IDCard = MultipartBody.Part.createFormData("passport", card.name, requestFileCard)


        profileViewModel.updatePassport(token
            ,IDCard) { isSuccess, registrationResponse, message ->
            if (isSuccess) {
                // Registration successful, handle accordingly
                Toast.makeText(requireContext(), "Success " , Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
                left_icon.visibility=View.GONE
                iconImageView.visibility=View.GONE
            } else {
                Toast.makeText(requireContext(), "Error " + registrationResponse, Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
            }
        }
    }


    private fun updateProfileOthers(token: String, card: File) {

        progressBar.visibility=View.VISIBLE

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)


        val requestFileCard = card.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val IDCard = MultipartBody.Part.createFormData("Others", card.name, requestFileCard)


        profileViewModel.updateOthers(token
            ,IDCard) { isSuccess, registrationResponse, message ->
            if (isSuccess) {
                // Registration successful, handle accordingly
                Toast.makeText(requireContext(), "Success " , Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
                left_icon.visibility=View.GONE
                iconImageView.visibility=View.GONE
            } else {
                Toast.makeText(requireContext(), "Error " + registrationResponse, Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
            }
        }
    }



    private fun updateProfileImage(token: String, photo: File) {

        progressBar.visibility=View.VISIBLE

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val requestFile = photo.asRequestBody("image/png".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("photo", photo.name, requestFile)

        profileViewModel.updateProfileImage(token
            ,photoPart) { isSuccess, registrationResponse, message ->
            if (isSuccess) {
                // Registration successful, handle accordingly
                Toast.makeText(requireContext(), "Success " , Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
                left_icon.visibility=View.GONE
                iconImageView.visibility=View.GONE
            } else {
                Toast.makeText(requireContext(), "Error " + registrationResponse, Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
            }
        }
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


    private fun chooseImageProfile(imageId: Int) {
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
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST+imageId )
        }
    }

    private fun chooseImage(imageId: Int) {
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
            intent.type = "*/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST + imageId)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, do nothing for now
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageId = requestCode - PICK_IMAGE_REQUEST
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                // Assuming rounded_image_view1 and rounded_image_view2 are your ImageView instances
                Log.d("ImageId",imageId.toString())
                when (imageId) {
                    1 -> {
                        ProfilePhotoAfterBitMapp = bitmapToFile(bitmap, requireContext(),imageId)
                        rounded_image_view.setImageBitmap(bitmap)
                        Log.d("ImageId",ProfilePhotoAfterBitMapp.toString())
                    }
                    2 -> {
                        CardIdPhotoAfterBitMapp=getFileFromUri(filePath!!,requireContext(),imageId)
                    }
                    3-> {
                        PassportAfter=getFileFromUri(filePath!!,requireContext(),imageId)
                    }
                    4->{
                        OthersAfter=getFileFromUri(filePath!!,requireContext(),imageId)
                    }
                    else -> {
                        // Handle invalid imageId
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }



    private fun getFileFromUri(uri: Uri, context: Context,imageId: Int): File? {
        if (imageId==2){
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_file_idCard")
            inputStream?.use { input ->
                val outputStream = FileOutputStream(file)
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            return file
        }
        if (imageId==3){
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_file_passport")
            inputStream?.use { input ->
                val outputStream = FileOutputStream(file)
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            return file
        }
        if (imageId==4){
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_file_others")
            inputStream?.use { input ->
                val outputStream = FileOutputStream(file)
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            return file
        }


        return null
    }


    private fun bitmapToFile(bitmap: Bitmap?, context: Context, imageId: Int): File? {
        bitmap ?: return null // If bitmap is null, return null

        if(imageId==1)
        {
            val file = File(context.cacheDir, "temp_image.png")
            try {
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream) // Adjusted quality to 80
                val byteArray = outputStream.toByteArray()

                val fos = FileOutputStream(file)
                fos.use { output ->
                    output.write(byteArray)
                    output.flush()
                }
                return file

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return null
    }



    fun updateUserText(token: String,userData: UserData){
        progressBar.visibility=View.VISIBLE
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.updateProfileInfo(token,userData){ isSuccess, registrationResponse, message ->
            if (isSuccess) {
                // Registration successful, handle accordingly
                Toast.makeText(requireContext(), "Success " , Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
            } else {
                Toast.makeText(requireContext(), "Error " , Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE
            }

        }
    }

    /*




    testttt
     */
//    fun updateUserPoth(token: String, photo:File,card:File) {
//
//        progressBar.visibility=View.VISIBLE
//
//        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
//
//        val requestFile = photo.asRequestBody("image/png".toMediaTypeOrNull())
//        val photoPart = MultipartBody.Part.createFormData("photo", photo.name, requestFile)
//
//        val requestFileCard = card.asRequestBody("image/png".toMediaTypeOrNull())
//        val IDCard = MultipartBody.Part.createFormData("IDCard", card.name, requestFileCard)
//
//
//        profileViewModel.updateProfileUser(token
//            ,photoPart,IDCard) { isSuccess, registrationResponse, message ->
//            if (isSuccess) {
//                // Registration successful, handle accordingly
//                Toast.makeText(requireContext(), "Success " + registrationResponse, Toast.LENGTH_SHORT).show()
//                progressBar.visibility=View.GONE
//                left_icon.visibility=View.GONE
//                iconImageView.visibility=View.GONE
//            } else {
//                Toast.makeText(requireContext(), "Error " + registrationResponse, Toast.LENGTH_SHORT).show()
//                progressBar.visibility=View.GONE
//            }
//        }


       /* profileViewModel = ViewModelProvider(this).get(PhotoUploadViewModel::class.java)

        val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("photo", file.name, requestFile)

        profileViewModel.uploadPhoto(token, photoPart) { isSuccess, registrationResponse, message ->
            if (isSuccess) {
                // Registration successful, handle accordingly
                Toast.makeText(requireContext(), "Success " + registrationResponse, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Error " + registrationResponse, Toast.LENGTH_SHORT).show()
            }
        }*/


       /* profileViewModel = ViewModelProvider(this).get(PhotoUploadViewModel::class.java)


        profileViewModel.uploadPhoto(token,RequestBody.create("image/png".toMediaTypeOrNull(), file))
        { isSuccess, registrationResponse, message ->
            if (isSuccess) {
                // Registration successful, handle accordingly
                Toast.makeText(requireContext(), "Sussess "+registrationResponse, Toast.LENGTH_SHORT).show()


            } else {

                Toast.makeText(requireContext(), "Error "+registrationResponse, Toast.LENGTH_SHORT).show()
            }
        }*/



        /*val url = "https://datamanager686.pythonanywhere.com/api/update_user_info/"

        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", userEmail.text.toString())
            .addFormDataPart("phone", phoneNum.text.toString())
            .addFormDataPart("fullname", fullName.text.toString())
            .addFormDataPart(
                "IDCard",
                "IDCard.png",
                RequestBody.create("image/png".toMediaTypeOrNull(), file)
            )
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
                Log.d("Hi.MoHisham","An error occurred: ${e.message}")
                //Toast.makeText(requireContext(), "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("Hi.MoHisham","${responseBody}")
                //Toast.makeText(requireContext(), ""+responseBody, Toast.LENGTH_SHORT).show()
            }
        })*/

    }

