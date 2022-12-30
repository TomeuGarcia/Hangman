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
    private val DB_URL = "https://abductmania-default-rtdb.europe-west1.firebasedatabase.app/"
    private val RANKING_DB_ID = "ranking"
    private val PLAYERSCORES_DB_ID = "playerScores"
    private val USERNAME_DB_ID = "username"
    private val SCORE_DB_ID = "score"

    val rankingRef = Firebase.database(DB_URL).getReference(RANKING_DB_ID)
    private lateinit var database: DatabaseReference

    public val rankingUsersData = MutableLiveData<ArrayList<RankingUserData>>()

    private val currentUserId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid.toString()


    init
    {
        rankingUsersData.value = ArrayList<RankingUserData>()
        database = Firebase.database.reference
    }

    @IgnoreExtraProperties
    data class RankingUserData(public val username : String? = null,
                               public var score : Int? = null)
    {
    }

    public fun addNewUser(userId: String, userScore: Int = 0)
    {
        val rankingUsersData = RankingUserData(userId, userScore)

        database.child(RANKING_DB_ID).child(userId).setValue(rankingUsersData)
        // Looking at --> https://firebase.google.com/docs/database/android/read-and-write#kotlin+ktx_1
    }


    public fun loadRanking(context : Context)
    {
        val rankingPlayerScores = rankingRef.child(PLAYERSCORES_DB_ID)
        rankingPlayerScores.child(currentUserId).child(USERNAME_DB_ID).setValue("Joanet")
        rankingPlayerScores.child(currentUserId).child(SCORE_DB_ID).setValue("100")

        val request = rankingRef.child(PLAYERSCORES_DB_ID).get()

        request.addOnSuccessListener {
            val ranking = it.value

            if (ranking != null && ranking is List<*>) {
                val r = ranking.filterIsInstance<RankingUserData>()
                r.forEach { rud ->
                    rankingUsersData.value?.add(RankingUserData(rud.username, rud.score))
                }
                subscribe(r)
            }
            else {
                // ERROR
                Toast.makeText(context, "ranking request ERROR", Toast.LENGTH_SHORT).show()
            }
        }
        request.addOnFailureListener {
            // ERROR
            Toast.makeText(context, "ranking request FAILURE", Toast.LENGTH_SHORT).show()
        }

        rankingUsersData.value?.sortByDescending {
            it.score
        }

        rankingUsersData.value = rankingUsersData.value
    }

    private fun subscribe(rankingList: List<RankingUserData>) {
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



}
