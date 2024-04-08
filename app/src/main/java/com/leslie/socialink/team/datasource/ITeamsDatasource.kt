package com.leslie.socialink.team.datasource

import com.leslie.socialink.network.entity.TeamBean

interface ITeamsDatasource {

    suspend fun fetchTeamsData(
        teamsType: TeamsType = TeamsType.HOT,
        pn: String,
        ps: String
    ): List<TeamBean>

    fun cacheAndReplaceTeamsData(
        teamsType: TeamsType = TeamsType.HOT,
        teamsData: MutableList<TeamBean>
    ) {}

    fun cacheAndAppendTeamsData(
        teamsType: TeamsType = TeamsType.HOT,
        teamsData: MutableList<TeamBean>
    ) {}


    enum class TeamsType constructor(
        val value: Int
    ) {
        HOT(1),
        RECOMMEND(5),
        LATEST(2),
        MINE(3)
    }
}