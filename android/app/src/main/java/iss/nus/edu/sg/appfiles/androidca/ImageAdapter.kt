package iss.nus.edu.sg.appfiles.androidca

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import iss.nus.edu.sg.appfiles.androidca.models.ImageItem

class ImageAdapter(
    private val context: Context,
    private val images: MutableList<ImageItem>
) : BaseAdapter() {

    override fun getCount(): Int = images.size

    override fun getItem(position: Int): ImageItem = images[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_image, parent, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val imageItem = images[position]


        if (imageItem.url.isNotEmpty()) {
            //uses glide lib here
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
                .placeholder(android.R.color.darker_gray)
                .into(imageView)
        } else{
            imageView.setImageDrawable(null)
        }

        return view
    }



}