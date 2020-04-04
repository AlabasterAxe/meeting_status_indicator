package co.thkp.meetingstatusindicator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.thkp.meetingstatusindicator.model.RequestAttempt

@Database(entities = arrayOf(RequestAttempt::class), version = 1, exportSchema = false)
abstract class MsiDatabase : RoomDatabase() {

    abstract fun requestAttemptDao(): RequestAttemptDao

    companion object {
        @Volatile
        private var INSTANCE: MsiDatabase? = null

        fun getDatabase(context: Context): MsiDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MsiDatabase::class.java,
                    "meeting_status_indicator"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}