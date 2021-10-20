package com.simonigh.mojo.data

import com.simonigh.mojo.data.local.*
import com.simonigh.mojo.data.remote.MemberApi
import com.simonigh.mojo.data.remote.dto.toMember
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class MembersRepository(
    private val memberApi: MemberApi,
    private val cacheDatabase: CacheDatabase
) {
    
    fun getMembers(): Single<List<Member>> {
       return cacheDatabase.memberDao.getMembers()
            .subscribeOn(Schedulers.io())
            .flatMap { localList ->
                if(localList.isEmpty()){
                   getMemberRemote().flatMap { remoteList ->
                        remoteList.forEach { member ->
                            addMember(member).subscribeOn(Schedulers.io()).subscribe()
                        }
                      Single.just(remoteList)
                    }
                } else {
                    Single.just(localList.map { it.toMember() })
                }
        }
    }
    private fun getMemberLocale(): Single<List<Member>> {
        return cacheDatabase.memberDao.getMembers().subscribeOn(Schedulers.io()).map { localList ->
            localList.map { it.toMember() }
        }.onErrorReturnItem(listOf())
    }
    private fun getMemberRemote(): Single<List<Member>> {
        return memberApi.getMembers().subscribeOn(Schedulers.io()).map { list ->
            list.map { it.toMember() }
        }.onErrorReturnItem(listOf())
    }
    
    fun addMember(member: Member): Completable {
        return cacheDatabase.memberDao
            .addMember(member.toMemberEntity())
            .subscribeOn(Schedulers.io())
    }
}

fun Member.toMemberEntity(): MemberEntity {
    return MemberEntity(name, position, platform, picUrl)
}
sealed class Result {
    data class Success(val data: List<Member>): Result()
    data class Error(val error: String): Result()
}