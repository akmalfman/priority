package com.example.priority.view.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.priority.R
import com.example.priority.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = auth.currentUser
        user?.let {
            // Menampilkan nama pengguna di EditText jika ada
            binding.edtNamaLengkap.setText(it.displayName)
        }

        // Load existing image from Firebase Realtime Database
        loadImageFromDatabase()

        binding.imgArrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.imgItemPhoto.setOnClickListener {
            // Memulai pemilihan gambar dari galeri
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            launcherIntentGallery.launch(intent)
        }

        binding.btnSubmit.setOnClickListener {
            updateProfile()
        }
    }

    private fun loadImageFromDatabase() {
        val user = auth.currentUser ?: return
        val database = Firebase.database("https://priority-2e229-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        database.child("users").child(user.uid).child("profileImageUrl")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profileImageUrl = snapshot.getValue(String::class.java)
                    if (!profileImageUrl.isNullOrEmpty() && File(profileImageUrl).exists()) {
                        // Load image from local path
                        val uri = Uri.fromFile(File(profileImageUrl))
                        binding.imgItemPhoto.setImageURI(uri)
                    } else {
                        // Load dummy image
                        binding.imgItemPhoto.setImageResource(R.drawable.dummy_pp)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("EditProfileFragment", "Failed to load profile image: ${error.message}")
                    // Load dummy image on error
                    binding.imgItemPhoto.setImageResource(R.drawable.dummy_pp)
                }
            })
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data
            binding.imgItemPhoto.setImageURI(selectedImageUri)
        }
    }

    private fun updateProfile() {
        val fullname = binding.edtNamaLengkap.text.toString().trim()
        if (fullname.isEmpty()) {
            Toast.makeText(requireContext(), "Nama Lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(requireContext(), "User tidak terdeteksi. Silakan login ulang.", Toast.LENGTH_SHORT).show()
            return
        }

        // Simpan gambar secara lokal jika ada yang dipilih
        var profileImageUrl: String? = null
        if (selectedImageUri != null) {
            profileImageUrl = saveImageLocally(selectedImageUri!!)
        }

        // Simpan data pengguna ke Firebase Realtime Database
        saveUserDataToFirebase(user.uid, fullname, profileImageUrl)
    }

    private fun saveImageLocally(uri: Uri): String? {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val fileName = "profile_image.png"
        val file = File(requireContext().filesDir, fileName)
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath // Mengembalikan path file yang disimpan secara lokal
    }

    private fun saveUserDataToFirebase(uid: String, fullname: String, profileImageUrl: String?) {
        val database = Firebase.database("https://priority-2e229-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        val updates = mutableMapOf<String, Any>(
            "fullname" to fullname
        )
        profileImageUrl?.let {
            updates["profileImageUrl"] = it // Menyimpan path file lokal ke database
        }

        // Perbarui data di Realtime Database
        database.child("users").child(uid).updateChildren(updates)
            .addOnSuccessListener {
                // Perbarui nama di Firebase Auth
                val profileUpdates = userProfileChangeRequest {
                    displayName = fullname
                }
                auth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Gagal memperbarui nama di Auth", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("EditProfileFragment", "Gagal memperbarui data pengguna: ${e.message}")
                Toast.makeText(requireContext(), "Gagal memperbarui data pengguna", Toast.LENGTH_SHORT).show()
            }
    }
}
