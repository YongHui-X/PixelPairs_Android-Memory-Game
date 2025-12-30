package iss.nus.edu.sg.appfiles.androidca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(
    private val cardFaces: List<Int>,
    private val matched: BooleanArray,
    private val getFirstIndex: () -> Int,
    private val getSecondIndex: () -> Int,
    private val isBusy: () -> Boolean,
    private val onCardClick: (position: Int, imageView: ImageView) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

     class CardViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val ivCard: ImageView = itemView.findViewById(R.id.ivCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        val isMatched = matched[position]
        val shouldShowFront =
            isMatched ||
                    position == getFirstIndex() ||
                    position == getSecondIndex()

        // prevent reuse
        holder.ivCard.rotationY = 0f

        if (isMatched) {
            holder.ivCard.setImageResource(cardFaces[position])
            holder.ivCard.alpha = 0f
            holder.itemView.isClickable = false
        } else {
            holder.ivCard.alpha = 1f
            holder.itemView.isClickable = true

            if (shouldShowFront) {
                holder.ivCard.setImageResource(cardFaces[position])
            } else {
                holder.ivCard.setImageResource(R.drawable.card_back)
            }
        }

        holder.itemView.setOnClickListener {
            if (isBusy()) return@setOnClickListener
            if (matched[position]) return@setOnClickListener
            if (position == getFirstIndex()) return@setOnClickListener

            onCardClick(position, holder.ivCard)
        }
    }

    override fun getItemCount(): Int = cardFaces.size
}
