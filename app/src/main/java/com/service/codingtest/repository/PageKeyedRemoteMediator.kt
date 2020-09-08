package com.service.codingtest.repository

import android.util.Log
import android.widget.Toast
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.service.codingtest.db.AppDB
import com.service.codingtest.db.ImageRemoteKeyDao
import com.service.codingtest.db.ItemsDao
import com.service.codingtest.manager.PuzzleApp
import com.service.codingtest.model.response.DocumentData
import com.service.codingtest.model.response.ImageRemoteKeyEntity
import com.service.codingtest.network.Constant.IS_EMPTY_LIST
import com.service.codingtest.network.ImageAPI
import com.service.codingtest.network.MLog
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(
    private val db: AppDB,
    private val imageAPI: ImageAPI,
    private val query: String
) : RemoteMediator<Int, DocumentData>() {
    private val imageDao: ItemsDao = db.imageDao()
    private val remoteKeyDao: ImageRemoteKeyDao = db.remoteKeys()

    private val TAG = PageKeyedRemoteMediator::class.java.name

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DocumentData>
    ): MediatorResult {
        try {

            var page = when (loadType) {
                REFRESH -> 0
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {
                    val remoteKey = db.withTransaction {
                        remoteKeyDao.remoteKeyBySearchWord(query)
                    }

                    if (remoteKey == null || remoteKey.pageCount == null || remoteKey.pageCount == 0) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    MLog.d(TAG, "remoteKey.pageCount:" + remoteKey.pageCount)
                    remoteKey.pageCount
                }
            }

            MLog.d(TAG, "loadType:" + loadType.name + " / page:" + page)

            val data = imageAPI.getAPI(query = query, page = ++page)

            var items = data.documents

            items = items.map {
                it.searchWord = query
                it
            }

            db.withTransaction {
                if (loadType == REFRESH) {
                    imageDao.deleteBySubreddit(query)
                    remoteKeyDao.deleteBySearchWord(query)
                }

                remoteKeyDao.insert(ImageRemoteKeyEntity(query, page))
                imageDao.insertAll(items)
            }

            MLog.d(TAG, "items.isEmpty():" + items.isEmpty())

            return if(items.isEmpty()) MediatorResult.Error(Throwable(IS_EMPTY_LIST))
            else MediatorResult.Success(endOfPaginationReached = false)

        } catch (e: IOException) {
            MLog.w(TAG, e.message)

            if(!imageDao.exist(query))
                Toast.makeText(PuzzleApp.applicationContext(), e.message, Toast.LENGTH_SHORT).show()

            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            MLog.w(TAG, e.message)
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            MLog.w(TAG, e.message)
            return MediatorResult.Error(e)
        }
    }


}



