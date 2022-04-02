package com.example.myappweather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myappweather.R
import com.example.myappweather.view.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit()
        }
    }
}