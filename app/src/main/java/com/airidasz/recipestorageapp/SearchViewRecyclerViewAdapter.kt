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
import java.util.*
import kotlin.collections.ArrayList

class SearchActivityRecyclerViewAdapter// Constructor for the Class
    (sentRecipesList: MutableList<Recipe>?, context: Context?) :
    RecyclerView.Adapter<SearchActivityRecyclerViewAdapter.RecipeHolder>(), Filterable {

    private var recipeList: MutableList<Recipe>? = ArrayList()
    private var allRecipes : MutableList<Recipe> = sentRecipesList!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.recipe_item_search, parent, false)
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
    }

    inner class RecipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText : TextView = itemView.findViewById(R.id.recipe_search_title)
        private val description : TextView = itemView.findViewById(R.id.recipe_search_description)

        fun setValues(recipe : Recipe?) {
            titleText.text = recipe?.name

            for (i in 0 until recipe?.ingredients?.count()!!) {
                val quantity = recipe.ingredients?.get(i)?.quantity!! * recipe.portion
                val measurements = "${recipe.ingredients?.get(i)?.measurement_units!!} ${recipe.ingredients?.get(i)?.ingredient}"

                if (quantity % 1 == 0F)
                    description.append("• ${quantity.toInt()} $measurements\n")
                else
                    description.append("• $quantity $measurements\n")
            }

            // If no ingredients exist, set text to recipe description
            if (description.text == "")
                description.text = recipe.description
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            //Automatic on background thread
            override fun performFiltering(charSequence: CharSequence?): FilterResults? {
                val filteredList: MutableList<Recipe> = ArrayList()

                if (charSequence.isNullOrBlank()) {
                    filteredList.clear()
                } else {
                    for (recipe in allRecipes) {
                        if (recipe.name.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filteredList.add(recipe)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                recipeList?.clear()
                recipeList?.addAll(0, results?.values as Collection<Recipe>)
                notifyDataSetChanged()
            }
        }
    }
}