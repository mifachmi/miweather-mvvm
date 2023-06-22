package com.fachmi.miweather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fachmi.miweather.databinding.ActivityMainBinding
import com.fachmi.miweather.response.ApiResponse
import com.fachmi.miweather.response.CurrentWeatherResponse
import com.fachmi.miweather.utils.kelvinToCelsius
import com.fachmi.miweather.utils.unixTimestampToDateTimeString
import com.fachmi.miweather.utils.unixTimestampToTimeString
import com.fachmi.miweather.viewmodel.CurrentWeatherViewModel
import com.fachmi.miweather.viewmodel.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()

        // initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    getCurrentLocation()
                } else {
                    // permission denied
                    Toast.makeText(
                        this, "You need to grant permission to access location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel =
            ViewModelProvider(this, factory)[CurrentWeatherViewModel::class.java]
    }

    private fun getCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
            );
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location
                latitude = location.latitude
                longitude = location.longitude
                println("latitude: $latitude, longitude: $longitude")
                val cityName = getCityNameFromCoordinates(applicationContext, latitude, longitude)
                println("Current City: $cityName")
                fetchDataFromApi(latitude, longitude)

                // TODO: change the hardcode way
                fetchDataFromApiByCity(NEW_YORK)
                fetchDataFromApiByCity(SINGAPORE)
                fetchDataFromApiByCity(MUMBAI)
                fetchDataFromApiByCity(DELHI)
                fetchDataFromApiByCity(SYDNEY)
                fetchDataFromApiByCity(MELBOURNE)
            }
            .addOnFailureListener {
                Toast.makeText(
                    this, "Failed on getting current location",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getCityNameFromCoordinates(
        context: Context,
        latitude: Double,
        longitude: Double
    ): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var cityName = ""

        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address: Address = addresses[0]
                cityName = address.locality ?: ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return cityName
    }

    private fun fetchDataFromApi(lat: Double, lon: Double) {
        viewModel.getCurrentWeather(lat, lon).observe(this) { result ->
            when (result) {
                is ApiResponse.Success -> {
                    val data = result.data
                    populateDataFromApi(data)
                }

                is ApiResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    println("Error: ${result.errorMessage}")
                }

                is ApiResponse.Loading -> {
                    println("Loading...")
                }

                else -> {}
            }
        }
    }

    private fun fetchDataFromApiByCity(city: String) {
        viewModel.getCurrentWeatherByCityName(city).observe(this) { result ->
            when (result) {
                is ApiResponse.Success -> {
                    val data = result.data
                    /*when (data.name) {
                        NEW_YORK -> populateDataFromApi(data)
                    }*/
                    populateDataFromApi(data)
                }

                is ApiResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    println("Error: ${result.errorMessage}")
                }

                is ApiResponse.Loading -> {
                    println("Loading...")
                }

                else -> {}
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun populateDataFromApi(data: CurrentWeatherResponse) {
        binding.apply {
            progressBar.visibility = View.GONE
            tvDateTime.text = data.dt?.unixTimestampToDateTimeString()
            when (data.name) {
                NEW_YORK -> {
                    layoutItemOtherLocation.apply {
                        newYorkLocation.apply {
                            tvLocationOtherLocation.text = data.name
                            tvTimeOtherLocation.text = data.dt?.unixTimestampToTimeString()
                            tvTempOtherLocation.text =
                                data.main?.temp?.kelvinToCelsius().toString()
                            tvDescOtherLocation.text = data.weather?.get(0)?.main
                            Glide.with(this@MainActivity)
                                .load("https://openweathermap.org/img/w/${data.weather?.get(0)?.icon}.png")
                                .into(ivWeatherOtherLocation)
                        }
                    }
                }

                SINGAPORE -> {
                    layoutItemOtherLocation.apply {
                        singaporeLocation.apply {
                            tvLocationOtherLocation.text = data.name
                            tvTimeOtherLocation.text = data.dt?.unixTimestampToTimeString()
                            tvTempOtherLocation.text =
                                data.main?.temp?.kelvinToCelsius().toString()
                            tvDescOtherLocation.text = data.weather?.get(0)?.main
                            Glide.with(this@MainActivity)
                                .load("https://openweathermap.org/img/w/${data.weather?.get(0)?.icon}.png")
                                .into(ivWeatherOtherLocation)
                        }
                    }
                }

                MUMBAI -> {
                    layoutItemOtherLocation.apply {
                        mumbaiLocation.apply {
                            tvLocationOtherLocation.text = data.name
                            tvTimeOtherLocation.text = data.dt?.unixTimestampToTimeString()
                            tvTempOtherLocation.text =
                                data.main?.temp?.kelvinToCelsius().toString()
                            tvDescOtherLocation.text = data.weather?.get(0)?.main
                            Glide.with(this@MainActivity)
                                .load("https://openweathermap.org/img/w/${data.weather?.get(0)?.icon}.png")
                                .into(ivWeatherOtherLocation)
                        }
                    }
                }

                DELHI -> {
                    layoutItemOtherLocation.apply {
                        delhiLocation.apply {
                            tvLocationOtherLocation.text = data.name
                            tvTimeOtherLocation.text = data.dt?.unixTimestampToTimeString()
                            tvTempOtherLocation.text =
                                data.main?.temp?.kelvinToCelsius().toString()
                            tvDescOtherLocation.text = data.weather?.get(0)?.main
                            Glide.with(this@MainActivity)
                                .load("https://openweathermap.org/img/w/${data.weather?.get(0)?.icon}.png")
                                .into(ivWeatherOtherLocation)
                        }
                    }
                }

                SYDNEY -> {
                    layoutItemOtherLocation.apply {
                        sydneyLocation.apply {
                            tvLocationOtherLocation.text = data.name
                            tvTimeOtherLocation.text = data.dt?.unixTimestampToTimeString()
                            tvTempOtherLocation.text =
                                data.main?.temp?.kelvinToCelsius().toString()
                            tvDescOtherLocation.text = data.weather?.get(0)?.main
                            Glide.with(this@MainActivity)
                                .load("https://openweathermap.org/img/w/${data.weather?.get(0)?.icon}.png")
                                .into(ivWeatherOtherLocation)
                        }
                    }
                }

                MELBOURNE -> {
                    layoutItemOtherLocation.apply {
                        melbourneLocation.apply {
                            tvLocationOtherLocation.text = data.name
                            tvTimeOtherLocation.text = data.dt?.unixTimestampToTimeString()
                            tvTempOtherLocation.text =
                                data.main?.temp?.kelvinToCelsius().toString()
                            tvDescOtherLocation.text = data.weather?.get(0)?.main
                            Glide.with(this@MainActivity)
                                .load("https://openweathermap.org/img/w/${data.weather?.get(0)?.icon}.png")
                                .into(ivWeatherOtherLocation)
                        }
                    }
                }

                else -> {
                    layoutItemWeatherBasicInfo.apply {
                        tvCityCountryMain.text = data.name
                        tvTemperatureMain.text = data.main?.temp?.kelvinToCelsius().toString()
                        tvDescMain.text = data.weather?.get(0)?.main
                        tvLowTempMain.text = data.main?.tempMin?.kelvinToCelsius().toString()
                        tvHighTempMain.text = data.main?.tempMax?.kelvinToCelsius().toString()
                        Glide.with(this@MainActivity)
                            .load("https://openweathermap.org/img/w/${data.weather?.get(0)?.icon}.png")
                            .into(ivWeatherCondition)
                    }
                    layoutItemAdditionalInfoOne.apply {
                        tvFeelLikeValue.text = data.main?.feelsLike?.kelvinToCelsius().toString()
                        tvHumidityValue.text = "${data.main?.humidity}%"
                        tvVisibilityValue.text = "${data.visibility?.div(1000.0)} KM"
                    }
                    layoutItemAdditionalInfoTwo.apply {
                        tvSunriseTime.text = data.sys?.sunrise?.unixTimestampToTimeString()
                        tvSunsetTime.text = data.sys?.sunset?.unixTimestampToTimeString()
                    }
                }
            }
        }
    }

    companion object {
        private const val NEW_YORK = "New York"
        private const val SINGAPORE = "Singapore"
        private const val MUMBAI = "Mumbai"
        private const val DELHI = "Delhi"
        private const val SYDNEY = "Sydney"
        private const val MELBOURNE = "Melbourne"
    }
}