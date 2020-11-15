package com.airidasz.recipestorageapp

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class MainActivityRecyclerViewAdapter// Constructor for the Class
    (contactsList: MutableList<Recipe>?, private val context: Context?) :
    RecyclerView.Adapter<MainActivityRecyclerViewAdapter.RecipeHolder>() {

    private var recipeList: MutableList<Recipe>? = contactsList

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
                LinearLayout.LayoutParams.WRAP_CONTENT)

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
        private val description : TextView = itemView.findViewById(R.id.recipe_description)

        fun setValues(recipe : Recipe?) {
            titleText.text = recipe?.name

            for (i in 0 until recipe?.ingredients?.count()!!) {
                val quantity = recipe.ingredients?.get(i)?.quantity!! * recipe.portion
                val measurements = "${recipe.ingredients?.get(i)?.measurement_units!!} ${recipe.ingredients?.get(i)?.ingredient}"

                if (quantity % 1 == 0F)
                    description.append("• ${quantity.toInt()} $measurements\n")
                else
                    description.append("• ${quantity} $measurements\n")
            }

            // If no ingredients exist, set text to recipe description
            if (description.text == "")
                description.text = recipe.description

            // Set image, then some picture scaling magic
            image.setImageBitmap(recipe.image)
            if (recipe.image != null){
                val height = 170F // 170dp or other dp value

                val r: Resources? = context?.resources
                // Convert dp to px
                val imageHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    height,
                    r?.displayMetrics
                ).toInt()

                val params = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    imageHeight)

                image.layoutParams = params
            }
        }

    }
}