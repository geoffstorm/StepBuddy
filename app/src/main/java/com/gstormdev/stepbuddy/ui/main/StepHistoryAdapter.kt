package com.gstormdev.stepbuddy.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gstormdev.stepbuddy.databinding.StepListItemBinding
import com.gstormdev.stepbuddy.model.StepHistory
import java.text.SimpleDateFormat
import java.util.Locale

class StepHistoryAdapter: RecyclerView.Adapter<StepHistoryViewHolder>() {
    private var history: List<StepHistory> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepHistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return StepHistoryViewHolder(StepListItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount() = history.size

    override fun onBindViewHolder(holder: StepHistoryViewHolder, position: Int) {
        holder.bind(history[position])
    }

    fun setData(history: List<StepHistory>) {
        val diffResult = DiffUtil.calculateDiff(StepHistoryDiffCallback(history, this.history))
        diffResult.dispatchUpdatesTo(this)
        this.history = history
    }
}

class StepHistoryViewHolder(private val binding: StepListItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(model: StepHistory) {
        binding.tvSteps.text = model.steps.toString()
    }
}

class StepHistoryDiffCallback(private val newData: List<StepHistory>, private val data: List<StepHistory>): DiffUtil.Callback() {
    override fun getOldListSize() = data.size

    override fun getNewListSize() = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Check if the data is from the same date
        val df = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return df.format(data[oldItemPosition].dateTime) == df.format(newData[newItemPosition].dateTime)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Check if the step value is the same
        return data[oldItemPosition].steps == newData[newItemPosition].steps
    }
}