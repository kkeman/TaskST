package com.service.codingtest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.service.codingtest.model.response.FavoriteEntity
import com.service.codingtest.model.response.ImageRemoteKeyEntity
import com.service.codingtest.model.response.DocumentData

@Database(
    entities = [DocumentData::class, ImageRemoteKeyEntity::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {
    abstract fun imageDao(): ItemsDao
    abstract fun remoteKeys(): ImageRemoteKeyDao

    companion object {

        private var appDB: AppDB? = null

        fun getInstance(context: Context): AppDB {
            if (appDB == null) {
                synchronized(AppDB::class.java) {
                    appDB = Room.databaseBuilder(context, AppDB::class.java, "AppDB.db")
                        .addCallback(CALLBACK)
                        .allowMainThreadQueries().build()
                }
            }
            return appDB!!
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
//                db.beginTransaction()
//                db.execSQL(
//                    "CREATE TRIGGER insert_favorite " +
//                            "AFTER INSERT ON Items " +
//                            "BEGIN " +
//                            "UPDATE Items SET isFavorite = 1 WHERE (EXISTS(SELECT * FROM Favorite WHERE name = NEW.login));" +
//                            " END;"
//                )
////                EXISTS(SELECT * FROM Favorite WHERE id=:id)
//                db.setTransactionSuccessful()
//                db.endTransaction()
            }
        }
    }
}