package com.airidasz.recipestorageapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        view_item_quantity.setText(recipe.portion.toString(), TextView.BufferType.EDITABLE)

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

            view_item_ingredient_list.addView(myView)
        }

        changePortionSize(recipe.portion, ingredients)

        view_item_quantity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (count > 0){
                    changePortionSize(Integer.parseInt(s.toString()), ingredients)
                } else {
                    changePortionSize(recipe.portion, ingredients)
                }
            }
        })

    }

    private fun changePortionSize(portion:Int, ingredients: ArrayList<Ingredient>) {
        for (i in 0 until view_item_ingredient_list.childCount) {
            val myView = view_item_ingredient_list.getChildAt(i)

            myView.ingredient_quantity.text =
                "• " + (ingredients[i].quantity * portion).toString()

            if (ingredients[i].quantity % 1 == 0F) {
                myView.ingredient_quantity.text =
                    "• " + (ingredients[i].quantity * portion).toInt()
                        .toString()
            }
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