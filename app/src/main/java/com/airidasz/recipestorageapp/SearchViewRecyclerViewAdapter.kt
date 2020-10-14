package com.airidasz.recipestorageapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class SearchActivityRecyclerViewAdapter// Constructor for the Class
    (contactsList: MutableList<Recipe>?, context: Context?) :
    RecyclerView.Adapter<SearchActivityRecyclerViewAdapter.RecipeHolder>(), Filterable {

    private var recipeList: MutableList<Recipe>? = ArrayList()
    private var allRecipes : MutableList<Recipe> = contactsList!!

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
    }

    inner class RecipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image : ImageView = itemView.findViewById(R.id.recipe_image)
        private val titleText : TextView = itemView.findViewById(R.id.recipe_title)
        private val descriptionText : TextView = itemView.findViewById(R.id.recipe_description)

        fun setValues(recipe : Recipe?){
            titleText.text = recipe?.name
            descriptionText.text = recipe?.description
            image.setImageBitmap(recipe?.image)
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