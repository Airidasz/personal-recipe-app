package com.airidasz.recipestorageapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.security.AccessControlContext

val DATABASE_NAME= "MyDB"
val TABLE_NAME="Recipes"
val COL_NAME = "name"
val COL_DESCRIPTION = "description"
val COL_ID = "id"

class DataBaseHandler (var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COL_NAME + " VARCHAR(256)," + COL_DESCRIPTION + " VARCHAR(256))"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(user:User) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, user.name)
        cv.put(COL_DESCRIPTION, user.description)
        var err = db.insert(TABLE_NAME, null, cv)
        if (err == -1.toLong()) 
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
    }

    fun readData() : MutableList<User> {
        var list : MutableList<User> = ArrayList()

        var db = this.readableDatabase
        var query = "Select * from " + TABLE_NAME
        var result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do{
                var user = User()
                user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                user.name = result.getString(result.getColumnIndex(COL_NAME))
                user.description = result.getString(result.getColumnIndex(COL_DESCRIPTION))
                list.add(user)
            }while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }
}