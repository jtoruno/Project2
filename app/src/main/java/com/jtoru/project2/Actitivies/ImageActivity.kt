package com.jtoru.project2.Actitivies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.jtoru.project2.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val url = this.intent.getStringExtra("url")?:""
        Picasso.get().load(url).error(R.drawable.download).noPlaceholder().into(image_full_screen)

        image_full_back_btn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
