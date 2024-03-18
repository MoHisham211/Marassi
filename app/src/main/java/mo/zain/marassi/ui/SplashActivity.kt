package mo.zain.marassi.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mo.zain.marassi.R
import android.content.Intent
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 2000 // Splash screen delay time in milliseconds
    lateinit var imgSplash:ImageView
    lateinit var txtSplash:TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        imgSplash = findViewById(R.id.imageView)
        txtSplash = findViewById(R.id.textView2)

        animationAndIntent()
    }

    private fun animationAndIntent() {
        // Load the animation
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Start the animation
        imgSplash.startAnimation(fadeInAnimation)
        txtSplash.startAnimation(fadeInAnimation)

        supportActionBar?.hide()
        Handler().postDelayed({
            // Start the main activity after the splash delay
            val intent = Intent(this@SplashActivity, AuthenticationActivity::class.java)
            startActivity(intent)
            finish() // Close the splash activity
        }, SPLASH_DELAY)
    }

}