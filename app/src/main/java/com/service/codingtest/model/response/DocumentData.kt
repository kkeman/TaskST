package com.service.codingtest.model.response

import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Items")
data class DocumentData(
        @PrimaryKey
        @SerializedName("image_url")
        val image_url: String,

        var searchWord: String,
)