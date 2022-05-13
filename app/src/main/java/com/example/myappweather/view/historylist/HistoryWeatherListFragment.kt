package com.example.myappweather.view.historylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myappweather.databinding.FragmentHistoryWeatherListBinding
import com.example.myappweather.viewmodel.AppState
import com.example.myappweather.viewmodel.HistoryViewModel
import com.google.android.material.snackbar.Snackbar


class HistoryWeatherListFragment : Fragment() {


    private var _binding: FragmentHistoryWeatherListBinding? = null
    private val binding: FragmentHistoryWeatherListBinding
        get() {
            return _binding!!
        }

    private val adapter = HistoryWeatherListAdapter()


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    var isRussia = true
    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.also {// TODO HW вынесты в initRecycler()
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        val observer = { data: AppState -> renderData(data) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getAll()
    }

    fun renderData(data: AppState) {
        when (data) {
            is AppState.Error -> {
                Snackbar.make(binding.root, "Не получилось ${data.error}", Snackbar.LENGTH_LONG)
                    .show()
            }
            is AppState.Loading -> {

            }
            is AppState.Success -> {

                adapter.setData(data.weatherList)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryWeatherListFragment()

    }
}