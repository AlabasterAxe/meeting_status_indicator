package co.thkp.meetingstatusindicator.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.thkp.meetingstatusindicator.model.RequestAttempt

@Dao
interface RequestAttemptDao {
    @Query("SELECT * FROM request_attempt")
    fun getAllRequests(): LiveData<List<RequestAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(requestAttempt: RequestAttempt)

    @Query("DELETE FROM request_attempt")
    suspend fun deleteAll()
}