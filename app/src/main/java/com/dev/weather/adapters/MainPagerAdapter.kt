package com.dev.weather.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dev.weather.R
import com.dev.weather.db.City
import com.dev.weather.model.WeatherModel
import com.dev.weather.viewmodel.city.CityViewModel
import com.dev.weather.viewmodel.weather.WeatherViewModel
import kotlinx.android.synthetic.main.city_item_container.view.*
import kotlin.math.round
import com.dev.weather.ui.home.fragments.DisplayFragmentDirections

class MainPagerAdapter(
    var cityList: List<City>,
    private val viewModel: CityViewModel,
    private var weatherModel: WeatherViewModel,
    private val owner: LifecycleOwner
) : RecyclerView.Adapter<MainPagerAdapter.CustomViewHolder>() {

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
           // LayoutInflater.from(parent.context).inflate(R.layout.city_item_container, parent, false)
            LayoutInflater.from(parent.context).inflate(R.layout.city_item_view, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.itemView.apply {
            tvCityName.text = cityList[position].name

            btnDelete.setOnClickListener {
                viewModel.delete(cityList[position])
            }

         /*   btnMoreInformation.setOnClickListener {
                val action = DisplayFragmentDirections.actionDisplayFragmentToDetailFragment(
                    cityName = cityList[position].name
                )
                findNavController().navigate(action)
            }*/

            weatherModel.weather.observe(owner, Observer { observe ->
                observe?.let { weatherModel ->
                    weatherModel.list.first().main.temp.apply {
                        tvWeather.text = round(this).toInt().toString()

                    }

                    imgWeatherIcon.setImageDrawable(
                        when (weatherModel.list[0].weather[0].icon) {
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
                    val tempMin = round(weatherModel.findMin()!!).toInt().toString().plus("° / ")
                    val tempMax = round(weatherModel.findMax()!!).toInt().toString().plus("°")
                    tvMinMaxDegree.text = tempMin.plus(tempMax)

                    val currentDec = weatherModel.list[0].main
                    tvWind.text = weatherModel.list[0].wind.speed.toString().plus(" km/h")
                    tvHumidity.text = currentDec.humidity.toString()
                    tvPressure.text = currentDec.pressure.toString().plus(" hPa")
                    tvRealFeel.text = currentDec.feels_like.toString().plus(" °")

                    weatherModel.list.first().weather.first().description.apply {
                        tvDescription.text = replace(first(), first().toUpperCase())
                    }


                    progressBarDisplayFragment.visibility = View.INVISIBLE
                }
            })
        }
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun WeatherModel.findMin() =
        this.list.subList(0, 8).minBy { it.main.temp_min }?.main?.temp_min

    private fun WeatherModel.findMax() =
        this.list.subList(0, 8).maxBy { it.main.temp_min }?.main?.temp_max
}