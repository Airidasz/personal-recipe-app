package com.airidasz.recipestorageapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_view_item.*
import kotlinx.android.synthetic.main.ingredient_item.view.*
import kotlinx.android.synthetic.main.ingredient_layout.view.*

class ViewItemActivity : AppCompatActivity() {
    private val db = DataBaseHandler(this)
    private var recipeId:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recipeId = intent.getLongExtra("recipe_id", 1)

        val recipe = db.getRecipe(recipeId)
        val ingredients = db.getIngredientsByRecipeId(recipeId)

        view_item_title.text = recipe.name
        supportActionBar?.title = recipe.name

        view_item_description.text = recipe.description

        view_item_image.setImageBitmap(recipe.image)

        view_item_image.setOnClickListener{
            val intent = Intent(this, ViewImageActivity::class.java)
            intent.putExtra("recipe_id", recipeId)
            startActivity(intent)
        }

        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        for(value in ingredients){
            val myView: View = inflater.inflate(R.layout.ingredient_item, view_item_ingredient_list, false)

            myView.ingredient_name.text = value.ingredient
            myView.ingredient_measurement_units.text = value.measurement_units
            myView.ingredient_quantity.text = value.quantity.toString()

            view_item_ingredient_list.addView(myView)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.view_item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.view_item_delete->{
                db.removeRecipe(recipeId)
                finish()
            }
            R.id.view_item_modify-> Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show()
            android.R.id.home->finish()
        }
        return true
    }
}