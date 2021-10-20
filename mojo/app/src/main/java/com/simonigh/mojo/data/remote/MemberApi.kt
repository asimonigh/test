package com.simonigh.mojo.data.remote

import com.simonigh.mojo.data.remote.dto.MemberDto
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber

interface MemberApi {
    @GET("mojo/team.json")
    fun getMembers(): Single<List<MemberDto>>
    
    companion object {
        
        const val BASE_URL = "https://ptitchevreuil.github.io/"
        
        fun create(): MemberApi {
            val httpClient = OkHttpClient().newBuilder().build()
            
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build().create(MemberApi::class.java)
            
        }
    }
}