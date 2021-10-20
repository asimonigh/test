package com.simonigh.mojo.presentation

import androidx.lifecycle.*
import com.simonigh.mojo.data.Member
import com.simonigh.mojo.data.MembersRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

class MemberListViewModel(
    private val membersRepository: MembersRepository
): ViewModel() {

    val state: LiveData<List<Member>>
        get() = _state
    private val _state = MutableLiveData<List<Member>>()
    
    private val disposeBag = CompositeDisposable()
    
    init {
        loadMembers()
    }
    
    fun addMember(name: String, position: String, platform: String?) {
        val lastIndex = state.value?.lastIndex ?: -1
        membersRepository.addMember(Member(name, position, platform, null, lastIndex + 1), lastIndex + 1)
            .onErrorComplete().doOnComplete {
                loadMembers()
            }.subscribe()
    }
    
    private fun loadMembers() {
        disposeBag.add(
            membersRepository.getMembers().doOnSuccess {list ->
               _state.postValue(  list.sortedBy { it.index })
            }.subscribe()
        )
    }
    
    fun removeMember(name: String) {
        membersRepository.removeMember(name).onErrorComplete().doOnComplete {
            Timber.d("Member removed")
            loadMembers()
        }.subscribe()
    }
    override fun onCleared() {
        super.onCleared()
        disposeBag.clear()
    }
    
    
    fun upMember(member: Member) {
        var index = state.value?.indexOf(member)
        index?.let {
            index--
            membersRepository.addMember(member.copy(index = index),index).onErrorComplete().doOnComplete {
                loadMembers()
            }.subscribe()
        }
       
    }
    
    fun downMember(member: Member) {
        var index = state.value?.indexOf(member)
        index?.let {
            index++
            membersRepository.addMember(member.copy(index = index),index).onErrorComplete().doOnComplete {
                loadMembers()
            }.subscribe()
        }
    }
}

