package com.example.occupath

class User {
    var uid:String?= null
    var name:String? = null
    var email:String? = null
    var profileImage:String? = null
    constructor(){}
    constructor(
        uid:String?,
        name:String?,
        phoneNumber:String?,
        profileImage:String?){
        this.uid = uid
        this.name = name
        this.email = phoneNumber
        this.profileImage = profileImage
    }
}