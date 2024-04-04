package com.leslie.socialink.me.inject

import com.leslie.socialink.me.datasource.UserInfoLocalDataSource
import com.leslie.socialink.me.datasource.UserInfoRemoteDataSource
import com.leslie.socialink.me.repository.UserInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MeModuleProvider {

    @Singleton
    @Provides
    fun provideStreamRepository(
        localDataSource: UserInfoLocalDataSource,
        remoteDataSource: UserInfoRemoteDataSource
    ): UserInfoRepository = UserInfoRepository(
        localDataSource,
        remoteDataSource
    )
}