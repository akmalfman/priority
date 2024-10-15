package com.example.priority.view.main

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.priority.view.CameraFragment
import com.example.priority.utils.OnSmoothBottomBarItemSelectedListener
import com.example.priority.R
import com.example.priority.data.ResultState
import com.example.priority.data.response.AqiResponse
import com.example.priority.databinding.FragmentDashboardBinding
import com.example.priority.view.task.TaskFragment
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : Fragment(){

    private lateinit var binding: FragmentDashboardBinding

    private var listener: OnSmoothBottomBarItemSelectedListener? = null

    private lateinit var mAuth: FirebaseAuth

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Pastikan activity mengimplementasikan interface
        if (context is OnSmoothBottomBarItemSelectedListener) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnSmoothBottomBarItemSelectedListener")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val bundle = arguments
        val aqiString = bundle?.getString("textAqiu")
        val city = bundle?.getString("textCity") ?: ""
        val state = bundle?.getString("textState") ?: ""
        val aqi = aqiString?.toIntOrNull() ?: 0

        binding.tvAqi.text = aqi.toString()
        binding.tvCity.text = city
        binding.tvState.text = state

        updateAQI(aqi)

        return binding.root

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        binding.btnRekomendasi.setOnClickListener {
            val taskFragment = TaskFragment()

            // Ganti fragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, taskFragment)
                .addToBackStack(null)
                .commit()

            // Panggil method di interface untuk memperbarui SmoothBottomBar di MainActivity
            listener?.onItemSelected(1)

            // Tampilkan nama pengguna atau pesan jika belum login
            updateUserName()

        }
    }
    private fun updateUserName() {
        mAuth.currentUser?.let { currentUser ->
            val displayName = currentUser.displayName

            binding.tvName.text = displayName ?: currentUser.email ?: "Pengguna belum login"
        } ?: run {
            binding.tvName.text = "Pengguna belum login"
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun updateAQI(aqi: Int){
        binding.progressAqi.progress = aqi

        // Change color based on AQI value
        val color = when {
            aqi <= 50 -> R.color.green  // Good (0-50)
            aqi <= 100 -> R.color.yellow // Moderate (51-100)
            aqi <= 150 -> R.color.orange // Unhealthy for Sensitive Groups (101-150)
            aqi <= 200 -> R.color.red    // Unhealthy (151-200)
            aqi <= 300 -> R.color.purple // Very Unhealthy (201-300)
            else -> R.color.maroon        // Hazardous (301+)
        }

        // Change ProgressBar color
        binding.progressAqi.progressDrawable.setColorFilter(
            resources.getColor(color, null),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }
}