package com.leslie.socialink.team.datasource

import com.leslie.socialink.network.entity.TeamBean
import javax.inject.Inject

class TeamsLocalDatasource @Inject constructor() : ITeamsDatasource {

    private val teamsMap: MutableMap<ITeamsDatasource.TeamsType, MutableList<TeamBean>> = mutableMapOf()

    override suspend fun fetchTeamsData(teamsType: ITeamsDatasource.TeamsType, pn: String, ps: String): List<TeamBean> {
        return teamsMap[teamsType] ?: emptyList()
    }

    override fun cacheAndReplaceTeamsData(
        teamsType: ITeamsDatasource.TeamsType,
        teamsData: MutableList<TeamBean>
    ) {
        teamsMap[teamsType] = teamsData
    }

    override fun cacheAndAppendTeamsData(teamsType: ITeamsDatasource.TeamsType, teamsData: MutableList<TeamBean>) {
        teamsMap[teamsType]?.addAll(teamsData)
    }
}