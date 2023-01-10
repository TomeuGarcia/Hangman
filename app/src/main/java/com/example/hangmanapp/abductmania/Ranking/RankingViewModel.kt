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
    public val gettingNewData = MutableLiveData<Boolean>()


    public fun startRankingListening(context : Context)
    {
        rankingRef = Firebase.database(rankingDbUtils.DB_URL).getReference(rankingDbUtils.RANKING_DB_ID)
        rankingUsersData.value = ArrayList<RankingUserData>()

        val request = rankingRef.child(rankingDbUtils.PLAYERSCORES_DB_ID).get()

        gettingNewData.value = false

        rankingRef.addValueEventListener( object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot)
            {
                isRankingDataReady.value = false

                rankingUsersData.value?.clear()
                gettingNewData.value = true

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

                    gettingNewData.value = false
                }

                request.addOnFailureListener {
                    // ERROR
                    println( "DB error --------> ${it.message}")
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
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
