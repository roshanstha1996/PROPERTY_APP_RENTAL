package com.example.property_app_g01.models

import com.google.firebase.firestore.DocumentId

data class PropertyDetail(
    @DocumentId
    var id:String = "",
    var imageURL:String = "",
    var monthlyRent:String = "",
    var bedroomCount:String = "",
    var ownerId:String = "",
    val streetAddress: String = "",
    val lat: String = "",
    val lng: String = "",
    @JvmField
    var isRented:Boolean = false

)
