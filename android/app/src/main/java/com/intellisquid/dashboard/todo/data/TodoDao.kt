package com.intellisquid.dashboard.todo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_items WHERE status IN ('ACTIVE', 'COMPLETED') ORDER BY status ASC, priority DESC, createdAt DESC")
    fun observeDashboard(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_items WHERE status = 'ARCHIVED' ORDER BY archivedAt DESC")
    fun observeArchived(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_items ORDER BY createdAt ASC")
    suspend fun getAllForBackup(): List<TodoItem>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: TodoItem): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(items: List<TodoItem>): List<Long>

    @Update
    suspend fun update(item: TodoItem)

    @Delete
    suspend fun delete(item: TodoItem)

    @Query("SELECT * FROM todo_items WHERE id = :id")
    suspend fun getById(id: Long): TodoItem?
}
