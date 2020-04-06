package co.thkp.meetingstatusindicator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import co.thkp.meetingstatusindicator.data.MsiDatabase
import co.thkp.meetingstatusindicator.data.RequestAttemptDao
import co.thkp.meetingstatusindicator.model.RequestAttempt

class RequestAttemptViewModel(application: Application) : AndroidViewModel(application) {
    private val requestDao: RequestAttemptDao

    val allRequests: LiveData<List<RequestAttempt>>

    init {
        requestDao = MsiDatabase.getDatabase(application, viewModelScope).requestAttemptDao()
        allRequests = requestDao.getAllRequests()
    }
}