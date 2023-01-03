package com.example.hangmanapp.abductmania.Ranking

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class RankingViewModel : ViewModel()
{
    private lateinit var rankingRef: DatabaseReference

    private val currentUserId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid.toString()


    private val rankingDbUtils = RankingDatabaseUtils()

    public val rankingUsersData = MutableLiveData<ArrayList<RankingUserData>>()
    public val isRankingDataReady = MutableLiveData<Boolean>()


    init
    {
        rankingRef = Firebase.database(rankingDbUtils.DB_URL).getReference(rankingDbUtils.RANKING_DB_ID)
        rankingUsersData.value = ArrayList<RankingUserData>()
    }

    @IgnoreExtraProperties
    data class RankingUserData(public val username : String? = null,
                               public var score : Int? = null)
    {
    }

    public fun addNewUser(userId: String, userScore: Int = 0)
    {
        val rankingUsersData = RankingUserData(userId, userScore)

        //database.child(rankingDbUtils.RANKING_DB_ID).child(userId).setValue(rankingUsersData)
    }


    public fun loadRanking(context : Context)
    {
        /*
        val rankingPlayerScores = rankingRef.child(PLAYERSCORES_DB_ID)
        rankingPlayerScores.child(currentUserId).child(USERNAME_DB_ID).setValue("Joanet")
        rankingPlayerScores.child(currentUserId).child(SCORE_DB_ID).setValue(1000.0)
        rankingPlayerScores.child(currentUserId+"q").child(USERNAME_DB_ID).setValue("Pepe")
        rankingPlayerScores.child(currentUserId+"q").child(SCORE_DB_ID).setValue(400.0)
        */

        isRankingDataReady.value = false

        val request = rankingRef.child(rankingDbUtils.PLAYERSCORES_DB_ID).get()

        request.addOnSuccessListener {
            val playerScoresById = it.children

            playerScoresById.forEach { playerScore ->
                val username = playerScore.child(rankingDbUtils.USERNAME_DB_ID).value as String?
                val score = playerScore.child(rankingDbUtils.SCORE_DB_ID).value as Long?

                rankingUsersData.value?.add(RankingUserData(username, score?.toInt()))
            }
            subscribeUpdateData(rankingUsersData?.value as List<RankingUserData>)

            sortRankingData()
            rankingUsersData.value = rankingUsersData.value // Update
            isRankingDataReady.value = true
        }

        request.addOnFailureListener {
            // ERROR
            println( "DB error --------> ${it.message}")
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }


    }

    private fun subscribeUpdateData(rankingList: List<RankingUserData>) {
        rankingList.forEach {
            rankingRef.child(it.username ?: "").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val typeIndicator = object : GenericTypeIndicator<RankingUserData>() {}
                    val rud = snapshot.getValue(typeIndicator)

                    if (rud != null){
                        it.score = rud.score
                        rankingUsersData.postValue(rankingList as ArrayList<RankingUserData>)
                    }
                }

                override fun onCancelled(error: DatabaseError) = Unit
            })
        }
    }

    private fun sortRankingData()
    {
        rankingUsersData.value?.sortByDescending {
            it.score
        }
    }

    public fun getArrayRankingData() : List<RankingUserData>
    {
        return rankingUsersData?.value ?: listOf<RankingUserData>()
    }


}
