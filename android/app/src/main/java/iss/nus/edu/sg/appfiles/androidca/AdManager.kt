package iss.nus.edu.sg.appfiles.androidca

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.core.content.ContextCompat

class AdManager (private val context: Context){

    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0
    private var isRunning = false

    private val images = listOf(
        R.drawable.ad_1,
        R.drawable.ad_2,
        R.drawable.ad_3,
    )

    private fun isPaidUser(): Boolean {
        val sharedPref = context.getSharedPreferences("UserType", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isPaid", false)
    }

    fun startAds(imageView: ImageView) {
        stopAds()

        if (isPaidUser()) {
            imageView.visibility = ImageView.GONE
            return
        }

        isRunning = true
        imageView.visibility = ImageView.VISIBLE

        imageView.setImageDrawable(
            ContextCompat.getDrawable(context, images[currentIndex]))

        // ad schedule rotation
        scheduleNextAd(imageView)
    }

    private fun scheduleNextAd(imageView: ImageView){
        handler.postDelayed({
            if (isRunning) {
                imageView.animate().alpha(1f).setDuration(300).withEndAction {
                    currentIndex = (currentIndex + 1) % images.size
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(context, images[currentIndex])
                    )
                    imageView.animate().alpha(1f).setDuration(300).start()
                }.start()
                scheduleNextAd(imageView)
            }
        }, 30000)
    }

    fun stopAds() {
        isRunning = false
        handler.removeCallbacksAndMessages(null)
    }
}