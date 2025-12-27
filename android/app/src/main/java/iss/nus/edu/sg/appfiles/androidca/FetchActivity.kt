package iss.nus.edu.sg.appfiles.androidca

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import iss.nus.edu.sg.appfiles.androidca.databinding.ActivityFetchBinding
import iss.nus.edu.sg.appfiles.androidca.models.ImageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup


class FetchActivity : AppCompatActivity() {

    private lateinit var imageAdapter: ImageAdapter
    private val images = mutableListOf<ImageItem>()

    private lateinit var binding: ActivityFetchBinding
    private var fetchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFetchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        imageAdapter = ImageAdapter(this, images)
        binding.gridImage.adapter = imageAdapter

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
        }

    }

    private fun startFetch (url: String){
        fetchJob?.cancel()

        //reset
        images.clear()
        imageAdapter.notifyDataSetChanged()
        updateProgress(0)

//        val url = "https://stocksnap.io/search/food"

        fetchJob = lifecycleScope.launch(Dispatchers.IO){
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url(url)
                .header(
                    "User-Agent", "Mozilla"
                )
                .header("Referer", "https://stocksnap.io/")
                .build()

            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful){
                        showError("Server Error : ${response.code}")
                        return@use
                    }

                    //takes the content of the website and convert it into one piece of string
                    val html = response.body?.string() ?: ""
                    val doc = Jsoup.parse(html, url) //turn into doc obj

                    //pulls out tags with <img> tag
                    val imgElements = doc.select("img[src], img[data-src]")

                    for(element in imgElements){
                        if (!isActive || images.size >= 20) break

                        var imgUrl = element.absUrl("data-src")
                        if (imgUrl.isEmpty()){
                            imgUrl = element.absUrl("src")
                        }

                        //filter for actual content and ignore icons/logos
                        if(imgUrl.isNotEmpty() && imgUrl.contains("cdn.stocksnap.io")){

                            withContext(Dispatchers.Main){
                                images.add(ImageItem(url = imgUrl))
                                android.util.Log.d("FETCH_TEST",
                                    "Success! Image #${images.size} downloaded")
                                updateProgress(images.size)
                            }
                        }
                    }
                    if (images.isEmpty()){
                        showError("No images found")
                    }
                }
            } catch (e: Exception){
                showError("Error: ${e.message}")
            }
        }
    }

    private fun updateProgress(count: Int){
        val percent= (count * 100) / 20

        binding.progressBar.progress = percent
        binding.downloadText.text = "Downloading $count of 20 images."

        imageAdapter.notifyDataSetChanged()

        if(count == 20){
            Toast.makeText(this@FetchActivity, "Fetch Completed!", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main){
            Toast.makeText(this@FetchActivity, message, Toast.LENGTH_LONG).show()
        }
    }
}