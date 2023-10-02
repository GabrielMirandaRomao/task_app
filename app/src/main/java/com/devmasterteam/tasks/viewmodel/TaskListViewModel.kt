package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks : LiveData<List<TaskModel>> = _tasks

    private val _delete = MutableLiveData<ValidationModel>()
    val delete : LiveData<ValidationModel> = _delete

    private val _status = MutableLiveData<ValidationModel>()
    val status : LiveData<ValidationModel> = _status

    fun list() {
        taskRepository.list(object : ApiListener<List<TaskModel>> {
            override fun onSuccess(result: List<TaskModel>) {

                result.forEach {
                    it.priorityDescription = priorityRepository.getDescription(it.priorityId)
                }

                _tasks.value = result
            }

            override fun onFailure(message: String) {
            }
        })
    }

    fun delete(id: Int) {
        taskRepository.delete(id, object : ApiListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                list()
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
            }
        })
    }

    fun status(id: Int, isComplete: Boolean) {

        val listener = object : ApiListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                list()
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
            }
        }

        if(isComplete){
            taskRepository.complete(id, listener)
        } else {
            taskRepository.undo(id, listener)
        }
    }
}