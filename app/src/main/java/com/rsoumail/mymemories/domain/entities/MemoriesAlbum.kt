package com.rsoumail.mymemories.domain.entities

data class MemoriesAlbum(
    val albumId: Int,
    val memories: List<Memory>
)