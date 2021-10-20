package com.simonigh.mojo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.simonigh.mojo.data.Member

@Entity(tableName = "member")
data class MemberEntity(
    @PrimaryKey val name: String,
    val position: String,
    val platform: String?,
    val picUrl:String?,
    val index: Int
)

fun MemberEntity.toMember(): Member {
    return Member(name, position, platform,picUrl, index)
}