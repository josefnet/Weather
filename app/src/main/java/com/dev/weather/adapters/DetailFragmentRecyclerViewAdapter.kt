package com.dev.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.weather.R
import com.dev.weather.model.WeatherModel
import kotlinx.android.synthetic.main.one_row_detail_weather.view.*
import kotlin.math.round

class DetailFragmentRecyclerViewAdapter(
    var weatherModel: WeatherModel?
) : RecyclerView.Adapter<DetailFragmentRecyclerViewAdapter.CustomViewHolder>() {

    override fun getItemCount(): Int {
        return 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.one_row_detail_weather, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.itemView.apply {
            weatherModel?.let {
                val tempMin =
                    round(weatherModel?.findMin(position * 7, position * 7 + 7)!!).toInt()
                        .toString()
                        .plus("° / ")
                val tempMax =
                    round(weatherModel?.findMax(position * 7, position * 7 + 7)!!).toInt()
                        .toString()
                        .plus("°")
                tvDetailDegree.text = tempMin.plus(tempMax)


                imgWeatherIcon.setImageDrawable(
                    when (weatherModel?.list?.get(position * 8)!!.weather[0].icon) {
                        "01d" -> resources.getDrawable(R.drawable.img_01d, null)
                        "01n" -> resources.getDrawable(R.drawable.img_01n, null)
                        "02d" -> resources.getDrawable(R.drawable.img_02d, null)
                        "02n" -> resources.getDrawable(R.drawable.img_02n, null)
                        "03d" -> resources.getDrawable( R.drawable.img_03d,null)
                        "03n" -> resources.getDrawable( R.drawable.img_03n,null)
                        "04d" -> resources.getDrawable(R.drawable.img_04d, null)
                        "04n" -> resources.getDrawable(R.drawable.img_04n, null)
                        "09d" -> resources.getDrawable(R.drawable.img_09d, null)
                        "09n" -> resources.getDrawable(R.drawable.img_09n, null)
                        "10d" -> resources.getDrawable(R.drawable.img_10d, null)
                        "10n" -> resources.getDrawable(R.drawable.img_10n, null)
                        "11d" -> resources.getDrawable(R.drawable.img_11d, null)
                        "11n" -> resources.getDrawable(R.drawable.img_11n, null)
                        "13d" -> resources.getDrawable(R.drawable.img_13d, null)
                        "13n" -> resources.getDrawable(R.drawable.img_13n, null)
                        "50d" -> resources.getDrawable(R.drawable.img_50d, null)
                        "50n" -> resources.getDrawable(R.drawable.img_50n, null)
                        else -> resources.getDrawable(R.drawable.ic_humidity, null)
                    }
                )
            }
        }
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun WeatherModel.findMin(startIndex: Int, endIndex: Int) =
        this.list.subList(startIndex, endIndex).minBy { it.main.temp_min }?.main?.temp_min

    private fun WeatherModel.findMax(startIndex: Int, endIndex: Int) =
        this.list.subList(startIndex, endIndex).maxBy { it.main.temp_max }?.main?.temp_max
}