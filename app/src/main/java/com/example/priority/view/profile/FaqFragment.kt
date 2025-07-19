package com.example.priority.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.priority.databinding.FragmentFaqBinding

class FaqFragment : Fragment() {

    private var _binding: FragmentFaqBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaqBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listener untuk tombol kembali
        binding.imgArrowBack.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        // Menambahkan fungsi klik untuk setiap FAQ
        setupFaqClickListeners()
    }

    private fun setupFaqClickListeners() {
        // Listener untuk FAQ 1
        binding.tvFaqValue1.setOnClickListener {
            toggleVisibility(binding.tvFaqDetail1)
        }

        // Listener untuk FAQ 2
        binding.tvFaqValue2.setOnClickListener {
            toggleVisibility(binding.tvFaqDetail2)
        }

        // Listener untuk FAQ 3
        binding.tvFaqValue3.setOnClickListener {
            toggleVisibility(binding.tvFaqDetail3)
        }

        // Listener untuk FAQ 4
        binding.tvFaqValue4.setOnClickListener {
            toggleVisibility(binding.tvFaqDetail4)
        }

        // Listener untuk FAQ 5
        binding.tvFaqValue5.setOnClickListener {
            toggleVisibility(binding.tvFaqDetail5)
        }
    }

    /**
     * Fungsi helper untuk mengubah visibilitas (tampil/sembunyi) sebuah View.
     * @param view View yang visibilitasnya akan diubah.
     */
    private fun toggleVisibility(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}