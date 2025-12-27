package iss.nus.edu.sg.appfiles.androidca

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val tvMatches = findViewById<TextView>(R.id.Matches)
        val tvTimer = findViewById<TextView>(R.id.Timer)
        val rvCards = findViewById<RecyclerView>(R.id.rvCards)

        rvCards.layoutManager = GridLayoutManager(this, 3)

    }
}