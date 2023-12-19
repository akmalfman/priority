package com.example.priority.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.priority.R
import com.example.priority.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRiwayatLaporan.setOnClickListener {
            val historyReportFragment = HistoryReportFragment()

            // Ganti fragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, historyReportFragment)
                .addToBackStack(null)
                .commit()

        }

        binding.imgNextHistory.setOnClickListener {
            val historyFragment = HistoryFragment()

            // Ganti fragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, historyFragment)
                .addToBackStack(null)
                .commit()

        }

        binding.imgNextEditProfile.setOnClickListener {
            val editProfileFragment = EditProfileFragment()

            // Ganti fragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, editProfileFragment)
                .addToBackStack(null)
                .commit()

        }

        binding.imgNextTentangAplikasi.setOnClickListener {
            val aboutFragment = AboutFragment()

            // Ganti fragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, aboutFragment)
                .addToBackStack(null)
                .commit()

        }

        binding.imgNextFaq.setOnClickListener {
            val faqFragment = FaqFragment()

            // Ganti fragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, faqFragment)
                .addToBackStack(null)
                .commit()

        }

        binding.imgNextBantuan.setOnClickListener {
            val helpFragment = HelpFragment()

            // Ganti fragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, helpFragment)
                .addToBackStack(null)
                .commit()

        }


    }

}