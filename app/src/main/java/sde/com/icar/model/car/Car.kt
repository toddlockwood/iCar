package sde.com.icar.model.car

import java.io.Serializable

data class Car(
    val brand: String,
    val color: String,
    val lat: Double,
    val lng: Double,
    val model: String,
    val ownerId: String,
    val registration: String,
    val year: String
) : Serializable