package com.airidasz.recipestorageapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.ingredient_layout.view.*


class AddItemActivity() : AppCompatActivity() {
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
//        setSupportActionBar(findViewById(R.id.toolbar_add_item))

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

//            val params = AppBarLayout.LayoutParams(
//                AppBarLayout.LayoutParams.MATCH_PARENT,
//                AppBarLayout.LayoutParams.MATCH_PARENT
//            )
////            val a = add_recipe_fields.height
////
////            Toast.makeText(this, "$a", Toast.LENGTH_SHORT).show()
//
//
//            add_item_scroll.layoutParams = params

        }
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