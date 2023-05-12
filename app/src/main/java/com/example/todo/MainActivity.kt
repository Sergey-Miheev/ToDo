package com.example.todo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {
    //private lateinit var toolbar: Toolbar
    /*private lateinit var switchOnOff: SwitchCompat
    private lateinit var tvSwitchYes: TextView
    private lateinit var tvSwitchNo: TextView*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS )
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        /*switchOnOff = findViewById(R.id.switchOnOff)
        tvSwitchYes = findViewById(R.id.tvSwitchYes)
        tvSwitchNo = findViewById(R.id.tvSwitchNo)*/
        /*switchOnOff.setOnCheckedChangeListener { _, _ ->

        }*/
        //toolbar = findViewById(R.id.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}