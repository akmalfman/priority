package com.example.priority.view.main

import LeaderboardFragment
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.priority.R
import com.example.priority.databinding.FragmentDashboardBinding
import com.example.priority.utils.OnSmoothBottomBarItemSelectedListener
import com.example.priority.view.task.DetailFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val bundle = arguments
        val aqiString = bundle?.getString("textAqiu")
        val time = bundle?.getString("textTime")
        val city = bundle?.getString("textCity") ?: ""
        val state = bundle?.getString("textState") ?: ""
        val aqi = aqiString?.toIntOrNull() ?: 0

        binding.tvAqi.text = aqi.toString()
        binding.tvCity.text = city
        binding.tvState.text = state

        if (time != null) {
            val formattedTime = formatTime(time)
            binding.tvTime.text = formattedTime
        } else {
            binding.tvTime.text = "N/A"
        }

        updateAQI(aqi)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        binding.btnRekomendasi.setOnClickListener {
            val taskFragment = DetailFragment()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, taskFragment)
                .addToBackStack(null)
                .commit()

            listener?.onItemSelected(1)
        }

        binding.civLeaderboard.setOnClickListener {
            val leaderboardFragment = LeaderboardFragment()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, leaderboardFragment)
                .addToBackStack(null)
                .commit()
        }

        updateUserName()
        updatePoint()
        updateProfileImage()
    }

    private fun formatTime(isoTime: String): String {
        return try {
            val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
            val date = isoFormatter.parse(isoTime)

            val outputFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            outputFormatter.format(date)
        } catch (e: Exception) {
            Log.e("DashboardFragment", "Error parsing date: ${e.message}")
            "Invalid date"
        }
    }

    private fun updateUserName() {
        mAuth.currentUser?.let { currentUser ->
            val displayName = currentUser.displayName
            binding.tvName.text = displayName ?: currentUser.email ?: "Pengguna belum login"
        } ?: run {
            binding.tvName.text = "Pengguna belum login"
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun updateAQI(aqi: Int) {
        binding.progressAqi.progress = aqi

        val color = when {
            aqi <= 50 -> R.color.green
            aqi <= 100 -> R.color.yellow
            aqi <= 150 -> R.color.orange
            aqi <= 200 -> R.color.red
            aqi <= 300 -> R.color.purple
            else -> R.color.maroon
        }

        binding.progressAqi.progressDrawable.setColorFilter(
            resources.getColor(color, null),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    private fun updatePoint() {
        val database = FirebaseDatabase
            .getInstance("https://priority-2e229-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
        val userId = mAuth.currentUser?.uid

        if (userId != null) {
            database.child(userId).child("points").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val points = snapshot.getValue(Double::class.java) ?: 0.0
                    val decimalFormat = DecimalFormat("#.###")
                    val formattedDistance = decimalFormat.format(points)
                    binding.tvPointValue.text = formattedDistance
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DashboardFragment", "Error fetching points: ${error.message}")
                    binding.tvPointValue.text = "0"
                }
            })
        } else {
            binding.tvPointValue.text = "0"
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
                        // Load image from the provided URL or path using Glide
                        Glide.with(requireContext())
                            .load(profileImageUrl)
                            .placeholder(R.drawable.dummy_pp) // Placeholder if loading fails
                            .into(binding.imgProfile)
                    } else {
                        // Load the default dummy image if URL is null or empty
                        binding.imgProfile.setImageResource(R.drawable.dummy_pp)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DashboardFragment", "Failed to fetch profile image: ${error.message}")
                    binding.imgProfile.setImageResource(R.drawable.dummy_pp)
                }
            })
    }
}
