package com.airidasz.recipestorageapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity() {
    lateinit var backArrowIcon : Drawable
    lateinit var closeIcon : Drawable
    var searchBarNoText:Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(findViewById(R.id.toolbar_search))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        backArrowIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24_black)!!
        closeIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_close_24_black)!!

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recycler = findViewById<RecyclerView>(R.id.recipe_list_search)
        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager

        val db = DataBaseHandler(this)
        val data = db.readRecipes()

        val listAdapter = SearchActivityRecyclerViewAdapter(data, this)
        recycler.adapter = listAdapter

        search_window_search.isIconified = false

        search_window_search.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()){
                    supportActionBar?.setHomeAsUpIndicator(backArrowIcon)
                    searchBarNoText=true
                }
                else {
                    supportActionBar?.setHomeAsUpIndicator(closeIcon)
                    searchBarNoText=false
                }

                listAdapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home ->{
                if(searchBarNoText){
                    finish()
                } else {
                    search_window_search.setQuery("", false)
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}