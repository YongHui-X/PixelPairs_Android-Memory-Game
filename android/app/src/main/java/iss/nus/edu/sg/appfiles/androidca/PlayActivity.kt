package iss.nus.edu.sg.appfiles.androidca

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlayActivity : AppCompatActivity() {

    private lateinit var rvCards: RecyclerView
    private lateinit var tvMatches: TextView
    private lateinit var tvTimer: TextView

    private val cardFaces = mutableListOf<Int>()

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
        val faces = listOf(
            R.drawable.card_1,
            R.drawable.card_2,
            R.drawable.card_3,
            R.drawable.card_4,
            R.drawable.card_5,
            R.drawable.card_6
        )

        cardFaces.clear()
        cardFaces.addAll(faces)
        cardFaces.addAll(faces)
        cardFaces.shuffle()
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
        }
    }


    // Adapter


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

        flipCard(holder.ivCard, false, 0)
    }


    private fun flipCard(
        imageView: ImageView,
        showFront: Boolean,
        frontRes: Int
    ) {

        imageView.cameraDistance = 8000 * imageView.resources.displayMetrics.density

        imageView.animate()
            .rotationY(90f)
            .setDuration(150)
            .withEndAction {

                if (showFront) {
                    imageView.setImageResource(frontRes)
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

}
