package com.simonigh.mojo.data.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface MemberDao {
    
    @Query("SELECT * FROM member")
    fun getMembers(): Single<List<MemberEntity>>
    
    @Insert(onConflict = REPLACE)
    fun addMember(member: MemberEntity): Completable
}