package com.simonigh.mojo.di

import com.simonigh.mojo.data.MembersRepository
import com.simonigh.mojo.data.local.CacheDatabase
import com.simonigh.mojo.data.remote.MemberApi
import com.simonigh.mojo.presentation.MemberListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { MemberApi.create() }
    single { MembersRepository(get(), get()) }
    single { CacheDatabase.build(androidApplication()) }
    viewModel { MemberListViewModel(get()) }
}