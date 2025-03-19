package ca.uqac.stories.data.source
import androidx.room.Database
import androidx.room.RoomDatabase
import ca.uqac.stories.domain.model.Story

@Database(entities = [Story::class], version = 1)
abstract class StoriesDatabase : RoomDatabase() {
    abstract val dao: StoriesDao
    companion object {
        const val DATABASE_NAME = "stories.db"
    }
}