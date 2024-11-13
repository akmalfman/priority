package com.example.priority.view

import LeaderboardFragment
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.priority.R
import com.example.priority.data.ResultState
import com.example.priority.databinding.FragmentCameraBinding
import com.example.priority.utils.getImageUri
import com.example.priority.utils.reduceFileImage
import com.example.priority.utils.uriToFile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat

class CameraFragment : Fragment() {

    private val viewModel by viewModels<CameraViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var binding: FragmentCameraBinding
    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")

            val uid = getCurrentUserId()
            val distance = arguments?.getDouble("distance") ?: 0.0
            val database = FirebaseDatabase
                .getInstance("https://priority-2e229-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .reference

            val dataMap = mapOf(
                "imageUrl" to uri.toString(),
                "points" to distance
            )

            if (uid != null) {
                // Push new data
                database.child("users").child(uid).child("uploads").push().setValue(dataMap)
                    .addOnSuccessListener {
                        // Retrieve current points
                        database.child("users").child(uid).child("points").get().addOnSuccessListener { snapshot ->
                            val currentPoints = snapshot.getValue(Double::class.java) ?: 0.0
                            val newTotalPoints = currentPoints + distance

                            database.child("users").child(uid).child("points").setValue(newTotalPoints)
                                .addOnSuccessListener {
                                    showToast("Data berhasil disimpan dan poin diperbarui")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("CameraFragment", "Gagal memperbarui poin: ${e.message}")
                                    showToast("Gagal memperbarui poin")
                                }
                        }.addOnFailureListener { e ->
                            Log.e("CameraFragment", "Gagal mengambil poin saat ini: ${e.message}")
                            showToast("Gagal mengambil poin saat ini")
                        }

                        showLoading(false)
                        val leaderboardFragment = LeaderboardFragment()

                        // Ganti fragment
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.frame, leaderboardFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    .addOnFailureListener { e ->
                        Log.e("CameraFragment", "Gagal menyimpan data ke database: ${e.message}")
                        showToast("Gagal menyimpan data ke database")
                        showLoading(false)
                    }

            } else {
                showToast("User not logged in.")
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }


    private fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            Toast.makeText(requireContext(), "Gambar tampil", Toast.LENGTH_SHORT).show()
            showImage()
        } else {
            Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
            binding.previewImageView.visibility = View.VISIBLE
            binding.tvTakePicture.visibility = View.GONE
        }
    }
}