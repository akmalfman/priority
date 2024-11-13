package com.example.priority.view.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.priority.data.response.Upload
import com.example.priority.databinding.FragmentDetailHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailHistoryFragment : Fragment() {

    private lateinit var binding: FragmentDetailHistoryBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        binding.imgArrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()

        }

        loadUserHistory()
    }

    private fun loadUserHistory() {
        val userId = mAuth.currentUser?.uid ?: return
        val database = FirebaseDatabase
            .getInstance("https://priority-2e229-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
        database.child(userId).child("uploads")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uploads = mutableListOf<Upload>()
                    for (childSnapshot in snapshot.children) {
                        val upload = childSnapshot.getValue(Upload::class.java)
                        if (upload != null) {
                            uploads.add(upload)
                        } else {
                            Log.e("ProfileFragment", "Invalid upload data type in uploads")
                        }
                    }
                    // Set up the RecyclerView with horizontal layout
                    binding.rvHistories.layoutManager = LinearLayoutManager(requireContext())
                    val adapter = HistoryReportAdapter(requireContext(), uploads)
                    binding.rvHistories.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ProfileFragment", "Failed to fetch user history: ${error.message}")
                }
            })
    }

}