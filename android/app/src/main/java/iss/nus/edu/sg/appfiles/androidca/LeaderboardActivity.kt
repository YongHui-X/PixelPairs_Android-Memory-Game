package iss.nus.edu.sg.appfiles.androidca

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LeaderboardActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)


		//add test data for testing
		intent.putExtra("USERNAME", "TestPlayer")
		intent.putExtra("SCORE", 999)

		setContentView(R.layout.activity_leadboard)
	}
}