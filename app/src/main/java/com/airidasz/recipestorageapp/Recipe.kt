package com.airidasz.recipestorageapp

import android.graphics.Bitmap

class Recipe {
    var id : Int = 0
    var name : String = ""
    var description : String = ""
    var image:Bitmap? = null

    constructor(name:String, description:String, image:Bitmap?){
        this.name = name
        this.description = description
        this.image = image
    }

    constructor(){}
}