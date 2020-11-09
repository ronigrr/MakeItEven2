package com.yoyoG.makeiteven2.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yoyoG.makeiteven2.extras.Constants

@Database(
    entities = [RoomUserNote::class],
    version = 1
)
@TypeConverters(Converters::class)

abstract class RoomNoteDatabase : RoomDatabase() {

    abstract fun roomNoteDao(): NoteDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: RoomNoteDatabase? = null

        fun getInstance(context: Context): RoomNoteDatabase {
            return instance
                ?: synchronized(this) {
                    instance
                        ?: buildDatabase(
                            context
                        )
                            .also { instance = it }
                }
        }

        private fun buildDatabase(context: Context): RoomNoteDatabase {
            return Room.databaseBuilder(
                context, RoomNoteDatabase::class.java,
                Constants.ROOM_DATA_BASE_NAME
            )
                .build()
        }
    }
}