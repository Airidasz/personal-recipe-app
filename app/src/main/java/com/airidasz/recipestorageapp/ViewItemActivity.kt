package com.airidasz.recipestorageapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.activity_view_item.*
import kotlinx.android.synthetic.main.ingredient_item.view.*


class ViewItemActivity : AppCompatActivity() {
    private val db = DataBaseHandler(this)
    private var recipeId:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recipeId = intent.getLongExtra("recipe_id", 1)

        val recipe = db.getRecipe(recipeId)
        val ingredients = db.getIngredientsByRecipe(recipeId)

        view_item_title.text = recipe.name
        supportActionBar?.title = recipe.name

        view_item_description.text = recipe.description
        view_item_quantity.setText(recipe.portion.toString(), TextView.BufferType.EDITABLE)

        view_item_image.setImageBitmap(recipe.image)

//        if (recipe.image != null){ // If image exists, we set it's size
//            val height = 270F // 170dp or other dp value
//
//            val r: Resources? = this.resources
//            // Convert dp to px
//            val imageHeight = TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                height,
//                r?.displayMetrics
//            ).toInt()
//
//            val params = CollapsingToolbarLayout.LayoutParams(
//                CollapsingToolbarLayout.LayoutParams.MATCH_PARENT,
//                imageHeight)
//
//            view_item_image.layoutParams = params
//        }

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

    @SuppressLint("SetTextI18n")
    private fun changePortionSize(portion:Int, ingredients: ArrayList<Ingredient>) {
        for (i in 0 until view_item_ingredient_list.childCount) {
            val myView = view_item_ingredient_list.getChildAt(i)

            val quantity = ingredients[i].quantity * portion
            myView.ingredient_quantity.text = "•  $quantity"

            if (quantity % 1 == 0F) {
                myView.ingredient_quantity.text = "•  ${quantity.toInt()}"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.view_item_menu, menu)
        return true
    }

    override fun finish() {
        super.finish()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.view_item_delete->{
                db.removeRecipe(recipeId)
                finish()
            }
            R.id.view_item_modify-> {
                val intent = Intent(this, EditItemActivity::class.java)
                intent.putExtra("recipe_id", recipeId)
                startActivity(intent)
            }
            android.R.id.home->finish()
        }
        return true
    }
}