package ca.uqac.stories.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
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

    // Insert or update a story in the database
    @Upsert
    suspend fun upsertStory(story: Story)

    // Delete a story from the database
    @Delete
    suspend fun deleteStory(story: Story)
}
