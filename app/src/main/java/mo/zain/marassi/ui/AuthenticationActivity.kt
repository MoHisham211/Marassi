package mo.zain.marassi.ui


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

import mo.zain.marassi.R


class AuthenticationActivity : AppCompatActivity() {

    var myShared: SharedPreferences?=null
    //private lateinit var biometricPrompt: BiometricPrompt
    //private lateinit var promptInfo: BiometricPrompt.PromptInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        supportActionBar?.hide()
        myShared=getSharedPreferences("Token", Context.MODE_PRIVATE)
        //******************************************************



        /*val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                displayMessage("Biometric authentication is available")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                displayMessage("This device doesn't support biometric authentication")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                displayMessage("Biometric authentication is currently unavailable")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                displayMessage("No biometric credentials are enrolled")
        }

        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                displayMessage("Authentication error: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                displayMessage("Authentication succeeded!")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                displayMessage("Authentication failed")
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()



        biometricPrompt.authenticate(promptInfo)*/


    if (!myShared?.getString("Token","").isNullOrEmpty())
        {
            startActivity(Intent(this@AuthenticationActivity,MainActivity::class.java))
            finish()
        }

        /*
            Important in xml MainActivity-----> app:defaultNavHost="true"
            true --->تعني ان لما ارجع لازم اعدي علي كل حاجه فتحتها
            false --> تعني اني لما اعمل back اخرج من الكل

         */


        setupActionBarWithNavController(findNavController(R.id.fragmentContainerView))

    }
//    private fun displayMessage(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//    }
    override fun onSupportNavigateUp(): Boolean {
        var navigate = findNavController(R.id.fragmentContainerView)
        return navigate.navigateUp() || super.onSupportNavigateUp()
    }

}
