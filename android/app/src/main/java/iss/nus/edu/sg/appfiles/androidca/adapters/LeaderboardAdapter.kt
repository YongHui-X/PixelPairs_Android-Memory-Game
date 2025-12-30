package iss.nus.edu.sg.appfiles.androidca.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.appfiles.androidca.R

// Simple Player data class
data class Player(
    val rank: Int,
    val name: String,
    val time: String
)

// Adapter for RecyclerView
class LeaderboardAdapter : RecyclerView.Adapter<LeaderboardAdapter.PlayerViewHolder>() {

    // List of players
    private var players = listOf<Player>()

    // Update the list
    fun setPlayers(newPlayers: List<Player>) {
        players = newPlayers
        notifyDataSetChanged()
    }

    // Create view for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return PlayerViewHolder(view)
    }

    // Bind data to view
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(players[position])
    }

    // How many items?
    override fun getItemCount(): Int = players.size

    // ViewHolder class
    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvRank: TextView = itemView.findViewById(R.id.tvRank)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)

        fun bind(player: Player) {
            // Show medal for top 3
            tvRank.text = when (player.rank) {
                1 -> "First"
                2 -> "Second"
                3 -> "Third"
                else -> "#${player.rank}"
            }

            tvName.text = player.name
            tvTime.text = player.time
        }
    }
}