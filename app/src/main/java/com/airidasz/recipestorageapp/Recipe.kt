package com.airidasz.recipestorageapp

import android.graphics.Bitmap

class Recipe {
    var id : Long = 0
    var name : String = ""
    var description : String = ""
    var portion:Int = 1;
    var image:Bitmap? = null

    constructor(name:String, description:String, portion:Int,image:Bitmap?){
        this.name = name
        this.description = description
        this.portion = portion
        this.image = image
    }

    constructor(){}
}