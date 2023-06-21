package com.fachmi.miweather.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fachmi.miweather.di.Injection
import com.fachmi.miweather.repository.CurrentWeatherRepository

class ViewModelFactory private constructor(
    private val currentWeatherRepository: CurrentWeatherRepository
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideCurrentWeatherRepository(context)
                )
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java) -> {
                CurrentWeatherViewModel(currentWeatherRepository) as T
            }

            else -> throw Throwable("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}