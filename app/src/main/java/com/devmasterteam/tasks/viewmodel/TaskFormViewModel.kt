package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val taskRepository = TaskRepository(application.applicationContext)

    private val _prioritiesList = MutableLiveData<List<PriorityModel>>()
    val prioritiesList: LiveData<List<PriorityModel>> = _prioritiesList

    fun save(task: TaskModel) {
        taskRepository.create(task, object : ApiListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                val s = ""
            }

            override fun onFailure(message: String) {
                val s = ""
            }
        })
    }

    fun loadPriorities() {
        _prioritiesList.value = priorityRepository.listPriority()
    }

}