package com.leslie.socialink.team.datasource

import com.leslie.socialink.network.Constants
import com.leslie.socialink.network.RetrofitClient
import com.leslie.socialink.network.entity.TeamBean
import javax.inject.Inject

class TeamsRemoteDatasource @Inject constructor(): ITeamsDatasource {

    override suspend fun fetchTeamsData(teamsType: ITeamsDatasource.TeamsType, pn: String, ps: String): List<TeamBean> {
        RetrofitClient.homeService.teams(
            type = teamsType.value.toString(),
            pn = pn,
            ps = Constants.DEFAULT_PS.toString()
        ).data?.let {
            it.data.forEach { teamBean -> teamBean.itemType = 1 }
            return it.data
        }
        return emptyList()
    }

}