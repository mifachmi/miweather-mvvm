package com.fachmi.miweather.response

import com.fachmi.miweather.network.ApiService

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
}