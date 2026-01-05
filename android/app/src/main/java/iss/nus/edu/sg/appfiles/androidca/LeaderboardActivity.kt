package iss.nus.edu.sg.appfiles.androidca

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LeaderboardActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val time = intent.getLongExtra("time", 0)
        val score = intent.getIntExtra("score", 0)

    }
}