package iss.nus.edu.sg.appfiles.androidca.fetch

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import iss.nus.edu.sg.appfiles.androidca.databinding.ActivityFetchBinding
import okhttp3.OkHttpClient
import okhttp3.Request

class FetchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFetchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFetchBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.fetch) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initFetch()
    }

    fun initFetch() {
        binding.fetchBtn.setOnClickListener {
            val userInputUrl = binding.urlInput.text.toString()

            if (userInputUrl.isNotEmpty()) {
                //start fetch
                startFetch(userInputUrl)
            } else {
                Toast.makeText(this, "Try again. Fill in something this time!",
                    Toast.LENGTH_SHORT).show()
            }
//           val file = createDestFile(url)
        }

    }

    fun startFetch (url: String){
        val originalUrl = "https://stocksnap.io/search/food"
        val targetUrl = originalUrl

        var client = OkHttpClient()

        val request = Request.Builder()
            .url(targetUrl)
            .header(
                "User-Agent",
                "Mozilla/5.0"
            )
            .build()

    }
}