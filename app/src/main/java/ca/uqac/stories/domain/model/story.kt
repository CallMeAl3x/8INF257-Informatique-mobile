package ca.uqac.stories.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.osmdroid.util.GeoPoint
import java.time.LocalTime

@Entity(tableName = "stories")
data class Story (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val description: String,
    val done: Boolean,
    val priority: Int,
    val date: String,
    val category: String,
    val hour: Int,
    val minute: Int,
    val location: GeoPoint? = null
)
