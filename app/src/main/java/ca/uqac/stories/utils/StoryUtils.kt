package ca.uqac.stories.utils

import android.util.Log
import ca.uqac.stories.data.source.StoriesDao
import ca.uqac.stories.presentation.StoryVM

// Function to add or update a story in the database
suspend fun addOrUpdateStory(dao: StoriesDao, story: StoryVM) {
    val entity = story.toEntity()
    Log.d("addOrUpdateStory", "Upserting story with ID: ${entity.id}")
    dao.upsertStory(entity)
}


// Function to delete a story from the database
suspend fun deleteStoryFromList(dao: StoriesDao, story: StoryVM) {
    dao.deleteStory(story.toEntity())
}
