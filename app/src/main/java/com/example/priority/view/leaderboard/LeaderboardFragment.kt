import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.priority.data.response.User
import com.example.priority.databinding.FragmentLeaderboardBinding
import com.example.priority.view.leaderboard.LeaderboardAdapter
import com.google.firebase.database.*

class LeaderboardFragment : Fragment() {

    private lateinit var binding: FragmentLeaderboardBinding
    private lateinit var adapter: LeaderboardAdapter
    private var leaderboardList: List<User> = listOf()
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false)

        fetchLeaderboardData() // Fetch data from Firebase
        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = LeaderboardAdapter(leaderboardList)
        binding.rvBoard.layoutManager = LinearLayoutManager(context)
        binding.rvBoard.adapter = adapter
    }

    private fun fetchLeaderboardData() {
        database = FirebaseDatabase
            .getInstance("https://priority-2e229-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tempList = mutableListOf<User>()
                for (userSnapshot in dataSnapshot.children) {
                    val userId = userSnapshot.key ?: continue
                    val name = userSnapshot.child("fullname").getValue(String::class.java) ?: "Unknown"
                    val points = userSnapshot.child("points").getValue(Double::class.java) ?: 0.0
                    val profileImageUrl = userSnapshot.child("profileImageUrl").getValue(String::class.java) ?: ""
                    Log.e("firebaseeeeeeee", "onDataChange: $name", )
                    if (name.isNotBlank() && points >= 0) { // Validasi data
                        val user = User(userId, name, points, profileImageUrl)
                        tempList.add(user)
                    } else {
                        Log.w("Leaderboard", "Invalid data for userId: $userId")
                    }
                }
                leaderboardList = tempList.sortedByDescending { it.points }
                adapter.updateData(leaderboardList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase Error", "Error fetching data: ${databaseError.message}")
                Toast.makeText(context, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
