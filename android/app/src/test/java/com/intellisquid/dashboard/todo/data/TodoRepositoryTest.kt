package com.intellisquid.dashboard.todo.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TodoRepositoryTest {

    private lateinit var dao: FakeTodoDao
    private lateinit var repository: TodoRepository

    @Before
    fun setUp() {
        dao = FakeTodoDao()
        repository = TodoRepository(dao)
    }

    @Test
    fun addTodo_appearsOnDashboardAsActive() = runTest {
        repository.addTodo("Buy milk")

        val dashboard = repository.observeDashboard().first()
        assertEquals(1, dashboard.size)
        assertEquals(TodoStatus.ACTIVE, dashboard.first().status)
    }

    @Test
    fun archive_movesItemFromDashboardToArchiveAndStampsArchivedAt() = runTest {
        repository.addTodo("Buy milk")
        val item = repository.observeDashboard().first().first()

        repository.archive(item)

        assertTrue(repository.observeDashboard().first().isEmpty())
        val archived = repository.observeArchived().first()
        assertEquals(1, archived.size)
        assertEquals(TodoStatus.ARCHIVED, archived.first().status)
        assertTrue(archived.first().archivedAt != null)
    }

    @Test
    fun restoreFromArchive_bringsItemBackToDashboard() = runTest {
        repository.addTodo("Buy milk")
        val item = repository.observeDashboard().first().first()
        repository.archive(item)
        val archivedItem = repository.observeArchived().first().first()

        repository.restoreFromArchive(archivedItem)

        assertEquals(1, repository.observeDashboard().first().size)
        assertTrue(repository.observeArchived().first().isEmpty())
        assertNull(repository.observeDashboard().first().first().archivedAt)
    }

    @Test
    fun purge_permanentlyRemovesAnArchivedItem() = runTest {
        repository.addTodo("Buy milk")
        val item = repository.observeDashboard().first().first()
        repository.archive(item)
        val archivedItem = repository.observeArchived().first().first()

        repository.purge(archivedItem)

        assertTrue(repository.observeArchived().first().isEmpty())
        assertTrue(repository.observeDashboard().first().isEmpty())
    }

    @Test
    fun importItems_assignsFreshIdsInsteadOfOverwritingExistingRows() = runTest {
        repository.addTodo("Existing item")
        val existing = repository.observeDashboard().first().first()

        repository.importItems(
            listOf(existing.copy(id = existing.id, title = "Restored from backup")),
        )

        val dashboard = repository.observeDashboard().first()
        assertEquals(2, dashboard.size)
        assertTrue(dashboard.any { it.title == "Existing item" })
        assertTrue(dashboard.any { it.title == "Restored from backup" })
    }
}
