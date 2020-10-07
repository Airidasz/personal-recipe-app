package com.airidasz.recipestorageapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.core.database.getBlobOrNull
import java.io.ByteArrayOutputStream


val DATABASE_NAME= "MyDB"
val TABLE_NAME="Recipes"
val COL_NAME = "name"
val COL_DESCRIPTION = "description"
val COL_ID = "id"
val COL_IMG = "image"

class DataBaseHandler (var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
//        val createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
//                COL_NAME + " VARCHAR(256)," + COL_DESCRIPTION + " VARCHAR(256))"

        val createTable = "Create table $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COL_IMG BLOB,$COL_NAME VARCHAR(256),$COL_DESCRIPTION VARCHAR(256))"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(recipe:Recipe) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_IMG, getBitmapAsByteArray(recipe.image))
        cv.put(COL_NAME, recipe.name)
        cv.put(COL_DESCRIPTION, recipe.description)
        val err = db.insert(TABLE_NAME, null, cv)
        if (err == -1.toLong()) 
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
    }

    fun readData() : MutableList<Recipe> {
        val list : MutableList<Recipe> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from $TABLE_NAME"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do{
                var recipe = Recipe()
                recipe.id = result.getInt(result.getColumnIndex(COL_ID))
                recipe.image = getByteArrayAsBitmap(result.getBlobOrNull(result.getColumnIndex(COL_IMG)))
                recipe.name = result.getString(result.getColumnIndex(COL_NAME))
                recipe.description = result.getString(result.getColumnIndex(COL_DESCRIPTION))
                list.add(recipe)
            }while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }

    fun getRecipe(i:Int) : Recipe{
        val db = this.readableDatabase
        val query = "Select * from $TABLE_NAME Where id = $i"
        val result = db.rawQuery(query, null)
        result.moveToFirst()

        var recipe = Recipe()

        recipe.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
        recipe.image = getByteArrayAsBitmap(result.getBlobOrNull(result.getColumnIndex(COL_IMG)))
        recipe.name = result.getString(result.getColumnIndex(COL_NAME))
        recipe.description = result.getString(result.getColumnIndex(COL_DESCRIPTION))

        result.close()
        db.close()

        return recipe
    }

    fun removeRecipe(i:Int) {
        val db = this.readableDatabase
        db.delete(TABLE_NAME, "id = $i", null)

        db.close()
    }

    fun getBitmapAsByteArray(bitmap: Bitmap?): ByteArray? {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }

    fun getByteArrayAsBitmap(byteArray: ByteArray?): Bitmap? {
        val size = byteArray?.count() as Int

        return BitmapFactory.decodeByteArray(byteArray, 0, size)
    }

}