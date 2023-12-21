package com.example.priority.view.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val bundle = arguments
        val aqi = bundle!!.getString("textAqiu")
        val city = bundle.getString("textCity")
        Log.d("city", "onCreateView: $city ")
        val state = bundle.getString("textState")

        // Sets the derived data (type String) in the TextView
        binding.tvAqi.text = aqi
        binding.tvCity.text = city
        binding.tvState.text = state

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
}