package com.rsoumail.mymemories.framework.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memory")
data class MemoryModel(
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val url: String,
    @ColumnInfo val thumbnailUrl: String
)