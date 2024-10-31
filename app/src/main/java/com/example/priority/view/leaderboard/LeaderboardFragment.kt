package com.example.priority.view.leaderboard

import User
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.priority.data.api.ApiConfig
import com.example.priority.databinding.FragmentLeaderboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LeaderboardFragment : Fragment() {

    private lateinit var binding: FragmentLeaderboardBinding
    private lateinit var adapter: LeaderboardAdapter
    private var leaderboardList: List<User> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false)

        setupRecyclerView()
        fetchLeaderboardData() // Fetch data from API

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = LeaderboardAdapter(leaderboardList)
        binding.rvBoard.layoutManager = LinearLayoutManager(context)
        binding.rvBoard.adapter = adapter
    }

    private fun fetchLeaderboardData() {
        // Call the API to fetch leaderboard data
        ApiConfig.apiService.getLeaderboard().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    leaderboardList = response.body() ?: listOf()
                    adapter.updateData(leaderboardList) // Update adapter with new data
                } else {
                    Toast.makeText(context, "Failed to get data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("API Error", "Error fetching leaderboard", t)
                Toast.makeText(context, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}