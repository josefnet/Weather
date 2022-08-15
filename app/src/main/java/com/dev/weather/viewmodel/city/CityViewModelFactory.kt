package com.dev.weather.viewmodel.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.weather.db.CityDatabase


@Suppress("UNCHECKED_CAST")
class CityViewModelFactory(
    private val db: CityDatabase
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CityViewModel(db) as T
    }
}