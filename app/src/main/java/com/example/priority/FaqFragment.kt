package com.example.priority

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.priority.databinding.FragmentFaqBinding
import com.example.priority.databinding.FragmentHistoryBinding

class FaqFragment : Fragment() {

    private lateinit var binding: FragmentFaqBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFaqBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgArrowBack.setOnClickListener {

            // Ganti fragment
            requireActivity().supportFragmentManager.popBackStack()

        }


    }
}