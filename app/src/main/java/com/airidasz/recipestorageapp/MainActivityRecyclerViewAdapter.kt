package com.airidasz.recipestorageapp

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainActivityRecyclerViewAdapter// Constructor for the Class
    (contactsList: MutableList<Recipe>?, context :Context?) :
    RecyclerView.Adapter<MainActivityRecyclerViewAdapter.RecipeHolder>() {

    private var recipeList: MutableList<Recipe>? = contactsList
    private val context:Context? = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.recipe_item, parent, false)
        return RecipeHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeList?.size as Int
    }


    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        val recipe = recipeList?.get(position)

        holder.setValues(recipe)

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, ViewItemActivity::class.java)
            intent.putExtra("recipe_id", recipe?.id)
            it.context.startActivity(intent)
        }

        if(position ==  0){ // add a top and bottom margin for first card
            val firstElement = holder.itemView

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)

            val r: Resources? = context?.resources
            // Fancy dp to px conversion, since you can only set pixel programmatically
            val horizontal = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                15F,
                r?.displayMetrics
            ).toInt()
            val vertical = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20F,
                r?.displayMetrics
            ).toInt()

            params.setMargins(horizontal, vertical, horizontal, vertical)

            firstElement.layoutParams = params
        }
    }

    inner class RecipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image : ImageView = itemView.findViewById(R.id.recipe_image)
        private val titleText : TextView = itemView.findViewById(R.id.recipe_title)
        private val descriptionText : TextView= itemView.findViewById(R.id.recipe_description)

        fun setValues(recipe : Recipe?){
            titleText.text = recipe?.name
            descriptionText.text = recipe?.description
            image.setImageBitmap(recipe?.image)
        }

    }
}