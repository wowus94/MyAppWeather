package com.example.myappweather

import android.app.Application
import androidx.room.Room
import com.example.myappweather.domain.room.HistoryDao
import com.example.myappweather.domain.room.MyDB

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = null
    }

    companion object {
        private var db: MyDB? = null
        private var appContext: MyApp? = null
        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                if (appContext != null) {
                    db = Room.databaseBuilder(appContext!!, MyDB::class.java, "test")
                        .allowMainThreadQueries()
                        .build()
                } else {
                    throw IllegalStateException("Пустой appContext")
                }
            }
            return db!!.historyDao()
        }
    }
}