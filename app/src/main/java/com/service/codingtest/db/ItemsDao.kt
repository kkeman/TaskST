package com.service.codingtest.db

import androidx.paging.PagingSource
import androidx.room.*
import com.service.codingtest.model.response.DocumentData

@Dao
interface ItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: DocumentData)

    @Delete
    fun delete(items: DocumentData)

    @Update
    suspend fun update(items: DocumentData)

    @Query("SELECT * FROM Items WHERE searchWord = :searchWord")
    fun loadAll(searchWord: String): PagingSource<Int, DocumentData>

    @Query("DELETE FROM Items WHERE searchWord = :searchWord")
    suspend fun deleteBySubreddit(searchWord: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<DocumentData>)

    @Query("SELECT EXISTS(SELECT * FROM Items WHERE searchWord=:searchWord)")
    fun exist(searchWord: String) : Boolean
}
