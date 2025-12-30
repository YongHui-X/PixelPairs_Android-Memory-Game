package iss.nus.edu.sg.appfiles.androidca

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.appfiles.androidca.adapters.LeaderboardAdapter
import iss.nus.edu.sg.appfiles.androidca.models.LeaderboardEntry
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class LeaderboardFragment : Fragment() {

    private lateinit var rvLeaderboard: RecyclerView
    private val leaderboardEntries = mutableListOf<LeaderboardEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvLeaderboard = view.findViewById(R.id.rvLeaderboard)
        rvLeaderboard.layoutManager = LinearLayoutManager(requireContext())

        fetchLeaderboard()
    }

    private fun fetchLeaderboard() {
        Thread {
            try {
                val entries = getTopScores()
                requireActivity().runOnUiThread {
                    if (entries.isNotEmpty()) {
                        leaderboardEntries.clear()
                        leaderboardEntries.addAll(entries)
                        rvLeaderboard.adapter = LeaderboardAdapter(leaderboardEntries)
                    } else {
                        Toast.makeText(requireContext(), "No leaderboard data available", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Error loading leaderboard: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun getTopScores(): List<LeaderboardEntry> {
        val urlString = "http://10.0.2.2:5119/api/scores/top5"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        return try {
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                parseLeaderboardJson(response)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        } finally {
            connection.disconnect()
        }
    }

    private fun parseLeaderboardJson(jsonString: String): List<LeaderboardEntry> {
        val entries = mutableListOf<LeaderboardEntry>()
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            entries.add(
                LeaderboardEntry(
                    rank = i + 1,
                    username = jsonObject.getString("username"),
                    score = jsonObject.getInt("score")
                )
            )
        }

        return entries
    }
}