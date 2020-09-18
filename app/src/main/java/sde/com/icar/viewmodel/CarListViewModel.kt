package sde.com.icar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sde.com.icar.model.Constants
import sde.com.icar.model.NetworkUtil
import sde.com.icar.model.car.Car
import sde.com.icar.model.data.BasicAuth
import sde.com.icar.model.data.ICarService
import java.util.ArrayList

class CarListViewModel : ViewModel() {

    private val mDiscoveredCars = MutableLiveData<MutableList<Car>?>()
    private val mErrorMessage = MutableLiveData<String?>()

    private var mCarService : ICarService? = null

    private fun initServices() {
        val gson = GsonBuilder().setDateFormat("YYYY-MM-DD'T'HH:MM:SSZ").create()

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(BasicAuth(Constants.TOKEN))
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        mCarService = retrofit.create(ICarService::class.java)
    }

    fun discoverCarList(){
        mCarService!!.getCars()!!.enqueue(object : Callback<List<Car>?> {
            override fun onResponse(call: Call<List<Car>?>, response: Response<List<Car>?>) {
                if (!response.isSuccessful){
                    showError(NetworkUtil.onApiResponseError(response))
                    return
                }
                var currentList = mDiscoveredCars.value
                if (currentList == null){
                    currentList = ArrayList()
                }
                if (response.body() != null){
                    currentList.addAll(response.body()!!)
                }
                mDiscoveredCars.postValue(currentList)
            }
            override fun onFailure(call: Call<List<Car>?>, t: Throwable) {
                showError(t.localizedMessage)
            }
        })
    }



    val discoveredCars : LiveData<MutableList<Car>?>
        get(){
            discoverCarList()
            return mDiscoveredCars
        }

    private fun showError(message: String) {
        mErrorMessage.postValue(message)
    }

    init {
        init()
        initServices()
    }

    private fun init() {
        mDiscoveredCars.value = null
        mErrorMessage.value = null
    }

}