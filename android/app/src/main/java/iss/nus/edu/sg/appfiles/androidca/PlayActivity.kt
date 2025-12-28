package iss.nus.edu.sg.appfiles.androidca

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PlayActivity : AppCompatActivity() {
    private lateinit var adManager: AdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        // Display ads on app
        val isPaidUser = intent.getBooleanExtra("isPaid", false)
        saveUserType(isPaidUser)
        adManager = AdManager(this)
        adManager.startAds(findViewById<ImageView>(R.id.ads_image))
    }

    // Display ads on app function
    private fun saveUserType(isPaid: Boolean){
        val sharedPref = getSharedPreferences("UserType", MODE_PRIVATE)
        sharedPref.edit().putBoolean("isPaid", isPaid).apply()

    }
    override fun onDestroy() {
        super.onDestroy()
        adManager.stopAds()
    }

}