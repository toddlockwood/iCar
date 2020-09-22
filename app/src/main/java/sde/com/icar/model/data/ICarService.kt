package sde.com.icar.model.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import sde.com.icar.model.car.Car
import sde.com.icar.model.car.Person

interface ICarService {

    @GET("rest/car-list")
    fun getCars(): Call<List<Car>>

    @GET("rest/car-list/{id}")
    fun getCarById(@Path("id") carId: String?): Call<Car?>?

    @GET("rest/person-list/{id}")
    fun getPersonById(@Path("id") personId: String?): Call<Person?>?

    @POST("/rest/car-list")
    fun createCar(@Body car: Car?): Call<Car?>?
}