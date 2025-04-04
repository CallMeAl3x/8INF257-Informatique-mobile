package ca.uqac.stories.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Upsert
import androidx.room.Insert
import ca.uqac.stories.domain.model.Story
import kotlinx.coroutines.flow.Flow

@Dao
interface StoriesDao {
    // Retrieve all stories as a Flow to observe changes in the database
    @Query("SELECT * FROM stories")
    fun getStories(): Flow<List<Story>>

    // Retrieve a single story by its ID
    @Query("SELECT * FROM stories WHERE id = :id")
    suspend fun getStory(id: Int): Story?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: Story)

    // Insert or update a story in the database
    @Upsert
    suspend fun upsertStory(story: Story)

    // Delete a story from the database
    @Delete
    suspend fun deleteStory(story: Story)

    @Query(" SELECT * FROM stories WHERE location IS NOT NULL AND ABS((SUBSTR(location, 1, INSTR(location, ',')-1)) - :currentLat) < :radius AND ABS((SUBSTR(location, INSTR(location, ',')+1)) - :currentLon) < :radius")
    fun getStoriesNearLocation(currentLat: Double, currentLon: Double, radius: Double): List<Story>

    @Query("SELECT * FROM stories WHERE hour = :currentHour AND minute = :currentMinute")
    fun getStoriesByTime(currentHour: Int, currentMinute: Int): List<Story>
}
