package com.airidasz.recipestorageapp

import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

//        val searchView = findViewById<SearchView>(R.id.action_search_two)
//        searchView.isIconified = false
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchView = menu?.findItem(R.id.action_search_two)?.actionView as SearchView?

        searchView?.isIconified = false


        searchView?.setOnCloseListener {
            finish()
            return@setOnCloseListener true
        }
       // Toast.makeText(this,  searchView.toString(), Toast.LENGTH_SHORT).show();

        return true
    }
}