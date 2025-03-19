package ca.uqac.stories.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "stories")
data class Story (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val description: String,
    val done: Boolean,
    val priority: Int,
    val date: String,
    val category: String
)
