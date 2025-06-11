package com.duczxje.screenstreaming.presentation.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.duczxje.screenstreaming.R
import com.duczxje.screenstreaming.flutter.fragment.FlutterContainerFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupFlutterFragment()
    }

    private fun setupFlutterFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flutter_container, FlutterContainerFragment.newInstance(intent.extras))
            .commit()
    }
}