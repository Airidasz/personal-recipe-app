package com.airidasz.recipestorageapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ViewItemActivity : AppCompatActivity() {
    private val db = DataBaseHandler(this)
    private var recipeId:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recipeId = intent.getIntExtra("recipe_id", 1)

        val data = db.getRecipe(recipeId)

        val title: TextView = findViewById(R.id.view_item_title)
        title.text = data.name
        supportActionBar?.title = data.name

        val description: TextView = findViewById(R.id.view_item_description)
        description.text = data.description

        val image: ImageView = findViewById(R.id.view_item_image)
        image.setImageBitmap(data.image)

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