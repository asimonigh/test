package com.simonigh.mojo.presentation

import androidx.lifecycle.*
import com.simonigh.mojo.data.Member
import com.simonigh.mojo.data.MembersRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

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
        membersRepository.addMember(Member(name, position, platform, null))
            .onErrorComplete().doOnComplete {
                loadMembers()
            }.subscribe()
    }
    
    private fun loadMembers() {
        disposeBag.add(
            membersRepository.getMembers().doOnSuccess {
               _state.postValue(it)
            }.subscribe()
        )
    }
    override fun onCleared() {
        super.onCleared()
        disposeBag.clear()
    }
    
}

data class MembersListState(val members: List<Member>, val error: String? = null)