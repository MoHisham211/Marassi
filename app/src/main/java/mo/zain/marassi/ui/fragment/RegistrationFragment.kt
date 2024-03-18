package mo.zain.marassi.ui.fragment

import RegistrationViewModel
import UserData
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import mo.zain.marassi.R
import mo.zain.marassi.helper.SharedPreferencesHelper
import mo.zain.marassi.ui.MainActivity


class RegistrationFragment : Fragment() {

    /**
     *
     * FullName
     * UserName
     * Email
     * phone
     * password
     *
     */

    lateinit var FullName: TextInputEditText
    lateinit var UserName: TextInputEditText
    lateinit var phone: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var Email: TextInputEditText
    lateinit var toSignIn: Button

    lateinit var progressBar: ProgressBar
    lateinit var agreeCheck: CheckBox
    lateinit var viewContant:View
    lateinit var toSignUp: TextView

    //
    var saveToken:SharedPreferences ? =null

    //private val viewModel: RegistrationViewModel by viewModels()
    //private val repository = RegistrationRepository()

    private lateinit var viewModel: RegistrationViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_registration, container, false)

        FullName=view.findViewById(R.id.FullName)
        UserName=view.findViewById(R.id.UserName)
        phone=view.findViewById(R.id.phone)
        password=view.findViewById(R.id.password)
        Email=view.findViewById(R.id.Email)
        toSignIn=view.findViewById(R.id.toSignIn)
        progressBar=view.findViewById(R.id.progressBar)
        agreeCheck=view.findViewById(R.id.agreeCheck)
        toSignUp=view.findViewById(R.id.toSignUp)
        saveToken=requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)

        toSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        toSignIn.setOnClickListener {
            checkData()
        }




        return view;
    }

    private fun checkData() {
        if (FullName.text.isNullOrEmpty())
        {
            FullName.setError("Please Enter Your Name!")
            return
        }else if (Email.text.isNullOrEmpty())
        {
            Email.setError("Please Enter Your Email!")
            return
        }else if (phone.text.isNullOrEmpty()){
            phone.setError("Please Enter Your Phone Number!")
            return
        }else if (password.text.isNullOrEmpty()){
            password.setError("Please Enter Your Password!")
            return
       }else if (UserName.text.isNullOrEmpty()){
            UserName.setError("Please Enter Your UserName!")
            return
        }
//        else if(!agreeCheck.isChecked)
//        {
//            Snackbar.make(viewContant, "Please agree to all conditions", Snackbar.LENGTH_LONG)
//                .setAction("CLOSE") {
//
//                }
//                .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
//                .show()
//        }
        else{
            registerUser(UserName.text.toString(),password.text.toString(),Email.text.toString(),phone.text.toString(),FullName.text.toString())
        }
    }



    private fun registerUser(username: String, password: String, email: String, phone: String, fullname: String) {
        progressBar.visibility=View.VISIBLE
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

            val userData = UserData(username, password, email, phone ,"",fullname)

        viewModel.registerUser(userData) { isSuccess, registrationResponse, message ->
                if (isSuccess) {
                    progressBar.visibility=View.GONE
                    // Registration successful, handle accordingly
                    Toast.makeText(requireContext(), "Create User Done", Toast.LENGTH_SHORT).show()

                    SharedPreferencesHelper.saveUserData(requireContext(), userData)

                    var editor: SharedPreferences.Editor=saveToken!!.edit()
                    editor.putString("Token",
                        registrationResponse!!.data.token.trim()).apply()


                    val intent= Intent(requireContext(), MainActivity::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()

                } else {
                    progressBar.visibility=View.GONE
                    Toast.makeText(requireContext(), "Error ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }