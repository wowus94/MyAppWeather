package com.example.myappweather.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.myappweather.databinding.FragmentDetailsBinding
import com.example.myappweather.repository.OnServerResponse
import com.example.myappweather.repository.Weather
import com.example.myappweather.repository.WeatherLoader
import com.example.myappweather.repository.dto.WeatherDTO
import com.example.myappweather.utils.*
import com.example.myappweather.viewmodel.MainViewModel


class DetailsFragment : Fragment(), OnServerResponse {
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)
            .get(MainViewModel::class.java)
    }

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { intent ->
                intent.getParcelableExtra<WeatherDTO>(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER)?.let {
                    onResponse(it)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    lateinit var currentCityName: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver,
            IntentFilter(KEY_WAVE_SERVICE_BROADCAST)
        )
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            WeatherLoader(this@DetailsFragment).loaderWeather(it.city.lat, it.city.lon)
            requireActivity().startService(Intent(requireContext(), DetailsFragment::class.java).apply {
                putExtra(KEY_BUNDLE_LAT, it.city.lat)
                putExtra(KEY_BUNDLE_LON, it.city.lon)
            })
        }
    }


    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            with(weather) {
                loadingLayout.visibility = View.GONE
                cityName.text = currentCityName
                temperatureValue.text = factDTO.temperature.toString()
                feelsLikeValue.text = factDTO.feelsLike.toString()
                cityCoordinates.text =
                    "${weather.infoDTO.lat} ${weather.infoDTO.lon}"

            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }
}
