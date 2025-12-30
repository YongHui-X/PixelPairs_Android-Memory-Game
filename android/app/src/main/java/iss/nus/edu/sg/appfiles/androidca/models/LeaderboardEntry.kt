package iss.nus.edu.sg.appfiles.androidca.models

data class LeaderboardEntry (
    val rank: Int,
    val username: String,
    val score: Long,
	val formattedTime: String
)

