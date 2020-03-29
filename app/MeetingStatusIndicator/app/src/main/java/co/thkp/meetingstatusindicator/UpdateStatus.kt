package co.thkp.meetingstatusindicator

import android.app.AlarmManager
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

val ENDPOINT = "http://192.168.1.2:5000"

class UpdateStatus : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val clockInfo = alarmManager.nextAlarmClock
        var requestSuffix = "/off";
        if (clockInfo != null) {
            requestSuffix = "/on"
        }

        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(Request.Method.GET,
            "$ENDPOINT$requestSuffix",
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Log.i("meetingstatusindicator", "worked");
            },
            Response.ErrorListener { Log.i("meetingstatusindicator", "didn't work"); })

        queue.add(stringRequest);
    }
}