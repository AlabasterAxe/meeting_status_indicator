package co.thkp.meetingstatusindicator

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

val MINUTE_IN_MILLIS = 1000 * 60L;

class AutoStart : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val updateStatusIntent: Intent = Intent(context, UpdateStatus::class.java)
        val alarmIntent =
            PendingIntent.getBroadcast(context, 0, updateStatusIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmMgr =
            context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), MINUTE_IN_MILLIS, alarmIntent)
    }
}