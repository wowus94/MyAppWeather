package com.example.myappweather.view.threads

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        val myThreads = MyThreads()
        myThreads.start()
        var counter = 0
        with(binding) {
            //val time = editText.text.toString().toLong()
            button1.setOnClickListener {
                Thread {
                    val time = editText.text.toString().toLong()
                    sleep(time * 1000L)
                    //requireActivity().runOnUiThread { textView1.text = "Поработали $time сек." }
                    Handler(Looper.getMainLooper()).post {
                        textView1.text = "Поработали $time сек."
                        createTextView("${Thread.currentThread().name} ${++counter}")
                    }

                }.start()
            }
            //"Вечный поток"
            button2.setOnClickListener {
                myThreads.mHandler?.post {
                    val time = editText.text.toString().toLong()
                    sleep(time * 1000L)
                    Handler(Looper.getMainLooper()).post {
                        textView2.text = "Поработали $time сек."
                        createTextView("${Thread.currentThread().name} ${++counter}")
                    }
                }
            }
        }
    }

    private fun createTextView(name: String) {
        binding.mainContainer.addView(TextView(requireContext()).apply {
            text = name
            textSize = 14f
        })
    }

    class MyThreads : Thread() {
        var mHandler: Handler? = null
        override fun run() {
            Looper.prepare()
            mHandler = Handler(Looper.myLooper()!!)
            Looper.loop()
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() = ThreadsFragment()

    }
}