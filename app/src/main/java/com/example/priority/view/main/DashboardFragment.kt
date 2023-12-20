package com.example.priority.view.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.priority.view.CameraFragment
import com.example.priority.utils.OnSmoothBottomBarItemSelectedListener
import com.example.priority.R
import com.example.priority.databinding.FragmentDashboardBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        binding.btnRekomendasi.setOnClickListener {
            val cameraFragment = CameraFragment()

            // Ganti fragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, cameraFragment)
                .addToBackStack(null)
                .commit()

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