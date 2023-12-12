package com.example.priority

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.priority.databinding.ActivityMainBinding
import com.example.priority.databinding.FragmentDashboardBinding
import me.ibrahimsn.lib.SmoothBottomBar

class DashboardFragment : Fragment(){

    private lateinit var binding: FragmentDashboardBinding

    private var listener: OnSmoothBottomBarItemSelectedListener? = null

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

        binding.btnSemua.setOnClickListener {
            val taskFragment = TaskFragment()

            // Ganti fragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, taskFragment)
                .addToBackStack(null)
                .commit()

            // Panggil method di interface untuk memperbarui SmoothBottomBar di MainActivity
            listener?.onItemSelected(1)
        }


    }
}