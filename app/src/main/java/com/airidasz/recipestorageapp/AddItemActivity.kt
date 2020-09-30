package com.airidasz.recipestorageapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_item.*


class AddItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        var db = DataBaseHandler(this)

        btn_add_recipe.setOnClickListener {
            if(pavadinimas.text.toString().isNotEmpty() &&
                aprasymas.text.toString().isNotEmpty()
            ){
                var user = User(pavadinimas.text.toString(), aprasymas.text.toString())
                db.insertData(user)

//                val returnIntent = Intent()
//                returnIntent.putExtra("result", 1)
//                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            } else
                Toast.makeText(this, "Fill in fields", Toast.LENGTH_SHORT).show()
        }

        btn_exit_add.setOnClickListener {
            finish()
        }
    }
}