package com.simonigh.mojo.data.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.DELETE

@Dao
interface MemberDao {
    
    @Query("SELECT * FROM member")
    fun getMembers(): Single<List<MemberEntity>>
    
    @Insert(onConflict = REPLACE)
    fun addMember(member: MemberEntity): Completable
    
    @Query("DELETE FROM member WHERE name = :name" )
    fun removeMember(name: String): Completable
}