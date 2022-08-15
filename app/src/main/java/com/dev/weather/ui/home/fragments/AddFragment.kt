package com.dev.weather.ui.home.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.dev.weather.R
import com.dev.weather.db.City
import com.dev.weather.db.CityDatabase
import com.dev.weather.util.CheckLocationPermission
import com.dev.weather.util.Constants.LOCATION_REQUEST_CODE
import com.dev.weather.viewmodel.city.CityViewModel
import com.dev.weather.viewmodel.city.CityViewModelFactory
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class AddFragment : Fragment(R.layout.fragment_add), EasyPermissions.PermissionCallbacks {

    private lateinit var dbViewModel: CityViewModel

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()

        btnAdd.setOnClickListener {
            addCityToDb()
        }

        switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getCurrentLocation()
            } else {
                Toast.makeText(this.requireContext(), "Uncheck", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentLocation() {
        //Check Permissions
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //Permissions are Denied
            requestPermission()
        } else {
            //Permissions are Granted
            val locationManager =
                requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            location?.let {
                val geoCoder = Geocoder(this.requireContext(), Locale.getDefault())
                val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                val cityName = addresses[0].adminArea

                edtCityName.setText(cityName)
            }

            if (location == null) {
                Toast.makeText(this.requireContext(), "Your location could not be reached", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun requestPermission() {
        if (CheckLocationPermission.hasLocationPermission(requireContext())) {
            getCurrentLocation()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You must grant location access.",
                LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    private fun setupViewModel() {
        val db = CityDatabase.getInstance(this.requireContext())
        val factory = CityViewModelFactory(db)
        dbViewModel = ViewModelProviders.of(this, factory).get(CityViewModel::class.java)
    }

    private fun addCityToDb() {
        val cityName = edtCityName.text.toString()
        if (cityName.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                dbViewModel.upsert(City(name = cityName))
            }

            findNavController().navigate(R.id.action_addFragment_to_displayFragment)
        } else {
            Toast.makeText(this.requireContext(), "You must enter the city name", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else requestPermission()

        switchLocation.isChecked = false
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        switchLocation.isChecked = true
        getCurrentLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
