package com.example.hangmanapp.abductmania.Ranking

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import java.lang.Error

class RankingViewModel : ViewModel()
{
    public val rankingUsersData = MutableLiveData<ArrayList<RankingUserData>>()
    val db = Firebase.database("https://abductmania-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("ranking") // collection name

    private val currentUser: String
        get() = FirebaseAuth.getInstance().currentUser?.email ?: throw IllegalStateException("Not logged in")

    init
    {
        rankingUsersData.value = ArrayList<RankingUserData>()
    }


    data class RankingUserData(public val username : String,
                               public var score : Int)
    {
    }


    public fun loadRanking()
    {
        val request = db.get()
        request.addOnSuccessListener {
            val rank = it.value

            if (rank != null && rank is List<*>) {
                val r = rank.filterIsInstance<RankingUserData>()
                r.forEach { rud ->
                    rankingUsersData.value?.add(RankingUserData(rud.username, rud.score))
                }
                subscribe(r)
            }
            else {
                // ERROR
            }
        }
        request.addOnFailureListener {
            // ERROR
        }

        rankingUsersData.value?.sortByDescending {
            it.score
        }

        rankingUsersData.value = rankingUsersData.value
    }

    private fun subscribe(rankingList: List<RankingUserData>) {
        rankingList.forEach {
            db.child(it.username).addValueEventListener(object : ValueEventListener {
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
