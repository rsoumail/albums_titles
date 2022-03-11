package com.rsoumail.mymemories.framework.db

import androidx.test.core.app.ApplicationProvider
import com.rsoumail.mymemories.di.roomTestModule
import com.rsoumail.mymemories.framework.entities.MemoryModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class MemoryDaoTest: KoinTest {

    private val memoriesDatabase: MemoriesDatabase by inject()
    private val memoryDao: MemoryDao by inject()

    /**
     * Override default Koin configuration to use Room in-memory database
     */
    @Before
    fun before() {
        startKoin{
            androidContext(ApplicationProvider.getApplicationContext())
            modules(roomTestModule)
        }
    }

    @Test
    fun testSave() {

        val entities = arrayListOf<MemoryModel>()
        entities.add(MemoryModel(1, "title", "url", "thumbnailUrl"))

        runBlocking {
            // Save entities
            memoryDao.saveMemories(entities)

            // Request all entities
            val requestedEntities = memoryDao.getAllMemories()

            // compare result
            Assert.assertEquals(entities, requestedEntities)
        }
    }

    /**
     * Close resources
     */
    @After
    fun after() {
        memoriesDatabase.close()
        stopKoin()
    }
}