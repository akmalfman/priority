package com.example.priority.view.profile

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.priority.databinding.FragmentHelpBinding


class HelpFragment : Fragment() {

    private lateinit var binding: FragmentHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgArrowBack.setOnClickListener {

            // Ganti fragment
            requireActivity().supportFragmentManager.popBackStack()

        }

        // --- KODE TAMBAHAN UNTUK INTENT ---

        // 1. Listener untuk membuka aplikasi Email
        binding.cardEmail.setOnClickListener {
            val emailTujuan = "akmalfsalman2@gmail.com"
            val emailSubject = "Bantuan Aplikasi Priority"

            // Membuat Intent untuk mengirim email
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // Hanya aplikasi email yang akan menangani ini
                putExtra(Intent.EXTRA_EMAIL, arrayOf(emailTujuan))
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            }

            // Memastikan ada aplikasi email yang terinstall sebelum menjalankan intent
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Tidak ada aplikasi email yang ditemukan.", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Listener untuk membuka WhatsApp
        binding.cardWhatsApp.setOnClickListener {
            // PENTING: Ganti dengan nomor WhatsApp Anda
            // Gunakan format internasional tanpa +, spasi, atau tanda hubung. Contoh: 6281234567890
            val nomorWhatsApp = "6285156760830"
            val url = "https://api.whatsapp.com/send?phone=$nomorWhatsApp"

            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Menangani jika WhatsApp tidak terinstall di perangkat pengguna
                Toast.makeText(requireContext(), "Aplikasi WhatsApp tidak ditemukan.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}