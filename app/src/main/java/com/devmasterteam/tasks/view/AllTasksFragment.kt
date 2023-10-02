package com.devmasterteam.tasks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmasterteam.tasks.databinding.FragmentAllTasksBinding
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.view.adapter.TaskAdapter
import com.devmasterteam.tasks.viewmodel.TaskListViewModel

class AllTasksFragment : Fragment() {

    private lateinit var viewModel: TaskListViewModel
    private lateinit var binding: FragmentAllTasksBinding
    private val adapter = TaskAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {
        viewModel = ViewModelProvider(this)[TaskListViewModel::class.java]
        binding = FragmentAllTasksBinding.inflate(inflater, container, false)

        setUpRecycler()
        observe()

        val listener = object : TaskListener {
            override fun onListClick(id: Int) {
                TODO("Not yet implemented")
            }

            override fun onDeleteClick(id: Int) {
                viewModel.delete(id)
            }

            override fun onCompleteClick(id: Int) {
                TODO("Not yet implemented")
            }

            override fun onUndoClick(id: Int) {
                TODO("Not yet implemented")
            }

        }

        adapter.attachListener(listener)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.list()
    }

    private fun setUpRecycler(){
        binding.recyclerAllTasks.layoutManager = LinearLayoutManager(context)
        binding.recyclerAllTasks.adapter = adapter
    }

    private fun observe() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter.updateTasks(tasks)
        }
        viewModel.delete.observe(viewLifecycleOwner) {
            if(!it.showStatus()){
                Toast.makeText(context, it.showMessage(), Toast.LENGTH_LONG).show()
            }
        }
    }
}