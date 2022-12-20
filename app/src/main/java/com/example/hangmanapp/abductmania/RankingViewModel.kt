package com.example.hangmanapp.abductmania

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RankingViewModel : ViewModel()
{
    public val rankingUsersData = MutableLiveData<ArrayList<RankingUserData>>()

    init
    {
        rankingUsersData.value = ArrayList<RankingUserData>()
    }


    data class RankingUserData(public val username : String,
                               public val score : Int)
    {
    }


    public fun loadRanking()
    {
        rankingUsersData.value?.add(RankingUserData("Naplm", 77))
        rankingUsersData.value?.add(RankingUserData("Juan", 900000))
        rankingUsersData.value?.add(RankingUserData("xXEricAkaYuukiasXx", 696969))
        rankingUsersData.value?.add(RankingUserData("NaCl", 434))
        rankingUsersData.value?.add(RankingUserData("Enemy UAV", 60))
        rankingUsersData.value?.add(RankingUserData("VTOL", 59))
        rankingUsersData.value?.add(RankingUserData("Dron Bomba", 2))
        rankingUsersData.value?.add(RankingUserData("Ju", 1243))

        rankingUsersData.value?.sortByDescending {
            it.score
        }

        rankingUsersData.value = rankingUsersData.value
    }

}