package com.example.priority.view.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.priority.R
import com.example.priority.data.response.Upload
import com.example.priority.databinding.ItemRowHistoryReportBinding
import java.text.DecimalFormat

class HistoryReportAdapter(private val context: Context, private val uploads: List<Upload>) :
    RecyclerView.Adapter<HistoryReportAdapter.HistoryReportViewHolder>() {

    inner class HistoryReportViewHolder(val binding: ItemRowHistoryReportBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryReportViewHolder {
        val binding = ItemRowHistoryReportBinding.inflate(LayoutInflater.from(context), parent, false)
        return HistoryReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryReportViewHolder, position: Int) {
        val upload = uploads[position]

        // Load image using Glide
        Glide.with(context)
            .load(upload.imageUrl)
            .placeholder(R.drawable.dummy_pp) // Placeholder if image is not available
            .into(holder.binding.imgHistory)

        // Set text values
        val decimalFormat = DecimalFormat("#.###")
        val formattedPoints = decimalFormat.format(upload.points)
        holder.binding.tvPointValue.text = formattedPoints.toString()
        holder.binding.tvDate.text = upload.date
        holder.binding.tvClock.text = upload.clock
    }

    override fun getItemCount(): Int {
        return uploads.size
    }
}
