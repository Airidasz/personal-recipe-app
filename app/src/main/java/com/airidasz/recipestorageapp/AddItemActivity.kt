package com.airidasz.recipestorageapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.ingredient_layout.view.*
import kotlin.math.abs


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

            // Check if the user has the ability to take pictures
            if(cameraIntent.resolveActivity(packageManager) != null)
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

            startActivityForResult(chooser, 100)

        }

       //  Add recipe button, checks if fields are not empty, adds recipe if true
        btn_add_recipe.setOnClickListener {
            if(add_item_title.text.toString().isNotEmpty() &&
                add_item_description.text.toString().isNotEmpty()
            ){
                val recipe = Recipe()
                recipe.name = add_item_title.text.toString()
                recipe.description = add_item_description.text.toString()
                recipe.portion = add_item_quantity.text.toString().toInt()
                recipe.image = imageBitmap

                val id = db.insertRecipe(recipe)

                for (i in 0 until add_item_ingredient_list.childCount) {
                    val view = add_item_ingredient_list.getChildAt(i)

                    val ingredient = Ingredient()
                    ingredient.recipeId = id
                    ingredient.ingredient = view.add_item_ingredient_name.text.toString()
                    ingredient.quantity = view.add_item_ingredient_quantity.text.toString().toFloat() / recipe.portion
                    ingredient.measurement_units = view.add_item_ingredient_measurement_units.text.toString()
                    db.insertIngredient(ingredient)
                }

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
            val myView: View = inflater.inflate(R.layout.ingredient_layout, add_item_ingredient_list, false)

            myView.btn_remove_ingredient.setOnClickListener {
                add_item_ingredient_list.removeView(myView)
            }

            add_item_ingredient_list.addView(myView)

            add_item_nested_scroll.post(Runnable { add_item_nested_scroll.fullScroll(View.FOCUS_DOWN) })
        }

        // Change cancel and add recipe button opacity based on the visibility of image
        app_bar_main.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val opacity = abs(verticalOffset) / appBarLayout.totalScrollRange.toFloat()
            if (opacity == 0F) {
                btn_add_recipe.visibility = View.GONE
                btn_exit_add.visibility = View.GONE
            }
            else {
                btn_add_recipe.visibility = View.VISIBLE
                btn_exit_add.visibility = View.VISIBLE
            }

            btn_add_recipe.alpha = opacity
            btn_exit_add.alpha = opacity
        })
    }

    private fun addIngredientField(): View{
        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val myView: View = inflater.inflate(R.layout.ingredient_layout, add_item_ingredient_list, false)

        myView.btn_remove_ingredient.setOnClickListener {
            add_item_ingredient_list.removeView(myView)
        }

        add_item_ingredient_list.addView(myView)

        return myView
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

