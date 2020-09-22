package sde.com.icar.model.car

import java.io.Serializable

data class Car(
        var _id: String? = null,
        var brand: String? = null,
        var color: String = "#00000a",
        var lat: Double = 0.0,
        var lng: Double = 0.0,
        var model: String? = null,
        var ownerId: String = "0",
        var registration: String? = null,
        var year: String? = null
) : Serializable