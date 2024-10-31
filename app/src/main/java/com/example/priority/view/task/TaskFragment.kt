package com.example.priority.view.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.priority.R
import com.example.priority.databinding.FragmentDashboardBinding
import com.example.priority.databinding.FragmentTaskBinding
import com.google.firebase.auth.FirebaseAuth

class TaskFragment : Fragment() {

    private lateinit var binding: FragmentTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDetail.setOnClickListener {
            val detailFragment = DetailFragment()

            // Ganti fragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, detailFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}