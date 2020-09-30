package com.airidasz.recipestorageapp

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        //var db = DataBaseHandler(this)

        btn_move_to_add_activity.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {  // After a pause OR at startup
        super.onResume()
        var db = DataBaseHandler(this)
        var data = db.readData()
        tvResult.text = ""
        for (i in 0..data.size-1){
            tvResult.append(data.get(i).id.toString() + " " + data.get(i).name + " " + data.get(i).description + "\n")
        }
    }

}