package ca.uqac.stories.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ca.uqac.stories.domain.model.Story

@Database(
    entities = [Story::class],
    version = 2,  // Version augmentée
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class StoriesDatabase : RoomDatabase() {
    abstract val dao: StoriesDao

    companion object {
        const val DATABASE_NAME = "stories.db"

        @Volatile
        private var INSTANCE: StoriesDatabase? = null

        fun getInstance(context: Context): StoriesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StoriesDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration() // Destruction/reconstruction
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}