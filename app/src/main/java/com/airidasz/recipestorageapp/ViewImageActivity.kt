package com.airidasz.recipestorageapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_view_image.*
import kotlinx.android.synthetic.main.ingredient_layout.view.*
import kotlin.math.abs

class ViewImageActivity() : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)

        val recipeId = intent.getIntExtra("recipe_id", 1)

        val db = DataBaseHandler(this)
        val data = db.getRecipe(recipeId)

        data.image?.let { view_image_image.setImageBitmap(it) }
    }
}