package co.thkp.meetingstatusindicator

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import co.thkp.meetingstatusindicator.data.MsiDatabase
import co.thkp.meetingstatusindicator.model.RequestAttempt
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

val ENDPOINT = "http://192.168.1.2:5000"

val NOT_USED = true

class UpdateStatus : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val clockInfo = alarmManager.nextAlarmClock
        val preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val endpoint = preferences.getString(ENDPOINT_KEY, ENDPOINT)
        val registeredBusyStatus =
            if(preferences.contains(PREVIOUS_STATUS_KEY))
                preferences.getBoolean(PREVIOUS_STATUS_KEY, NOT_USED)
            else
                null
        val currentBusyStatus = clockInfo != null

        if (registeredBusyStatus == null || registeredBusyStatus != currentBusyStatus) {
            val requestSuffix = if (currentBusyStatus) "on" else "off"
            val queue = Volley.newRequestQueue(context)
            val url = "$endpoint/$requestSuffix?source=nap"
            val stringRequest = StringRequest(Request.Method.GET,
                url,
                Response.Listener<String> { response ->
                    // Display the first 500 characters of the response string.
                    Log.i("meetingstatusindicator", "worked")
                    storeAttempt(url, context, "succeeded")
                    preferences.edit().putBoolean(PREVIOUS_STATUS_KEY, currentBusyStatus).apply()
                },
                Response.ErrorListener {
                    Log.i("meetingstatusindicator", "didn't work")
                    storeAttempt(url, context, "failed")
                })

            queue.add(stringRequest)
        }
    }

    private fun storeAttempt(url: String, context: Context, status: String) {
        val ra =
            RequestAttempt(status = status, url = url, id = null, requestDateTime = Date())
        val scope = CoroutineScope(Dispatchers.IO)
        val requestDao = MsiDatabase.getDatabase(context, scope).requestAttemptDao()
        scope.launch {
            requestDao.insert(ra)
        }
    }
}