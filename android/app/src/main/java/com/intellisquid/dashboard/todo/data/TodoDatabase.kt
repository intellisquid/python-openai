package com.intellisquid.dashboard.todo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TodoItem::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        private const val DATABASE_NAME = "dashboard_todo.db"

        @Volatile
        private var instance: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    DATABASE_NAME,
                ).build().also { instance = it }
            }
    }
}
