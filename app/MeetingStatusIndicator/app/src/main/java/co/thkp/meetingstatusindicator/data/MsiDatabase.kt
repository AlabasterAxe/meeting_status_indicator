package co.thkp.meetingstatusindicator.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import co.thkp.meetingstatusindicator.model.RequestAttempt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(RequestAttempt::class), version = 2, exportSchema = false)
@TypeConverters(Converters::class)
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
                ).addCallback(MsiDatabaseCallback(scope)).addMigrations(MIGRATION_1_2).build()
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
        }
    }
}