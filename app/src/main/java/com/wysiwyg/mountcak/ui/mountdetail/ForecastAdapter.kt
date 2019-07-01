package com.wysiwyg.mountcak.ui.mountdetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Forecast
import com.wysiwyg.mountcak.data.model.Temperature
import com.wysiwyg.mountcak.data.model.Weather
import kotlinx.android.synthetic.main.item_forecast.view.*
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(private val forecast: MutableList<Forecast?>) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false))
    }

    override fun getItemCount(): Int {
        return forecast.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(
            forecast[position]!!.weather[0],
            forecast[position],
            forecast[position]!!.main
        )
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bindItem(
            weather: Weather?,
            forecast: Forecast?,
            temperature: Temperature?
        ) {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val sdf = SimpleDateFormat("EEEE, HH:mm", Locale.getDefault())
            val icon = "http://openweathermap.org/img/wn/${weather?.icon}@2x.png"

            Glide.with(itemView.context).load(icon).into(itemView.imgWeather)
            itemView.tvDay.text = sdf.format(format.parse(forecast?.dt_txt))
            itemView.tvCurrentWeather.text = weather?.description
            itemView.tvTemperature.text = "${temperature?.temp_min}\u00B0 - ${temperature?.temp_max}\u00B0 C"
        }
    }
}