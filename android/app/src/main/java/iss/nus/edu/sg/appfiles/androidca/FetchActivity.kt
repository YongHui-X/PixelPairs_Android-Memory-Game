package iss.nus.edu.sg.appfiles.androidca

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import iss.nus.edu.sg.appfiles.androidca.models.ImageItem

class FetchActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var imageAdapter: ImageAdapter
    private val images = mutableListOf<ImageItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch)

        gridView = findViewById(R.id.gridImage)

        imageAdapter = ImageAdapter(this, images)
        gridView.adapter = imageAdapter

    }
}