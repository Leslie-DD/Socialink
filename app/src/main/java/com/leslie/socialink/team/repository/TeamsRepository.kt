package com.leslie.socialink.team.repository

import com.leslie.socialink.network.entity.TeamBean
import com.leslie.socialink.team.datasource.ITeamsDatasource
import com.leslie.socialink.team.datasource.TeamsLocalDatasource
import com.leslie.socialink.team.datasource.TeamsRemoteDatasource

class TeamsRepository(
    private val localDatasource: TeamsLocalDatasource,
    private val remoteDatasource: TeamsRemoteDatasource
) {

    /**
     * Force Refresh
     *
     * Re-fetch data from remote datasource and cache to local datasource
     */
    suspend fun refreshTeams(teamsType: ITeamsDatasource.TeamsType, pn: String, ps: String): List<TeamBean> {
        val teamsData: List<TeamBean> = remoteDatasource.fetchTeamsData(teamsType, pn, ps)
        if (teamsData.isNotEmpty()) {
            localDatasource.cacheAndReplaceTeamsData(teamsType, teamsData)
        }
        return teamsData
    }

    /**
     * Local Data First (Offline-First)
     *
     * Fetch from local first. Fetch from remote and cache to local if local is empty
     */
    suspend fun fetchTeams(teamsType: ITeamsDatasource.TeamsType, pn: String, ps: String): List<TeamBean> {
        val localTeamsData = localDatasource.fetchTeamsData(teamsType, pn, ps)
        if (localTeamsData.isNotEmpty()) {
            return localTeamsData
        }
        val remoteTeamsData = remoteDatasource.fetchTeamsData(teamsType,,)
        localDatasource.cacheAndReplaceTeamsData(teamsType, remoteTeamsData)
        return remoteTeamsData
    }
}