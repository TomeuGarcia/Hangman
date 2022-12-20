package com.example.hangmanapp.abductmania.Game.Api

import android.content.Context
import android.preference.PreferenceManager
import android.widget.Toast
import com.example.hangmanapp.R
import com.example.hangmanapp.abductmania.DatabaseUtils.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HangmanApiCommunication(private val createNewHangmanGameResponseCallback : (HangmanNewGame) -> Unit,
                              private val createNewHangmanGameFailureCallback : () -> Unit,
                              private val getSolutionResponseCallback : (HangmanGameSolution) -> Unit,
                              private val getSolutionFailureCallback : () -> Unit,
                              private val getHintResponseCallback : (HangmanGameHint) -> Unit,
                              private val getHintFailureCallback : () -> Unit,
                              private val guessLetterResponseCallback : (HangmanLetterGuessResponse, Char) -> Unit,
                              private val guessLetterFailureCallback : () -> Unit)
{

    //private val HANGMAN_API_URL : String = "https://hangman-api.herokuapp.com/"
    private val HANGMAN_API_URL : String = "http://hangman.enti.cat:5002/"
    private val LANGUAGES : List<String> = listOf("en", "cat", "es")
    private val USERS_COLLECTION = "users"
    private val LANGUAGE = "language"
    private val EMAIL = "email"

    private val retrofit = Retrofit.Builder()
        .baseUrl(HANGMAN_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitApiHangman = retrofit.create(ApiHangman::class.java)

    private lateinit var firestore: FirebaseFirestore
    private lateinit var email: String
    private lateinit var usersCollection : CollectionReference
    private var currentUser : User? = null
    private var users = arrayListOf<User>()

    private var languageIndex = 0

    public fun loadData(context : Context)
    {
        firestore = FirebaseFirestore.getInstance()
        usersCollection = firestore.collection(USERS_COLLECTION)

        // Shared prefs
        val shared = PreferenceManager.getDefaultSharedPreferences(context)
        email = shared.getString(EMAIL, null)?: ""

        languageIndex = shared.getInt(LANGUAGE, 0)

        // Firestore
        usersCollection.get()
            .addOnSuccessListener {
                users = it?.documents?.mapNotNull { dbUser ->
                    dbUser.toObject(User::class.java)
                } as ArrayList<User>

                currentUser = users.find { itUser ->
                    isUserCurrentUser(itUser)
                }

                currentUser?.apply{
                    languageIndex = language
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, context.getString(R.string.somethingWentWrong),
                    Toast.LENGTH_LONG).show()
            }
    }

    private fun isUserCurrentUser(user : User) : Boolean
    {
        return user.email == email
    }

    public fun createNewHangmanGame()
    {
        retrofitApiHangman.createNewLangHangmanGame(LANGUAGES[languageIndex]).enqueue(object : Callback<HangmanNewGame> {

            override fun onResponse(call: Call<HangmanNewGame>,
                                    response: Response<HangmanNewGame>)
            {
                val hangmanNewGame =  response.body()
                if (hangmanNewGame != null)
                {
                    createNewHangmanGameResponseCallback(hangmanNewGame)
                }
                else
                {
                    createNewHangmanGameFailureCallback()
                }
            }

            override fun onFailure(call: Call<HangmanNewGame>, t: Throwable)
            {
                createNewHangmanGameFailureCallback()
            }
        })
    }

    public fun getSolution(gameToken : String)
    {
        retrofitApiHangman.getSolution(gameToken).enqueue(object : Callback<HangmanGameSolution> {
            override fun onResponse(call: Call<HangmanGameSolution>,
                                    response: Response<HangmanGameSolution>)
            {
                val hangmanGameSolution = response.body()
                if (hangmanGameSolution != null)
                {
                    getSolutionResponseCallback(hangmanGameSolution)
                }
                else
                {
                    getSolutionFailureCallback()
                }
            }

            override fun onFailure(call: Call<HangmanGameSolution>, t: Throwable)
            {
                getSolutionFailureCallback()
            }
        })
    }

    public fun getHint(gameToken : String)
    {
        retrofitApiHangman.getHint(gameToken).enqueue(object : Callback<HangmanGameHint> {
            override fun onResponse(call: Call<HangmanGameHint>,
                                    response: Response<HangmanGameHint>)
            {
                val hangmanGameHint = response.body()
                if (hangmanGameHint != null)
                {
                    getHintResponseCallback(hangmanGameHint)
                }
                else
                {
                    getHintFailureCallback()
                }
            }

            override fun onFailure(call: Call<HangmanGameHint>, t: Throwable)
            {
                getHintFailureCallback()
            }
        })
    }

    public fun guessLetter(gameToken : String, letter : Char)
    {
        retrofitApiHangman.guessLetter(letter.toString(), gameToken).enqueue(object :
            Callback<HangmanLetterGuessResponse> {
            override fun onResponse(call: Call<HangmanLetterGuessResponse>,
                                    response: Response<HangmanLetterGuessResponse>)
            {
                val hangmanLetterGuessResponse = response.body()
                if (hangmanLetterGuessResponse != null)
                {
                    guessLetterResponseCallback(hangmanLetterGuessResponse, letter)
                }
                else
                {
                    guessLetterFailureCallback()
                }
            }

            override fun onFailure(call: Call<HangmanLetterGuessResponse>, t: Throwable)
            {
                guessLetterFailureCallback()
            }
        })
    }



}