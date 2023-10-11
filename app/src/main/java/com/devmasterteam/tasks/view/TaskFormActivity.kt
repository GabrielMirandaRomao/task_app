package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskFormActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var list: List<PriorityModel> = mutableListOf()
    private var taskIdentification = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this)[TaskFormViewModel::class.java]
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        viewModel.loadPriorities()

        loadDataFromActivity()

        // Layout
        setContentView(binding.root)
        setListener()
        observe()
    }

    private fun setListener() {
        binding.buttonDate.setOnClickListener {
            handleDate()
        }
        binding.buttonSave.setOnClickListener {
            handleSave()
        }
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            taskIdentification = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.load(taskIdentification)
        }
    }

    private fun getIndex(priorityId: Int): Int {
        var index = 0
        for (l in list) {
            if (l.id == priorityId) {
                break
            }
            index++
        }
        return index
    }

    private fun observe() {
        viewModel.prioritiesList.observe(this) { listPriority ->
            list = listPriority
            val list = mutableListOf<String>()
            for (item in listPriority) {
                list.add(item.description)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerPriority.adapter = adapter
        }
        viewModel.taskSave.observe(this) { task ->
            if (task.showStatus()) {
                if(taskIdentification == 0) {
                    toast("Tarefa criada com sucesso!")
                } else {
                    toast("Tarefa atualizada com sucesso!")
                }
                finish()
            } else {
                toast(task.showMessage())
            }
        }
        viewModel.task.observe(this) { task ->
            binding.editDescription.setText(task.description)
            binding.checkComplete.isChecked = task.complete
            binding.spinnerPriority.setSelection(
                getIndex(task.priorityId)
            )
            val date = SimpleDateFormat("yyyy-MM-dd").parse(task.dueDate)
            binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(date)
        }
        viewModel.taskLoad.observe(this) { load ->
            if (!load.showStatus()) {
                toast(load.showMessage())
                finish()
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dueDate = dateFormat.format(calendar.time)
        binding.buttonDate.text = dueDate
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.id = taskIdentification
            this.description = binding.editDescription.text.toString()
            this.complete = binding.checkComplete.isChecked
            this.dueDate = binding.buttonDate.text.toString()

            val index = binding.spinnerPriority.selectedItemPosition
            this.priorityId = list[index].id
        }

        viewModel.save(task)
    }

    private fun handleDate() {
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, day).show()
    }
}