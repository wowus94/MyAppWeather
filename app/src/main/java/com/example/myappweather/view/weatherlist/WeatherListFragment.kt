package com.example.myappweather.view.weatherlist

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myappweather.R
import com.example.myappweather.databinding.FragmentWeatherListBinding
import com.example.myappweather.repository.City
import com.example.myappweather.repository.Weather
import com.example.myappweather.utils.KEY_BUNDLE_WEATHER
import com.example.myappweather.view.details.DetailsFragment
import com.example.myappweather.viewmodel.AppState
import com.example.myappweather.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class WeatherListFragment : Fragment(), OnItemListClickListener {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)
            .get(MainViewModel::class.java)
    }

    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }

    private val adapter = WeatherListAdapter(this)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    var isRussia = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val observer = { data: AppState -> renderData(data) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getWeatherRussia()
        setupFabCities()
        setupFabLocation()
        initRecycler()
    }


    private fun initRecycler() { //TODO
        binding.recyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupFabLocation() {
        binding.mainFragmentFABLocation.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            explain()
        } else {
            mRequestPermission()
        }
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.dialog_rationale_title))
            .setMessage(resources.getString(R.string.dialog_rationale_message))
            .setPositiveButton(resources.getString(R.string.dialog_rationale_give_access)) { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(resources.getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    val REQUEST_CODE = 888
    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    explain()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun getAddressByLocation(location: Location) {
        val geocoder = Geocoder(requireContext())
        val timeStump = System.currentTimeMillis()
        Thread {
            val addressText =
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    10000
                )[0].getAddressLine(0)
            requireActivity().runOnUiThread{
                showAddressDialog(addressText, location)
            }
        }.start()
        Log.d("@@@", " прошло ${System.currentTimeMillis() + timeStump}")
    }

    val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("@@@", location.toString())
            getAddressByLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                providerGPS?.let {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        100f,
                        locationListener
                    )
                }

            }
        }
    }

    private fun setupFabCities() {
        with(binding) {
            floatingActionButton.setOnClickListener {
                isRussia = !isRussia
                if (isRussia) {
                    viewModel.getWeatherRussia()
                    floatingActionButton.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_russia
                        )
                    )
                } else {
                    viewModel.getWeatherWorld()
                    floatingActionButton.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_earth)
                    )
                }
            }
        }
        viewModel.getWeatherRussia()
    }

    fun renderData(data: AppState) {
        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                binding.loadingLayout.withAction(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeatherRussia() },
                    Snackbar.LENGTH_LONG
                )
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherList)
            }
        }
    }

    private fun View.withAction(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }


    companion object {

        @JvmStatic
        fun newInstance() = WeatherListFragment()

    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(
                R.id.container,
                DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(
                        KEY_BUNDLE_WEATHER,
                        weather
                    )
                })
            ).addToBackStack("").commit()
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    onItemClick(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }


}