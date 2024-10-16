package com.example.priority.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.priority.R
import com.example.priority.databinding.FragmentProfileBinding
import com.example.priority.utils.OnSmoothBottomBarItemSelectedListener
import com.example.priority.view.login.SignInActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

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
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()

//        binding.tvRiwayatLaporan.setOnClickListener {
//            val historyReportFragment = HistoryReportFragment()
//
//            // Ganti fragment
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.frame, historyReportFragment)
//                .addToBackStack(null)
//                .commit()
//
//        }
//
//        binding.imgNextHistory.setOnClickListener {
//            val historyFragment = HistoryFragment()
//
//            // Ganti fragment
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.frame, historyFragment)
//                .addToBackStack(null)
//                .commit()
//
//        }
//
//        binding.imgNextEditProfile.setOnClickListener {
//            val editProfileFragment = EditProfileFragment()
//
//            // Ganti fragment
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.frame, editProfileFragment)
//                .addToBackStack(null)
//                .commit()
//
//        }
//
//        binding.imgNextTentangAplikasi.setOnClickListener {
//            val aboutFragment = AboutFragment()
//
//            // Ganti fragment
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.frame, aboutFragment)
//                .addToBackStack(null)
//                .commit()
//
//        }
//
//        binding.imgNextFaq.setOnClickListener {
//            val faqFragment = FaqFragment()
//
//            // Ganti fragment
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.frame, faqFragment)
//                .addToBackStack(null)
//                .commit()
//
//        }
//
//        binding.imgNextBantuan.setOnClickListener {
//            val helpFragment = HelpFragment()
//
//            // Ganti fragment
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.frame, helpFragment)
//                .addToBackStack(null)
//                .commit()
//
//        }

        mAuth = FirebaseAuth.getInstance()

        // Tampilkan nama pengguna atau pesan jika belum login
        updateUserName()
    }

    private fun setClickListeners() {
        with(binding) {
            tvRiwayatLaporan.setOnClickListener { replaceFragment(HistoryReportFragment()) }
            imgNextHistory.setOnClickListener { replaceFragment(HistoryFragment()) }
            EditProfile.setOnClickListener { replaceFragment(EditProfileFragment()) }
//            imgNextEditProfile.setOnClickListener { replaceFragment(EditProfileFragment()) }
            TentangAplikasi.setOnClickListener { replaceFragment(AboutFragment()) }
//            imgNextTentangAplikasi.setOnClickListener { replaceFragment(AboutFragment()) }
            FAQ.setOnClickListener { replaceFragment(FaqFragment()) }
//            imgNextFaq.setOnClickListener { replaceFragment(FaqFragment()) }
            Bantuan.setOnClickListener { replaceFragment(HelpFragment()) }
//            imgNextBantuan.setOnClickListener { replaceFragment(HelpFragment()) }
            btnSignOut.setOnClickListener { signOut() }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun signOut() {
        mAuth.signOut()
        startActivity(Intent(requireContext(), SignInActivity::class.java))
        requireActivity().finish()
        showToast("Berhasil Keluar")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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