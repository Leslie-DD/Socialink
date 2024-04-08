package com.leslie.socialink.inject

import com.leslie.socialink.me.datasource.UserInfoLocalDataSource
import com.leslie.socialink.me.datasource.UserInfoRemoteDataSource
import com.leslie.socialink.me.repository.UserInfoRepository
import com.leslie.socialink.team.datasource.TeamsLocalDatasource
import com.leslie.socialink.team.datasource.TeamsRemoteDatasource
import com.leslie.socialink.team.repository.TeamsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModuleProvider {

    @Singleton
    @Provides
    fun provideUserInfoRepository(
        localDataSource: UserInfoLocalDataSource,
        remoteDataSource: UserInfoRemoteDataSource
    ): UserInfoRepository = UserInfoRepository(
        localDataSource,
        remoteDataSource
    )

    @Singleton
    @Provides
    fun provideTeamsRepository(
        localDataSource: TeamsLocalDatasource,
        remoteDataSource: TeamsRemoteDatasource
    ): TeamsRepository = TeamsRepository(
        localDataSource,
        remoteDataSource
    )
}