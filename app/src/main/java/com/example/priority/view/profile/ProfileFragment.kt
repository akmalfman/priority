package com.example.priority.view.profile

import LeaderboardFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.priority.R
import com.example.priority.databinding.FragmentProfileBinding
import com.example.priority.utils.OnSmoothBottomBarItemSelectedListener
import com.example.priority.view.login.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private var listener: OnSmoothBottomBarItemSelectedListener? = null

    private lateinit var mAuth: FirebaseAuth

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSmoothBottomBarItemSelectedListener) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnSmoothBottomBarItemSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()

        mAuth = FirebaseAuth.getInstance()

        updateUserName()
        updateProfileImage()
        updatePoints()
    }

    private fun setClickListeners() {
        with(binding) {
            tvPartisipasi.setOnClickListener { replaceFragment(LeaderboardFragment()) }
            imgNextPartisipasi.setOnClickListener { replaceFragment(LeaderboardFragment()) }
            EditProfile.setOnClickListener { replaceFragment(EditProfileFragment()) }
            TentangAplikasi.setOnClickListener { replaceFragment(AboutFragment()) }
            FAQ.setOnClickListener { replaceFragment(FaqFragment()) }
            Bantuan.setOnClickListener { replaceFragment(HelpFragment()) }
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

    private fun updateProfileImage() {
        val userId = mAuth.currentUser?.uid ?: return
        val database = FirebaseDatabase
            .getInstance("https://priority-2e229-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
        database.child(userId).child("profileImageUrl")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profileImageUrl = snapshot.getValue(String::class.java)
                    if (!profileImageUrl.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(profileImageUrl)
                            .placeholder(R.drawable.dummy_pp)
                            .into(binding.circleImageView2)
                    } else {
                        binding.circleImageView2.setImageResource(R.drawable.dummy_pp)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ProfileFragment", "Failed to fetch profile image: ${error.message}")
                    binding.circleImageView2.setImageResource(R.drawable.dummy_pp)
                }
            })
    }

    private fun updatePoints() {
        val userId = mAuth.currentUser?.uid ?: return
        val database = FirebaseDatabase
            .getInstance("https://priority-2e229-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
        database.child(userId).child("points")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val points = snapshot.getValue(Double::class.java) ?: 0.0
                    val decimalFormat = DecimalFormat("#.###")
                    val formattedPoints = decimalFormat.format(points)
                    binding.tvPointValue.text = formattedPoints
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ProfileFragment", "Failed to fetch points: ${error.message}")
                    binding.tvPointValue.text = "0"
                }
            })
    }
}
