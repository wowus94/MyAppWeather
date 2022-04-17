package com.example.myappweather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myappweather.databinding.FragmentDetailsBinding
import com.example.myappweather.repository.OnServerResponse
import com.example.myappweather.repository.Weather
import com.example.myappweather.repository.WeatherLoader
import com.example.myappweather.repository.dto.WeatherDTO
import com.example.myappweather.utils.KEY_BUNDLE_WEATHER
import com.example.myappweather.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*


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
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            WeatherLoader(this@DetailsFragment).loaderWeather(it.city.lat, it.city.lon)
        }
    }

    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            with(weather) { // TODO в правильности такого решения не уверен.
                loadingLayout.visibility = View.GONE
                cityName.text = currentCityName
                temperatureValue.text = factDTO.temperature.toString()
                feelsLikeValue.text = factDTO.feelsLike.toString()
                cityCoordinates.text =
                    "${weather.infoDTO.lat} ${weather.infoDTO.lon}"
                showSnackBar()
            }
        }
    }

    private fun showSnackBar() = //TODO как в методичке не получилось. Так работает.
        Snackbar.make(mainView, "Получилось", Snackbar.LENGTH_LONG).show()


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