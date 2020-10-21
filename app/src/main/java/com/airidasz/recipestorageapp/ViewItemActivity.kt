package com.airidasz.recipestorageapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_view_item.*

class ViewItemActivity : AppCompatActivity() {
    private val db = DataBaseHandler(this)
    private var recipeId:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recipeId = intent.getIntExtra("recipe_id", 1)

        val data = db.getRecipe(recipeId)

        view_item_title.text = data.name
        supportActionBar?.title = data.name

        view_item_description.text = data.description

        view_item_image.setImageBitmap(data.image)

        view_item_image.setOnClickListener{
            val intent = Intent(this, ViewImageActivity::class.java)
            intent.putExtra("recipe_id", recipeId)
            startActivity(intent)
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