package co.thkp.meetingstatusindicator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import co.thkp.meetingstatusindicator.model.RequestAttempt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(RequestAttempt::class), version = 1, exportSchema = false)
abstract class MsiDatabase : RoomDatabase() {

    abstract fun requestAttemptDao(): RequestAttemptDao

    companion object {
        @Volatile
        private var INSTANCE: MsiDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MsiDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MsiDatabase::class.java,
                    "meeting_status_indicator"
                ).addCallback(MsiDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class MsiDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database -> scope.launch { populateDatabase(
                database.requestAttemptDao()
            ) }}
        }

        suspend fun populateDatabase(requestAttemptDao: RequestAttemptDao) {
            requestAttemptDao.deleteAll()

            val req = RequestAttempt(id = null, url = "http://192.168.1.2:5000/on", status = "succeeded")
            requestAttemptDao.insert(req)
        }
    }
}