package sde.com.icar.model.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import sde.com.icar.model.car.Car

interface ICarService {

    //https://reqbin.com/

    @GET("rest/car-list")
    fun getCars(): Call<List<Car>>

    @GET("rest/car-list/{_id}")
    fun getCarById(@Path("gistId") carId: String?): Call<Car?>?


}