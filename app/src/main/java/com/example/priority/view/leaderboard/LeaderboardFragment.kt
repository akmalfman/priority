package com.example.priority.view.leaderboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.priority.R
import com.example.priority.databinding.FragmentHistoryBinding
import com.example.priority.databinding.FragmentLeaderboardBinding

class LeaderboardFragment : Fragment() {

    private lateinit var binding: FragmentLeaderboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }
}