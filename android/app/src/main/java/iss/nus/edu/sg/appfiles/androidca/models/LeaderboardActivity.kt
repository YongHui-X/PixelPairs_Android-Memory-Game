package iss.nus.edu.sg.appfiles.androidca.models

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.appfiles.androidca.adapters.LeaderboardAdapter
import iss.nus.edu.sg.appfiles.androidca.adapters.Player
import iss.nus.edu.sg.appfiles.androidca.R

class LeaderboardActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val recyclerView = findViewById<RecyclerView>(R.id.rvLeaderboard)

        val adapter = LeaderboardAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val testData = listOf(
            Player(1, "Alice", "00:35"),
            Player(2, "Bob", "00:42"),
            Player(3, "Charlie", "00:45"),
            Player(4, "David", "00:50"),
            Player(5, "Eve", "00:55")
        )

        adapter.setPlayers(testData)
    }
}