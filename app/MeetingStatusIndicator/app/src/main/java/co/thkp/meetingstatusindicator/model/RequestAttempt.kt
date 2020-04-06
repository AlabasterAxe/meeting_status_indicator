package co.thkp.meetingstatusindicator.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName="request_attempt")
class RequestAttempt (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "datetime") val requestDateTime: Date
)