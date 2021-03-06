package com.airidasz.recipestorageapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar_main))

        val searchIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_search_24)!!
        supportActionBar?.setIcon(searchIcon)

        btn_move_to_add_activity.setOnClickListener {
            //Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG).show()
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        toolbar_main.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
        }

        toolbar_main.setNavigationOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
        }

    }

    override fun onResume() {  // After a pause OR at startup
        super.onResume()

        val recycler = findViewById<RecyclerView>(R.id.recipe_list)
        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager

        val db = DataBaseHandler(this)
        val recipes = db.readRecipes()

        if(recipes.size <=1)
            app_bar_main.setExpanded(true)

        if(recipes.size <= 0)
            activity_main_empty_message.visibility = TextView.VISIBLE
        else
            activity_main_empty_message.visibility = TextView.INVISIBLE

        val listAdapter = MainActivityRecyclerViewAdapter(recipes, this)
        recycler.adapter = listAdapter
        
    }






}