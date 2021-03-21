package com.mapsplayground.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.mapsplayground.R
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(applicationContext) {
            Configuration.getInstance().load(
                this,
                PreferenceManager.getDefaultSharedPreferences(this)
            )
        }
        setContentView(R.layout.activity_main)
    }
}
