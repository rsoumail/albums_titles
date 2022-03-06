package com.rsoumail.mymemories.utils

import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.framework.entities.MemoryModel
import com.rsoumail.mymemories.framework.network.Album

fun mapToMemory(albums: List<Album>): List<Memory> {
    val memories = arrayListOf<Memory>()
    albums.map {
        memories.add(Memory(it.id, it.title, it.url, it.thumbnailUrl))
    }
    return memories
}

@JvmName("mapToMemory1")
fun mapToMemory(memoriesModel: List<MemoryModel>): List<Memory> {
    val memories = arrayListOf<Memory>()
    memoriesModel.map {
        memories.add(Memory(it.id, it.title, it.url, it.thumbnailUrl))
    }
    return memories
}