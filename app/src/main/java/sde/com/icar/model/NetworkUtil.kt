package sde.com.icar.model

import retrofit2.Response


object NetworkUtil {

    fun onApiResponseError(response: Response<*>):String{
        if (response.code() == 403){
            return "Token incorrect"
        }
        return response.message()
    }

}