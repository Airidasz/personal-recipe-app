package com.airidasz.recipestorageapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar_main))

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
    }

    override fun onResume() {  // After a pause OR at startup
        super.onResume()

        val recycler = findViewById<RecyclerView>(R.id.recipe_list)
        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager

        val db = DataBaseHandler(this)
        val data = db.readData()

        val listAdapter = MyAdapter(data, this)
        recycler.adapter = listAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_settings-> {
                Toast.makeText(this,  "Not yet", Toast.LENGTH_SHORT).show();
            }
        }
        return true
    }


}