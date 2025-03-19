package ca.uqac.stories.utils

import android.util.Log
import ca.uqac.stories.data.source.StoriesDao
import kotlinx.coroutines.flow.first

suspend fun logDatabaseState(dao: StoriesDao) {
    try {
        val stories = dao.getStories().first()
        Log.d("DatabaseState", "Current stories in the database:")
        stories.forEach { story ->
            Log.d("DatabaseState", "Story(id=${story.id}, title='${story.title}', description='${story.description}', done=${story.done}, priority=${story.priority})")
        }
    } catch (e: Exception) {
        Log.e("DatabaseState", "Error logging database state", e)
    }
}
