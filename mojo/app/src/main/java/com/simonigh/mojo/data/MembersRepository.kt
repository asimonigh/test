package com.simonigh.mojo.data

import com.simonigh.mojo.data.remote.MemberApi
import com.simonigh.mojo.data.remote.dto.toMember
import io.reactivex.rxjava3.core.Single

class MembersRepository(
    private val memberApi: MemberApi
) {
    
    fun getMembers(): Single<List<Member>> {
        return memberApi.getMembers().map { members ->
            members.map { it.toMember() }
        }
    }
}