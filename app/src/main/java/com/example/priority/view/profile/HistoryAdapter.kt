import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.priority.R
import com.example.priority.databinding.ItemRowHistoryBinding

class HistoryAdapter(private val context: Context, private val imageUrls: List<String>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: ItemRowHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemRowHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val imageUrl = imageUrls[position]

        // Load the image using Glide
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.dummy_pp) // Placeholder image
            .into(holder.binding.imgHistory)
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }
}
