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

const val DATABASE_NAME= "MyDB"
const val TABLE_RECIPES="recipes"
const val COL_NAME = "name"
const val COL_DESCRIPTION = "description"
const val COL_PORTION = "portion"
const val COL_ID = "id"
const val COL_IMG = "image"

const val TABLE_INGREDIENTS = "ingredients"
const val COL_RECIPE_ID = "recipe_id"
const val COL_INGREDIENT= "ingredient"
const val COL_QUANTITY= "quantity"
const val COL_MEASUREMENT_UNITS= "measurement_units"


class DataBaseHandler (var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createRecipeTable = "Create table $TABLE_RECIPES ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COL_IMG BLOB,$COL_NAME VARCHAR(256),$COL_DESCRIPTION VARCHAR(256),$COL_PORTION INTEGER)"
        val createIngredientTable = "Create table $TABLE_INGREDIENTS ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COL_RECIPE_ID INTEGER,$COL_INGREDIENT VARCHAR(256),$COL_QUANTITY INTEGER,$COL_MEASUREMENT_UNITS VARCHAR(256), FOREIGN KEY($COL_RECIPE_ID) REFERENCES recipe($COL_ID))"

        db?.execSQL(createRecipeTable)
        db?.execSQL(createIngredientTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertRecipe(recipe:Recipe):Long {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_IMG, getBitmapAsByteArray(recipe.image))
        cv.put(COL_NAME, recipe.name)
        cv.put(COL_PORTION, recipe.portion)
        cv.put(COL_DESCRIPTION, recipe.description)
        val id = db.insert(TABLE_RECIPES, null, cv)
        if (id == -1.toLong())
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()

        return id
    }

    fun insertIngredient(ingredient:Ingredient) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_RECIPE_ID,ingredient.recipeId)
        cv.put(COL_INGREDIENT,ingredient.ingredient)
        cv.put(COL_QUANTITY,ingredient.quantity)
        cv.put(COL_MEASUREMENT_UNITS,ingredient.measurement_units)

        val id = db.insert(TABLE_INGREDIENTS, null, cv)
        if (id == -1.toLong())
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()

    }

    fun readRecipeData() : MutableList<Recipe> {
        val list : MutableList<Recipe> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from $TABLE_RECIPES"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do{
                val recipe = Recipe()
                recipe.id = result.getLong(result.getColumnIndex(COL_ID))
                recipe.image = getByteArrayAsBitmap(result.getBlobOrNull(result.getColumnIndex(COL_IMG)))
                recipe.name = result.getString(result.getColumnIndex(COL_NAME))
                recipe.description = result.getString(result.getColumnIndex(COL_DESCRIPTION))
                recipe.portion = result.getInt(result.getColumnIndex(COL_PORTION))
                list.add(recipe)
            }while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }

    fun getRecipe(i:Int) : Recipe{
        val db = this.readableDatabase
        val query = "Select * from $TABLE_RECIPES Where id = $i"
        val result = db.rawQuery(query, null)
        result.moveToFirst()

        var recipe = Recipe()

        recipe.id = result.getString(result.getColumnIndex(COL_ID)).toLong()
        recipe.image = getByteArrayAsBitmap(result.getBlobOrNull(result.getColumnIndex(COL_IMG)))
        recipe.name = result.getString(result.getColumnIndex(COL_NAME))
        recipe.description = result.getString(result.getColumnIndex(COL_DESCRIPTION))

        result.close()
        db.close()

        return recipe
    }

    fun removeRecipe(i:Int) {
        val db = this.readableDatabase
        db.delete(TABLE_RECIPES, "id = $i", null)

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