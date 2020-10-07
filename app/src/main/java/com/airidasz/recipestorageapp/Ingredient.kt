package com.airidasz.recipestorageapp

class Ingredient {
    var id : Int = 0
    var recipeId:Int = 0
    var ingredientName : String = ""
    var quantity : Float = 0.0f
    var measurement_units : String = ""

    constructor(recipeId:Int, ingredientName:String, quantity:Float, measurement_units:String){
        this.recipeId = recipeId
        this.ingredientName = ingredientName
        this.quantity = quantity
        this.measurement_units=measurement_units
    }

    constructor(){}
}
