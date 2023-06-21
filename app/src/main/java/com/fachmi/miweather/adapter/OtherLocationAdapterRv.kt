package com.fachmi.miweather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.fachmi.miweather.databinding.ItemOtherLocationBinding
import com.fachmi.miweather.model.OtherLocationWeatherModel
import com.fachmi.miweather.utils.kelvinToCelsius

class OtherLocationAdapterRv(private val context: Context) :
    RecyclerView.Adapter<OtherLocationAdapterRv.OtherLocationVH>() {

    private val otherLocationList = mutableListOf<OtherLocationWeatherModel>()

    fun submitList(newList: MutableList<OtherLocationWeatherModel>) {
        otherLocationList.apply {
            notifyItemRangeRemoved(0, itemCount)
            clear()
            addAll(newList)
            notifyItemRangeChanged(0, itemCount)
        }
    }

    inner class OtherLocationVH(private val binding: ItemOtherLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(otherLocationWeatherModel: OtherLocationWeatherModel) {
            val imageUri =
                "https://openweathermap.org/img/w/${otherLocationWeatherModel.iconUrl}.png"
            binding.apply {
                tvLocationOtherLocation.text = otherLocationWeatherModel.city
                tvDescOtherLocation.text = otherLocationWeatherModel.description
                tvTempOtherLocation.text =
                    otherLocationWeatherModel.temperature.kelvinToCelsius().toString()
                ivWeatherOtherLocation.setImageURI(imageUri.toUri())
                tvTimeOtherLocation.text = otherLocationWeatherModel.dateTime
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherLocationVH {
        val binding = ItemOtherLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OtherLocationVH(binding)
    }

    override fun getItemCount(): Int = otherLocationList.size

    override fun onBindViewHolder(holder: OtherLocationVH, position: Int) {
        holder.bind(otherLocationList[position])
    }
}