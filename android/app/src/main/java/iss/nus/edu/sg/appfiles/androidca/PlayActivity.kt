package iss.nus.edu.sg.appfiles.androidca

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import android.content.Intent
import iss.nus.edu.sg.appfiles.androidca.adapters.CardAdapter
import iss.nus.edu.sg.appfiles.androidca.services.MusicService

class PlayActivity : AppCompatActivity() {

    private lateinit var rvCards: RecyclerView
    private lateinit var tvMatches: TextView
    private lateinit var tvTimer: TextView

    private val cardFaces = mutableListOf<String>()

    private val matched = BooleanArray(12)

    private var firstIndex = -1
    private var secondIndex = -1

    // avoid busy click
    private var isBusy = false

    private lateinit var adapter: CardAdapter
    private val handler = Handler(Looper.getMainLooper())


    private val timerHandler = Handler(Looper.getMainLooper())
    private var secondsElapsed = 0
    private var isGameRunning = false

    private lateinit var adManager: AdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        tvMatches = findViewById(R.id.Matches)
        rvCards = findViewById(R.id.rvCards)
        tvTimer = findViewById(R.id.Timer)

        rvCards.layoutManager = GridLayoutManager(this, 3)

        setupCards()

        adapter = CardAdapter(
            cardFaces = cardFaces,
            matched = matched,
            getFirstIndex = { firstIndex },
            getSecondIndex = { secondIndex },
            isBusy = { isBusy },
            onCardClick = { position, imageView ->
                onCardClicked(position, imageView)
            }
        )

        rvCards.adapter = adapter

        updateMatchText()
        startTimer()


        // Display ads on app
        val isPaidUser = intent.getBooleanExtra("isPaid", false)
        saveUserType(isPaidUser)
        adManager = AdManager(this)
        adManager.startAds(findViewById<ImageView>(R.id.ads_image))

        // Music
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("music", "play")
        startService(intent)
    }

    // Display ads on app function
    private fun saveUserType(isPaid: Boolean){
        val sharedPref = getSharedPreferences("UserType", MODE_PRIVATE)
        sharedPref.edit().putBoolean("isPaid", isPaid).apply()

    }


    private fun startTimer() {
        secondsElapsed = 0
        isGameRunning = true
        timerHandler.post(timerRunnable)
    }

    private fun stopTimer() {
        isGameRunning = false
        timerHandler.removeCallbacks(timerRunnable)
    }



    private fun setupCards() {
        val selectedUrls = intent.getStringArrayListExtra("selectedImages") ?: arrayListOf()

        CoroutineScope(Dispatchers.IO).launch {
            val localPaths = mutableListOf<String>()

            selectedUrls.forEachIndexed { index, url ->
                try {
                    val connection = URL(url).openConnection()
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0")
                    connection.setRequestProperty("Referer", "https://stocksnap.io/")

                    val inputStream = connection.getInputStream()
                    val file = File(filesDir, "card_$index.jpg")

                    FileOutputStream(file).use { output ->
                        inputStream.copyTo(output)
                    }
                    inputStream.close()

                    localPaths.add(file.absolutePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            withContext(Dispatchers.Main) {
                cardFaces.clear()
                cardFaces.addAll(localPaths)  // 6 images
                cardFaces.addAll(localPaths)  // Duplicate for pairs
                cardFaces.shuffle()

                adapter.notifyDataSetChanged()
                startTimer()
            }
        }
    }

    private fun updateTimerText() {
        val hours = secondsElapsed / 3600
        val minutes = (secondsElapsed % 3600) / 60
        val seconds = secondsElapsed % 60

        tvTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (isGameRunning) {
                secondsElapsed++
                updateTimerText()
                timerHandler.postDelayed(this, 1000)
            }
        }
    }

    private fun updateMatchText() {
        val count = matched.count { it }
        val matches = count / 2
        tvMatches.text = "$matches/6 matches"

        if (matches == 6) {
            stopTimer()

			handler.postDelayed({
				val leaderboardIntent = Intent(this, LeaderboardActivity::class.java)
				leaderboardIntent.putExtra("username", getIntent().getStringExtra("username"))
				leaderboardIntent.putExtra("score", secondsElapsed * 1000)
				startActivity(leaderboardIntent)
				finish()
			}, 1500)
        }
    }

    private fun onCardClicked(position: Int, imageView: ImageView) {

        if (firstIndex == -1) {
            firstIndex = position
            flipCard(imageView, true, cardFaces[position])
            return
        }

        secondIndex = position
        flipCard(imageView, true, cardFaces[position])
        isBusy = true

        handler.postDelayed({

            if (cardFaces[firstIndex] == cardFaces[secondIndex]) {

                matched[firstIndex] = true
                matched[secondIndex] = true
                updateMatchText()

                fadeOutMatched(firstIndex)
                fadeOutMatched(secondIndex)
            } else {
                flipBack(firstIndex)
                flipBack(secondIndex)
            }

            firstIndex = -1
            secondIndex = -1
            isBusy = false

        }, 800)
    }

    private fun flipBack(position: Int) {
        val holder = rvCards.findViewHolderForAdapterPosition(position)
                as? CardAdapter.CardViewHolder ?: return

        flipCard(holder.ivCard, false, "")
    }


    private fun flipCard(
        imageView: ImageView,
        showFront: Boolean,
        frontPath: String  // Changed from Int
    ) {
        imageView.cameraDistance = 8000 * imageView.resources.displayMetrics.density

        imageView.animate()
            .rotationY(90f)
            .setDuration(150)
            .withEndAction {
                if (showFront) {
                    val bitmap = BitmapFactory.decodeFile(frontPath)
                    imageView.setImageBitmap(bitmap)
                } else {
                    imageView.setImageResource(R.drawable.card_back)
                }

                imageView.rotationY = -90f
                imageView.animate()
                    .rotationY(0f)
                    .setDuration(150)
                    .start()
            }
            .start()
    }

    private fun fadeOutMatched(position: Int) {
        val holder = rvCards.findViewHolderForAdapterPosition(position)
                as? CardAdapter.CardViewHolder ?: return

        fadeOutCard(holder.ivCard)
    }

    private fun fadeOutCard(imageView: ImageView) {
        imageView.animate()
            .alpha(0f)
            .setDuration(600)
            .start()
    }

    override fun onDestroy() {
        super.onDestroy()
        adManager.stopAds()
    }
}