package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val database = TaskDatabase.getDatabase(context).priorityDAO()

    fun list(listener: ApiListener<List<PriorityModel>>) {
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        executeCall(remote.list(), listener)
    }

    fun listPriority() = database.list()

    fun getDescription(id: Int): String{
        return if(PriorityRepository.getDescription(id) == "") {
            val description = database.getDescripition(id)
            setDescription(id, description)
            description
        } else {
            PriorityRepository.getDescription(id)
        }
    }

    fun save(list: List<PriorityModel>) {
        database.clear()
        database.save(list)
    }

    companion object {
        private val cache = mutableMapOf<Int, String>()

        fun getDescription(id: Int): String= cache[id] ?: ""

        fun setDescription(id: Int, description: String) {
            cache[id] =  description
        }
    }
}