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
import sde.com.icar.model.car.Person
import sde.com.icar.model.data.BasicAuth
import sde.com.icar.model.data.ICarService

class CarDetailsViewModel : ViewModel() {

    private var mCar: MutableLiveData<Car?>? = null
    private var mPerson: MutableLiveData<Person?>? = null
    private val mErrorMessage = MutableLiveData<String?>()

    private var mCarService: ICarService? = null
    private var mCarId: String? = null
    private var mPersonId: String? = null

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

    fun getPersonById() {
        if (mPersonId!!.isEmpty()) {
            return
        }
        mCarService!!.getPersonById(mPersonId)!!.enqueue(object : Callback<Person?> {
            override fun onResponse(call: Call<Person?>, response: Response<Person?>) {
                if (!response.isSuccessful) {
                    showError(NetworkUtil.onApiResponseError(response))
                    return
                }
                mPerson!!.postValue(response.body())
            }

            override fun onFailure(call: Call<Person?>, t: Throwable) {
                showError(t.localizedMessage)
                mPerson!!.postValue(Person())
            }
        })
    }

    fun setPersonId(personId: String) {
        this.mPersonId = personId
    }

    val person: LiveData<Person?>
        get() {
            if (mPerson == null) {
                mPerson = MutableLiveData()
                getPersonById()
            }
            return mPerson as MutableLiveData<Person?>
        }

    fun getCarById() {
        if (mCarId!!.isEmpty()) {
            return
        }
        mCarService!!.getCarById(mCarId)!!.enqueue(object : Callback<Car?> {
            override fun onResponse(call: Call<Car?>, response: Response<Car?>) {
                if (!response.isSuccessful) {
                    showError(NetworkUtil.onApiResponseError(response))
                    return
                }
                mCar!!.postValue(response.body())
            }

            override fun onFailure(call: Call<Car?>, t: Throwable) {
                showError(t.localizedMessage)
            }
        })
    }

    fun setCarId(carId: String?) {
        this.mCarId = carId
    }

    val car: LiveData<Car?>
        get() {
            if (mCar == null) {
                mCar = MutableLiveData()
                getCarById()
            }
            return mCar as MutableLiveData<Car?>
        }

    init {
        init()
        initServices()
    }

    private fun showError(message: String) {
        mErrorMessage.postValue(message)
    }

    private fun init() {

        mErrorMessage.value = null
    }
}