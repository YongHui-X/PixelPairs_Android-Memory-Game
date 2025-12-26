package iss.nus.edu.sg.appfiles.androidca

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
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
        imageView.setImageBitmap(imageItem.bitmap)

        return view
    }
}