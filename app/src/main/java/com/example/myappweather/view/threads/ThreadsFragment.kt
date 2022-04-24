package com.example.myappweather.view.threads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myappweather.databinding.FragmentThreadsBinding
import java.lang.Thread.sleep


class ThreadsFragment : Fragment() {

    private var _binding: FragmentThreadsBinding? = null
    private val binding: FragmentThreadsBinding
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
        _binding = FragmentThreadsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            button.setOnClickListener {
                Thread {
                    val time = editText.text.toString().toLong()
                    sleep(time * 1000L)
                    requireActivity().runOnUiThread { textView.text = "Поработали $time сек." }
                }.start()
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ThreadsFragment()

    }
}