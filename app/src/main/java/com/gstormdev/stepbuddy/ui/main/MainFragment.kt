package com.gstormdev.stepbuddy.ui.main

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gstormdev.stepbuddy.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        private const val RC_FITNESS_STEPS = 100
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding

    private val historyAdapter = StepHistoryAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        viewModel.stepHistory.observe(viewLifecycleOwner, Observer {
            historyAdapter.setData(it)
        })

        viewModel.checkFitPermissions(this, RC_FITNESS_STEPS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_FITNESS_STEPS && resultCode == Activity.RESULT_OK) {
            viewModel.retrieveFitData()
        }
    }
}