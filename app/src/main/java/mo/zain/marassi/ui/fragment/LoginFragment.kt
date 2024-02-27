package mo.zain.marassi.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import mo.zain.marassi.R
import mo.zain.marassi.helper.SharedPreferencesHelper
import mo.zain.marassi.model.LoginData
import mo.zain.marassi.ui.MainActivity
import mo.zain.marassi.viewModel.LoginViewModel

class LoginFragment : Fragment() {
    /**
     * UserName
     *Password
     *
     * */
    lateinit var toSignUp: TextView
    lateinit var toSignIn: Button
    lateinit var userEmail: TextInputEditText
    lateinit var userPassword: TextInputEditText
    lateinit var progressBar2: ProgressBar
    var saveToken: SharedPreferences? =null
    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_login, container, false)
        toSignUp=view.findViewById(R.id.toSignUp)
        toSignIn=view.findViewById(R.id.toSignIn)
        userEmail=view.findViewById(R.id.userEmail)
        userPassword=view.findViewById(R.id.userPassword)
        progressBar2=view.findViewById(R.id.progressBar2)
        saveToken=requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)

        toSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        toSignIn.setOnClickListener {

            checkData()
        }
        return view
    }

    private fun checkData() {
        if (userEmail.text.isNullOrEmpty())
        {
            userEmail.setError("Please Enter Your UserName")
            return
        }else if (userPassword.text.isNullOrEmpty())
        {
            userPassword.setError("Please Enter Your Password")
            return
        }
        else{
            loginUser(userEmail.text.toString(),userPassword.text.toString())
        }

    }

    private fun loginUser(username: String, password: String) {
        progressBar2.visibility=View.VISIBLE
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        val loginData = LoginData(username, password)
        viewModel.loginUser(loginData) { isSuccess, registrationResponse, message ->
            if (isSuccess) {
                progressBar2.visibility=View.GONE
                // Registration successful, handle accordingly
                Toast.makeText(requireContext(), "Sussess "+registrationResponse, Toast.LENGTH_SHORT).show()

                var editor: SharedPreferences.Editor=saveToken!!.edit()
                editor.putString("Token",
                    registrationResponse!!.data.token.trim()).apply()

                SharedPreferencesHelper.saveUserData(requireContext(), registrationResponse.data)

                val intent= Intent(requireContext(), MainActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()

            } else {
                progressBar2.visibility=View.GONE
                Toast.makeText(requireContext(), "Error "+registrationResponse, Toast.LENGTH_SHORT).show()
            }
        }
    }


}