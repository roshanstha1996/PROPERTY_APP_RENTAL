package com.example.property_app_g01.models


import kotlinx.serialization.Serializable


@Serializable
data class Address(
    val streetAddress: String,
    val geo: Geo
)

@Serializable
data class Company(
    val name: String
)

@Serializable
data class Geo(
    val lat: String,
    val lng: String
)