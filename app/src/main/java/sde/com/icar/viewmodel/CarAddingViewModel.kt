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

class CarAddingViewModel : ViewModel() {

    private var mCar: Car? = null
    private var mCarService: ICarService? = null
    private val mErrorMessage = MutableLiveData<String?>()
    private var mIsCarAdded: MutableLiveData<Boolean?>? = MutableLiveData()

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

    fun createCar() {
        if (mCar == null) {
            return
        }

        mCarService!!.createCar(mCar)!!.enqueue(object : Callback<Car?> {
            override fun onResponse(call: Call<Car?>, response: Response<Car?>) {
                if (!response.isSuccessful) {
                    showError(NetworkUtil.onApiResponseError(response))
                    return
                }
                mIsCarAdded!!.postValue(true)
            }

            override fun onFailure(call: Call<Car?>, t: Throwable) {
                showError(t.localizedMessage)
            }
        })
    }

    val carAddedB: LiveData<Boolean?>?
        get() = mIsCarAdded

    private fun showError(message: String) {
        mErrorMessage.postValue(message)
    }

    fun setCar(car: Car?) {
        this.mCar = car
    }

    init {
        init()
        initServices()
    }

    private fun init() {
    }
}