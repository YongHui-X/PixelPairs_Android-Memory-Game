package iss.nus.edu.sg.appfiles.androidca.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.appfiles.androidca.R
import iss.nus.edu.sg.appfiles.androidca.models.LeaderboardEntry

class LeaderboardAdapter(private val entries: List<LeaderboardEntry>): RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    class LeaderboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvRank: TextView = view.findViewById(R.id.tvRank)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_leaderboard, parent, false)
        return LeaderboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int){
        val entry = entries[position]

        holder.tvRank.text = when (entry.rank){
            1 -> "🥇"
            2 -> "🥈"
            3 -> "🥉"
            else -> "${entry.rank}"
        }

        holder.tvName.text = entry.username

        val minutes = entry.score / 60
        val seconds = entry.score % 60
        holder.tvTime.text = String.format("%02d:%02d", minutes, seconds)
    }

    override fun getItemCount() = entries.size
}