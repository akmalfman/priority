package com.example.priority

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.priority.databinding.ActivityMainBinding
import androidx.core.view.MenuItemCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import me.ibrahimsn.lib.SmoothBottomBar

class MainActivity : AppCompatActivity(),  OnSmoothBottomBarItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gantiFragment(DashboardFragment())

        binding.bottomBar.setOnItemSelectedListener {
            when(it){
                0 -> gantiFragment(DashboardFragment())
                1 -> gantiFragment(TaskFragment())
                2 -> gantiFragment(CalcFragment())
                3 -> gantiFragment(ProfileFragment())
            }
        }
    }

    private fun gantiFragment(fragment: Fragment) {
        val transaksi = supportFragmentManager.beginTransaction()
        transaksi.replace(R.id.frame, fragment)
        transaksi.commit()
    }

    override fun onItemSelected(index: Int) {
        binding.bottomBar.itemActiveIndex=index
    }

}