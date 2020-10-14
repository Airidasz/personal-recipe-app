package com.airidasz.recipestorageapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
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

        backArrowIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24)!!
        closeIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_close_24)!!

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val recycler = findViewById<RecyclerView>(R.id.recipe_list_search)
        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager

        val db = DataBaseHandler(this)
        val data = db.readData()

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

                //(recycler.adapter as SearchActivityRecyclerViewAdapter).filter.filter(newText)
                listAdapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

//        val searchCloseImage = search_window_search.findViewById<ImageView>(R.id.search_close_btn)
//        searchCloseImage.isEnabled = false;
//        searchCloseImage.setImageDrawable(null)
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

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.search_menu, menu)
//
//        val searchView = menu?.findItem(R.id.action_search_two)?.actionView as SearchView?
//
//        searchView?.isIconified = false
//
//
//       // Toast.makeText(this,  searchView.toString(), Toast.LENGTH_SHORT).show();
//
//        return true
//    }
}