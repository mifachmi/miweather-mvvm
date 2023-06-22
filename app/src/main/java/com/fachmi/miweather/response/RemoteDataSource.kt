package com.fachmi.miweather.response

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fachmi.miweather.network.ApiService
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RemoteDataSource private constructor(
    private val apiService: ApiService
) {

    companion object {
        private val TAG = RemoteDataSource::class.java.simpleName.toString()
        private const val ERROR_MESSAGE_IO_EXCEPTION = "Check your internet connection"

        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiService: ApiService): RemoteDataSource {
            return instance ?: synchronized(this) {
                instance ?: RemoteDataSource(apiService)
            }
        }
    }

    private fun convertErrorBody(errorBody: ResponseBody?): CurrentWeatherResponse? {
        return try {
            val errorResponse = errorBody?.string()
            /*val jsonObject = JSONObject(errorResponse.toString())
            val errorsJson = jsonObject.getJSONObject("message")*/
            println("errorResponse: $errorResponse")

            val errorCurrentWeatherResponse = CurrentWeatherResponse(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
            errorCurrentWeatherResponse
        } catch (e: IOException) {
            null
        }
    }

    fun getCurrentWeather(lat: Double, lon: Double): LiveData<ApiResponse<CurrentWeatherResponse>> {
        val result = MutableLiveData<ApiResponse<CurrentWeatherResponse>>().apply {
            value = ApiResponse.Loading
        }

        apiService.getCurrentWeather(lat, lon)
            .enqueue(object : Callback<CurrentWeatherResponse> {
                override fun onResponse(
                    call: Call<CurrentWeatherResponse>,
                    response: Response<CurrentWeatherResponse>,
                ) {
                    val data = response.body()
                    result.value = if (response.isSuccessful && data != null) {
                        ApiResponse.Success(data)
                    } else {
                        val errorResp: CurrentWeatherResponse? =
                            convertErrorBody(response.errorBody())
                        ApiResponse.Error(errorResp?.msg.toString())
                    }
                }

                override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                    val errorMessage =
                        if (t is IOException) ERROR_MESSAGE_IO_EXCEPTION
                        else t.message.toString()
                    result.value = ApiResponse.Error(errorMessage)
                    Log.e(TAG, "${call.request().url}\n${t.stackTraceToString()}")
                }
            })

        return result
    }

    fun getCurrentWeatherByCity(cityName: String): LiveData<ApiResponse<CurrentWeatherResponse>> {
        val result = MutableLiveData<ApiResponse<CurrentWeatherResponse>>().apply {
            value = ApiResponse.Loading
        }

        apiService.getCurrentWeatherByCity(cityName)
            .enqueue(object : Callback<CurrentWeatherResponse> {
                override fun onResponse(
                    call: Call<CurrentWeatherResponse>,
                    response: Response<CurrentWeatherResponse>,
                ) {
                    val data = response.body()
                    result.value = if (response.isSuccessful && data != null) {
                        ApiResponse.Success(data)
                    } else {
                        val errorResp: CurrentWeatherResponse? =
                            convertErrorBody(response.errorBody())
                        ApiResponse.Error(errorResp?.msg.toString())
                    }
                }

                override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                    val errorMessage =
                        if (t is IOException) ERROR_MESSAGE_IO_EXCEPTION
                        else t.message.toString()
                    result.value = ApiResponse.Error(errorMessage)
                    Log.e(TAG, "${call.request().url}\n${t.stackTraceToString()}")
                }
            })

        return result
    }
}