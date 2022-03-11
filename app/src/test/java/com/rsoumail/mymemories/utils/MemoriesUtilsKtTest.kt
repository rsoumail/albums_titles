package com.rsoumail.mymemories.utils

import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.framework.entities.MemoryModel
import com.rsoumail.mymemories.framework.network.Album
import org.junit.Assert
import org.junit.jupiter.api.Test

internal class MemoriesUtilsKtTest {

    private val albums = listOf(Album(1, "title", "url", "thumbnailUrl"))
    private val memories = listOf(Memory(1, "title", "url", "thumbnailUrl"))



    @Test
    fun mapToMemory() {
        Assert.assertEquals(memories, mapToMemory(albums))
    }
}