package com.simonigh.mojo.data.local

import android.app.Application
import androidx.room.*

@Database(entities = [MemberEntity::class], version = 1)
abstract class CacheDatabase : RoomDatabase() {
    abstract val memberDao: MemberDao
    
    companion object {
        fun build(application: Application): CacheDatabase {
            return Room.databaseBuilder(application, CacheDatabase::class.java, "cache_db").build()
        }
    }
}