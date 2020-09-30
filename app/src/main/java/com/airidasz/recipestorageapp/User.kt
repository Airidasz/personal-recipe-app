package com.airidasz.recipestorageapp

class User {
    var id : Int = 0
    var name : String = ""
    var description : String = ""

    constructor(name:String, description:String){
        this.name = name
        this.description = description
    }

    constructor(){}
}