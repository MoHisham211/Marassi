package mo.zain.marassi.ui.fragment.main

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import mo.zain.marassi.R
import mo.zain.marassi.adapter.RequestsAdapter
import mo.zain.marassi.model.DataX
import mo.zain.marassi.model.DataXX
import mo.zain.marassi.model.DataXXX
import mo.zain.marassi.model.RequestResponse
import mo.zain.marassi.model.paymob.BillingData
import mo.zain.marassi.model.paymob.OrderRequest
import mo.zain.marassi.model.paymob.PaymentRequest
import mo.zain.marassi.model.paymob.TokenRequest
import mo.zain.marassi.viewModel.PortsViewModel
import mo.zain.marassi.viewModel.TokenViewModel
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull


class RequestsFragment : Fragment() {

    lateinit var addRequest:FloatingActionButton
    var dialog:AlertDialog ?=null
    private val PICK_IMAGE_REQUEST = 1
    private var request_file: File?=null
    var saveToken: SharedPreferences? = null
    private lateinit var viewModel: PortsViewModel
    private var AllPorts: ArrayList<DataX> = ArrayList()
    private var portId:Int ? =null
    private lateinit var RequestprogressBar:ProgressBar
    private var allRequests:ArrayList<DataXX> = ArrayList()
    lateinit var requestsAdapter:RequestsAdapter
    lateinit var rvGetData:RecyclerView
    lateinit var animation_view: LottieAnimationView
    private lateinit var viewModelPaymob: TokenViewModel
    private val apiKey="ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmpiR0Z6Y3lJNklrMWxjbU5vWVc1MElpd2ljSEp2Wm1sc1pWOXdheUk2T1RZME9UVTNMQ0p1WVcxbElqb2lhVzVwZEdsaGJDSjkuZ2pyUU1CZ2JwTFFlZ1ZKcWEtX0xUb1drUnBMSS02U0VQcDVFZ1c1OWQtMTdjaElfdnNNbGZMSlZiVDFqdjRfRWoyTE9pX1AteGJnTThYbUlBZHQ5bEE="

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_requests, container, false)

        addRequest=view.findViewById(R.id.addRequest)
        RequestprogressBar=view.findViewById(R.id.RequestprogressBar)
        rvGetData=view.findViewById(R.id.rvGetData)
        rvGetData.layoutManager= LinearLayoutManager(requireContext())
        animation_view=view.findViewById(R.id.animation_view)

        allRequests.clear()

        saveToken = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
        val token = saveToken!!.getString("Token", "")
        addRequest.setOnClickListener {

            showRequestDialog(token!!)
        }

        getAllRequestes(token!!)

        return view
    }
    /*
    https://datamanager686.pythonanywhere.com/api/echo_get_data/
     */

    fun showRequestDialog(token: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.add_request_dialog, null)

        dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()


        val adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = dialogView.findViewById<Spinner>(R.id.spinner)
        spinner.adapter = adapter
        // Set OnItemSelectedListener for the spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                portId=AllPorts.get(position).id
                /*Toast.makeText(requireContext(), ""+AllPorts.get(position).id+"\n"+AllPorts.get(position).name
                    , Toast.LENGTH_SHORT).show()*/
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle when nothing is selected
            }
        }
        dialogView.findViewById<TextView>(R.id.buttonSubmit).setOnClickListener {
            newRequest(token)
            // Handle submit button click
            dialog!!.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.buttonCancel).setOnClickListener {
            // Handle cancel button click

            dialog!!.dismiss()
        }

        dialogView.findViewById<ImageView>(R.id.requestImage).setOnClickListener {
            chooseImage()
        }
        dialog!!.show()

        getSeaPort(token, adapter)
    }

    private fun payNow(requestData: DataXXX) {
        viewModelPaymob = ViewModelProvider(this).get(TokenViewModel::class.java)

        viewModelPaymob.postToken(TokenRequest(apiKey)) { isSuccess, tokenResponse, message ->
                if (isSuccess) {
                    // Registration successful, handle accordingly
                    Toast.makeText(requireContext(), "Sussess "+tokenResponse, Toast.LENGTH_SHORT).show()

                    val token =tokenResponse!!.token

                    Log.d("PayMob","Output"+tokenResponse)
                    makeOrder(token,requestData)

                } else {

                    Toast.makeText(requireContext(), "Error "+tokenResponse, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun makeOrder(token: String,requestData: DataXXX) {

        viewModelPaymob.postOrder(token,
            OrderRequest("100000",token,
                "EGP","false")) { isSuccess, tokenResponse, message ->
            if (isSuccess) {
                // Registration successful, handle accordingly
                Toast.makeText(requireContext(), "Sussess "+tokenResponse, Toast.LENGTH_SHORT).show()

                val order_id=tokenResponse!!.id
                paymentKey(token,order_id,requestData)
                Log.d("PayMob","Output "+tokenResponse)

            } else {

                Toast.makeText(requireContext(), "Error "+tokenResponse, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun paymentKey(token: String, orderId: Int,requestData: DataXXX) {

        Log.d("PayMob","data"+"\n${token}\n${orderId}")
        viewModelPaymob.payment(token,
            PaymentRequest("100000",token, BillingData("803","200","Jaskolskiburgh",requestData.user_id.toString(),requestData.email,requestData.fullname,"50",requestData.user,
                requestData.phone,"5015","PKG",requestData.request_id.toString(),"805"),"EGP",3600,"4536488",orderId.toString())
        ) { isSuccess, tokenResponse, message ->
            if (isSuccess) {
                // Registration successful, handle accordingly
                Toast.makeText(requireContext(), "Sussess "+tokenResponse, Toast.LENGTH_SHORT).show()

                sendToWeb(tokenResponse!!.token)
                Log.d("PayMob","Output "+tokenResponse)

            } else {

                Log.d("PayMob","Output "+tokenResponse)
                Toast.makeText(requireContext(), "Error "+tokenResponse, Toast.LENGTH_SHORT).show()
            }
        }


    }
    private fun sendToWeb(token: String) {
        val url = "https://accept.paymob.com/api/acceptance/iframes/831173?payment_token=${token}"
        val webViewFragment = WebViewFragment.newInstance(url)
        // Pass the fragment instance to the navigate function
        findNavController().navigate(
            R.id.action_requestsFragment_to_webViewFragment,
            // Pass the URL as argument to the WebViewFragment
            bundleOf("url" to url)
        )
    }

    // Function to choose an image
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
            intent.type = "*/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST )
        }
    }

    private fun getFileFromUri(uri: Uri, context: Context, imageId: Int): File? {

            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "request_file")
            inputStream?.use { input ->
                val outputStream = FileOutputStream(file)
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            return file

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageId = requestCode - PICK_IMAGE_REQUEST
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val filePath = data.data
            try {

                request_file = getFileFromUri(filePath!!, requireContext(), imageId)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun getSeaPort(token: String, adapter: ArrayAdapter<String>) {
        viewModel = ViewModelProvider(this).get(PortsViewModel::class.java)

        viewModel.getAllPorts(token) { isSuccess, seaports, message ->
            if (isSuccess) {
                // Populate spinner adapter with names
                seaports?.let {
                    AllPorts.addAll(it.data)
                    adapter.clear()
                    val namesList = it.data.map { seaPort -> seaPort.name }
                    adapter.addAll(namesList)
                }
            } else {
                Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAllRequestes(token: String){

        RequestprogressBar.visibility=View.VISIBLE

        viewModel = ViewModelProvider(this).get(PortsViewModel::class.java)

        viewModel.getAllRequests(token) { isSuccess, seaports, message ->
            if (isSuccess) {
                // Populate spinner adapter with names
                seaports?.let {
                    allRequests.addAll(it.data)
                    requestsAdapter= RequestsAdapter(allRequests)
                    rvGetData.adapter=requestsAdapter
                    animation_view.visibility=View.GONE
                    RequestprogressBar.visibility=View.GONE
                }
            } else {
                Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
                RequestprogressBar.visibility=View.GONE
            }
        }
    }

    fun newRequest(token: String) {
        RequestprogressBar.visibility = View.VISIBLE
        val url = "https://datamanager686.pythonanywhere.com/api/new-port-request/"
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("days", dialog!!.findViewById<TextInputEditText>(R.id.editTextDays).text.toString())
            .addFormDataPart("seaport", portId!!.toString())
            .addFormDataPart("detail", dialog!!.findViewById<TextInputEditText>(R.id.editTextDetails).text.toString())
            .addFormDataPart(
                "request_file",
                "request_file",
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), request_file!!)
            )
            .build()
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Token $token")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                requireActivity().runOnUiThread {
                    RequestprogressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                //println("An error occurred: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val gson = Gson()
                val requestResponse: RequestResponse? = gson.fromJson(responseBody, RequestResponse::class.java)
                requireActivity().runOnUiThread {
                    RequestprogressBar.visibility = View.GONE
                    if (requestResponse?.success == true) {
                        // Request was successful
                        val requestData: DataXXX? = requestResponse.data
                        Toast.makeText(requireContext(), "Request ID: ${requestData?.request_id}", Toast.LENGTH_SHORT).show()
                        // Handle other fields as needed
                        payNow(requestData!!)
                    } else {
                        // Request failed
                        Toast.makeText(requireContext(), "Request failed: ${requestResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                println(responseBody)
            }
        })
    }



//    fun newRequest(token: String) {
//        RequestprogressBar.visibility=View.VISIBLE
//        val url = "https://datamanager686.pythonanywhere.com/api/new-port-request/"
//        val client = OkHttpClient()
//        val requestBody = MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart("days", dialog!!.findViewById<TextInputEditText>(R.id.editTextDays).text.toString())
//            .addFormDataPart("seaport", portId!!.toString())
//            .addFormDataPart("detail", dialog!!.findViewById<TextInputEditText>(R.id.editTextDetails).text.toString())
//            .addFormDataPart(
//                "request_file",
//                "request_file",
//                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), request_file!!)
//            )
//            .build()
//        val request = Request.Builder()
//            .url(url)
//            .header("Authorization", "Token $token")
//            .post(requestBody)
//            .build()
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//                requireActivity().runOnUiThread {
//                    RequestprogressBar.visibility=View.GONE
//                    Toast.makeText(requireContext(), "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//                //println("An error occurred: ${e.message}")
//            }
//            override fun onResponse(call: Call, response: Response) {
//                val responseBody = response.body?.string()
//                requireActivity().runOnUiThread {
//                    RequestprogressBar.visibility=View.GONE
//                    Toast.makeText(requireContext(), "${responseBody}", Toast.LENGTH_SHORT).show()
//                    payNow()
//                }
//                println(responseBody)
//            }
//        })
//    }
}
