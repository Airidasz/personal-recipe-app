package com.airidasz.recipestorageapp

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_add_item.*


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

        // Add recipe button, checks if fields are not empty, adds recipe if true
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