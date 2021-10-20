package com.simonigh.mojo.data.remote.dto

import com.simonigh.mojo.data.Member

data class MemberDto(
    val location: String,
    val name: String,
    val pic: String? = null,
    val position: String,
    val platform: String? = null
)

fun MemberDto.toMember(): Member {
    return Member(name, position, platform, pic)
}