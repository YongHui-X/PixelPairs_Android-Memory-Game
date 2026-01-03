package iss.nus.edu.sg.appfiles.androidca.adapters

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import iss.nus.edu.sg.appfiles.androidca.databinding.ItemImageBinding
import iss.nus.edu.sg.appfiles.androidca.models.ImageItem

class ImageAdapter(
    private val context: Context,
    private val images: MutableList<ImageItem>,
    private val onSelectionChanged: (Int) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = images.size

    override fun getItem(position: Int): ImageItem = images[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemImageBinding
        val view: View

        if (convertView == null) {
            binding = ItemImageBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as ItemImageBinding
        }

        val imageItem = images[position]

        if (imageItem.url.isNotEmpty()) {
            val glideUrl = GlideUrl(
                imageItem.url,
                LazyHeaders.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .addHeader("Referer", "https://stocksnap.io/")
                    .build()
            )

            Glide.with(context)
                .load(glideUrl)
                .centerCrop()
                .placeholder(R.color.darker_gray)
                .into(binding.imageView)

            binding.imageView.alpha = if (imageItem.isSelected) 0.5f else 1.0f
            binding.selectionIndicator.visibility = if (imageItem.isSelected) View.VISIBLE else View.GONE

            view.setOnClickListener {
                val selectedCount = images.count { it.isSelected }

                if (imageItem.isSelected) {
                    imageItem.isSelected = false
                    notifyDataSetChanged()
                    onSelectionChanged(selectedCount - 1)
                } else if (selectedCount < 6) {
                    imageItem.isSelected = true
                    notifyDataSetChanged()
                    onSelectionChanged(selectedCount + 1)
                } else {
                    Toast.makeText(context, "Maximum 6 images selected", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.imageView.setImageDrawable(null)
            binding.selectionIndicator.visibility = View.GONE
            view.setOnClickListener(null)
        }

        return view
    }
}