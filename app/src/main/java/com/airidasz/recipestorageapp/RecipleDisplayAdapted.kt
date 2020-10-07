package com.airidasz.recipestorageapp

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecipeDisplayAdapted(private val context: Activity, private val data: MutableList<Recipe>)
    : ArrayAdapter<Recipe>(context, R.layout.recipe_item, data) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.recipe_item, null, true)

        val image = rowView.findViewById(R.id.recipe_image) as ImageView
        val titleText = rowView.findViewById(R.id.recipe_title) as TextView
        val descriptionText = rowView.findViewById(R.id.recipe_description) as TextView

        titleText.text = data[position].name
        descriptionText.text = data[position].description
        image.setImageBitmap(data[position].image)

        return rowView
    }
}