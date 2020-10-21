package com.airidasz.recipestorageapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.ingredient_layout.view.*
import kotlin.math.abs
import kotlin.math.round


class AddItemActivity() : AppCompatActivity() {
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db = DataBaseHandler(this)

        // Starts activity to get recipe image
        add_item_image.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            galleryIntent.type = "image/*"

            val chooser = Intent.createChooser(galleryIntent, getString(R.string.choose))
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
            startActivityForResult(chooser, 100)

        }

       //  Add recipe button, checks if fields are not empty, adds recipe if true
        btn_add_recipe.setOnClickListener {
            if(add_item_title.text.toString().isNotEmpty() &&
                add_item_description.text.toString().isNotEmpty()
            ){
                val recipe = Recipe(add_item_title.text.toString(), add_item_description.text.toString(), imageBitmap)
                db.insertData(recipe)

                finish()
            } else
                Toast.makeText(this, "Fill in fields", Toast.LENGTH_SHORT).show()
        }

        btn_exit_add.setOnClickListener {
            finish()
        }

        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        btn_add_ingredient.setOnClickListener{
            val myView: View = inflater.inflate(R.layout.ingredient_layout, ingredient_list, false)

            myView.btn_remove_ingredient.setOnClickListener {
                ingredient_list.removeView(myView)
            }

            ingredient_list.addView(myView)

            add_item_nested_scroll.post(Runnable { add_item_nested_scroll.fullScroll(View.FOCUS_DOWN) })
        }

        // Change cancel and add recipe button opacity based on the visibility of image
        app_bar_main.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val opacity = abs(verticalOffset) / appBarLayout.totalScrollRange.toFloat()
            btn_add_recipe.alpha = opacity
            btn_exit_add.alpha = opacity
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // When activity to get image finishes, image processing starts
        if (resultCode == Activity.RESULT_OK){
            val imageUri = data?.data
            // Image from gallery and camera are received differently
            // so they have to be decoded to bitmap differently
            // camera we get data from extras
            // gallery form data.data which is a Uri
            val bitmap = if(imageUri != null)
                MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            else
                data?.extras?.get("data") as Bitmap

            val imgWidth = 1000
            val imgHeight = (bitmap.height * imgWidth) / bitmap.width

            imageBitmap = Bitmap.createScaledBitmap(bitmap, imgWidth,imgHeight, false)

            bitmap.recycle()
            add_item_image.setImageBitmap(imageBitmap)
        }
    }
}