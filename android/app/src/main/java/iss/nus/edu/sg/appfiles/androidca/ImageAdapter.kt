package iss.nus.edu.sg.appfiles.androidca

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.transition.Transition
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

        val progressLoader = view.findViewById<ProgressBar>(R.id.progressLoader)

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
            .into(object : com.bumptech.glide.request.target.CustomTarget<Drawable>() {
//              this is for loading progress bar
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    imageView.setImageDrawable(resource)
                    progressLoader.visibility = View.GONE
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    progressLoader.visibility = View.GONE
                }
            })

        return view
    }



}