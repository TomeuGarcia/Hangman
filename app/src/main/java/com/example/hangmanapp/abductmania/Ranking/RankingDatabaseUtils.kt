package com.example.hangmanapp.abductmania.Ranking

import com.google.firebase.auth.FirebaseAuth

class RankingDatabaseUtils
{
    public val DB_URL = "https://abductmania-default-rtdb.europe-west1.firebasedatabase.app/"
    public val RANKING_DB_ID = "ranking"
    public val PLAYERSCORES_DB_ID = "playerScores"
    public val USERNAME_DB_ID = "username"
    public val SCORE_DB_ID = "score"

    public val currentUserId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid.toString()

    public fun getGuestUsername() : String
    {
        return "Guest-"+currentUserId.substring(0, 8)
    }

}