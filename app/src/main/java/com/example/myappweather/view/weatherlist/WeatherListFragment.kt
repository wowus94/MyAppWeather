package com.example.myappweather.view.weatherlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myappweather.R
import com.example.myappweather.databinding.FragmentWeatherListBinding
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
        setupFab()
        initRecycler()
    }


    private fun initRecycler() { //TODO
        binding.recyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupFab() {
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
                Snackbar.make(
                    binding.root,
                    "Не получилось ${data.error}", Snackbar.LENGTH_LONG
                )
                    .setAction("Повторить") { viewModel.getWeatherRussia() }
                    .show()
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
}