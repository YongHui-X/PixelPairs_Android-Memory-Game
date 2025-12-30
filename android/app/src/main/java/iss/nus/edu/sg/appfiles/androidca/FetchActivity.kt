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
import android.content.Intent
import android.view.View
import android.widget.Button

class FetchActivity : AppCompatActivity() {

    private lateinit var imageAdapter: ImageAdapter
    private val images = mutableListOf<ImageItem>()
    private lateinit var binding: ActivityFetchBinding
    private var fetchJob: Job? = null

    private var username: String? = null
    private var isPaid: Boolean = false
    private var selectedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFetchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("username")
        isPaid = intent.getBooleanExtra("isPaid", false)

        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            hideProgressElements()

            imageAdapter = ImageAdapter(this@FetchActivity, images) { count ->
                selectedCount = count
                updatePlayButton()
            }
            gridImage.adapter = imageAdapter

            playBtn.visibility = View.GONE
            playBtn.isEnabled = false
            playBtn.setOnClickListener {
                if (selectedCount == 6) {
                    startPlayActivity()
                } else {
                    Toast.makeText(this@FetchActivity, "Please select exactly 6 images", Toast.LENGTH_SHORT).show()
                }
            }
            fetchBtn.setOnClickListener {
                val userInputUrl = urlInput.text.toString()
                if (userInputUrl.isNotEmpty()) {
                    startFetch(userInputUrl)
                } else {
                    Toast.makeText(this@FetchActivity, "Try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun hideProgressElements() = binding.apply {
        progressBar.visibility = View.GONE
        downloadText.visibility = View.GONE
        selectionText.visibility = View.GONE
    }

    private fun showProgressElements() = binding.apply {
        progressBar.visibility = View.VISIBLE
        downloadText.visibility = View.VISIBLE
        selectionText.visibility = View.VISIBLE
    }

    private fun updatePlayButton() = binding.apply {
        if (selectedCount == 6){
            playBtn.isEnabled = true
            playBtn.visibility = View.VISIBLE
        } else {
            playBtn.isEnabled = false
            playBtn.visibility = View.GONE
        }
        selectionText.text = "Selected: $selectedCount / 6"
    }

    private fun startPlayActivity() {
        val selectedImages = images.filter { it.isSelected }.map { it.url }


        Intent(this, PlayActivity::class.java).apply {
            putStringArrayListExtra("selectedImages", ArrayList(selectedImages))
            putExtra("username", username)
            putExtra("isPaid", isPaid)
        }.also { startActivity(it) }
    }

    private fun startFetch(url: String) {
        fetchJob?.cancel()
        showProgressElements()

        images.clear()
        selectedCount = 0
        updatePlayButton()

        repeat(20) {
            images.add(ImageItem(url = "", isSelected = false))
        }
        imageAdapter.notifyDataSetChanged()
        updateProgress(0)

        fetchJob = lifecycleScope.launch(Dispatchers.IO) {
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla")
                .header("Referer", "https://stocksnap.io/")
                .build()

            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        showError("Server Error : ${response.code}")
                        return@use
                    }

                    val html = response.body?.string() ?: ""
                    val doc = Jsoup.parse(html, url)
                    val imgElements = doc.select("img[src], img[data-src]")

                    var count = 0
                    for (element in imgElements) {
                        if (!isActive || count >= 20) break

                        val imgUrl = element.absUrl("data-src").takeIf { it.isNotEmpty() }
                            ?: element.absUrl("src")

                        if (imgUrl.isNotEmpty() && imgUrl.contains("cdn.stocksnap.io")) {
                            withContext(Dispatchers.Main) {
                                images[count] = ImageItem(url = imgUrl, isSelected = false)
                                imageAdapter.notifyDataSetChanged()
                                count++
                                updateProgress(count)
                            }
                        }
                        if (count > 1) kotlinx.coroutines.delay(500L)
                    }
                    if (images.isEmpty()) {
                        showError("No images found")
                    }
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }

    private fun updateProgress(count: Int) = binding.apply {
        val percent = (count * 100) / 20
        progressBar.progress = percent
        downloadText.text = "Downloading $count of 20 images."

        if (count == 20) {
            Toast.makeText(this@FetchActivity, "Fetch Completed. Select 6 images to play.",
                Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@FetchActivity, message, Toast.LENGTH_LONG).show()
        }
    }
}