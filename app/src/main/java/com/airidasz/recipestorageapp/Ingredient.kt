package com.airidasz.recipestorageapp

class Ingredient {
    var id : Long = 0
    var recipeId:Long = 0
    var ingredient : String = ""
    var quantity : Float = 0.0f
    var measurement_units : String = ""

    constructor(recipeId:Long, ingredient:String, quantity:Float, measurement_units:String){
        this.recipeId = recipeId
        this.ingredient = ingredient
        this.quantity = quantity
        this.measurement_units=measurement_units
    }

    constructor(){}
}
