package com.example.hangmanapp.abductmania.Game


import android.content.Context
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hangmanapp.abductmania.DatabaseUtils.SharedPrefsUtils
import com.example.hangmanapp.abductmania.Config.ConfigurationViewModel
import com.example.hangmanapp.abductmania.Game.Api.*
import com.example.hangmanapp.abductmania.Game.Drawings.HangmanDrawer
import com.example.hangmanapp.abductmania.Game.Drawings.HangmanDrawingBuilding
import com.example.hangmanapp.abductmania.Game.Drawings.HangmanDrawingUFO
import com.example.hangmanapp.abductmania.Game.Drawings.HangmanDrawingWaves
import com.example.hangmanapp.abductmania.Game.Keyboard.GameKeyboardMap
import com.example.hangmanapp.abductmania.Ranking.RankingDatabaseUtils
import com.example.hangmanapp.abductmania.MainMenu.MainMenuActivity
import com.example.hangmanapp.databinding.ActivityHangmanGameBinding
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import kotlin.math.max

class HangmanGameViewModel()
    : ViewModel()
{
    public val hangmanWord = MutableLiveData<String>()
    private var gameToken : String = ""
    private var hint : Char = ' '

    private val CORRECT_LETTER_POINTS : Int = 80
    private val WRONG_LETTER_POINTS : Int = 40
    private val ALL_LETTERS_GUESSED_POINTS : Int = 300
    private val RETRY_POINTS : Int = 100
    private var score : Int = 0

    private val MAX_WRONG_GUESSES : Int = 8
    private val RESET_GUESS_CHANCES : Int = 3
    private var wrongGuessesCount : Int = 0

    private val MISSING_LETTER : Char = '_'

    private val MAX_RETRIES = 1
    private var numRetries = MAX_RETRIES
    private var isGameOver : Boolean = false

    private lateinit var hangmanApiCommunication : HangmanApiCommunication
    private lateinit var gameKeyboardMap : GameKeyboardMap
    private lateinit var hangmanDrawer : HangmanDrawer


    private var countDownTimer: CountDownTimer? = null
    private val COUNTDOWN_TOTAL_TIME_MILLISECONDS : Long = 60000
    private val COUNTDOWN_INTERVAL_TIME_MILLISECONDS : Long = 1000
    private val COUNTDOWN_RETRY_TIME_MILLISECONDS : Long = COUNTDOWN_TOTAL_TIME_MILLISECONDS / 2
    public var countDownCurrentTimeSeconds = MutableLiveData<Long>()

    private lateinit var activityContext : Context  // Used to debug with Toast()

    public val isPausingDisabled = MutableLiveData<Boolean>()
    public val hasVictoryHappened = MutableLiveData<Boolean>()
    public val hasGameOverHappened = MutableLiveData<Boolean>()

    private val LOADED_AD_PARAM = "LOADED_AD_PARAM"
    private val SHOW_AD = "show_ad"
    private val LEVEL_START_PARAM = "LEVEL_START_PARAM"
    private val LEVEL_START = "level_start"
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics


    public fun createGame(context: Context, binding: ActivityHangmanGameBinding)
    {
        firebaseAnalytics.logEvent(
            LEVEL_START,
            bundleOf(
                LEVEL_START_PARAM to true
            )
        )

        activityContext = context

        hangmanWord.value = ""
        countDownCurrentTimeSeconds.value = COUNTDOWN_TOTAL_TIME_MILLISECONDS / 1000

        hangmanApiCommunication = HangmanApiCommunication(
            this::onCreateNewHangmanGameResponse, this::onCreateNewHangmanGameFailure,
            this::onGetSolutionResponse,          this::onGetSolutionFailure,
            this::onGetHintResponse,              this::onGetHintFailure,
            this::onGuessLetterResponse,          this::onGuessLetterFailure
        )
        hangmanApiCommunication.loadData(activityContext)

        gameKeyboardMap = GameKeyboardMap(binding)
        gameKeyboardMap.initButtonsClickCallback(this::guessLetter)
        gameKeyboardMap.hideOverlapImages()

        hangmanDrawer = HangmanDrawer(
            listOf(
                HangmanDrawingUFO(binding.ufoCenterImage, 2000, 20f),
                HangmanDrawingUFO(binding.ufoLeftImage, 2000, 20f),
                HangmanDrawingUFO(binding.ufoRightImage, 2000, 20f),
                HangmanDrawingWaves(binding.ufoWavesImage, 2000, 20f),
                HangmanDrawingBuilding(binding.building2Image, 10f),
                HangmanDrawingBuilding(binding.building4Image, -15f),
                HangmanDrawingBuilding(binding.building1Image, 15f),
                HangmanDrawingBuilding(binding.building3Image, -10f)
            )
        )

        createNewHangmanGame()
    }


    public fun getScore() : Int
    {
        return score
    }
    public fun getHangmanWord() : String
    {
        return hangmanWord.value ?: ""
    }

    private fun createNewHangmanGame()
    {
        gameKeyboardMap.disableRemainingLetterButtons()
        hangmanApiCommunication.createNewHangmanGame()
    }
    private fun onCreateNewHangmanGameResponse(hangmanNewGame : HangmanNewGame)
    {
        hangmanWord.value = hangmanNewGame.hangman
        gameToken = hangmanNewGame.token

        gameKeyboardMap.reenableRemainingLetterButtons()

        startCountDownTimer(COUNTDOWN_TOTAL_TIME_MILLISECONDS)
    }
    private fun onCreateNewHangmanGameFailure()
    {
        Toast.makeText(activityContext,"Something went wrong -> createNewHangmanGame()",
            Toast.LENGTH_LONG).show()

        gameKeyboardMap.reenableRemainingLetterButtons()
    }



    private fun getSolution()
    {
        hangmanApiCommunication.getSolution(gameToken)
    }
    private fun onGetSolutionResponse(hangmanGameSolution : HangmanGameSolution)
    {
        hangmanWord.value = hangmanGameSolution.solution
        gameToken = hangmanGameSolution.token

        onGameOverSolutionObtained()
    }
    private fun onGetSolutionFailure()
    {
        Toast.makeText(activityContext,"Something went wrong -> getSolution()",
            Toast.LENGTH_LONG).show()
    }



    private fun getHint()
    {
        hangmanApiCommunication.getHint(gameToken)
    }
    private fun onGetHintResponse(hangmanGameHint : HangmanGameHint)
    {
        hint = hangmanGameHint.hint[0]
        gameToken = hangmanGameHint.token

        guessLetter(hint)
    }
    private fun onGetHintFailure()
    {
        Toast.makeText(activityContext,"Something went wrong -> getHint()",
            Toast.LENGTH_LONG).show()
    }



    private fun guessLetter(letter : Char)
    {
        gameKeyboardMap.disableRemainingLetterButtons()
        hangmanApiCommunication.guessLetter(gameToken, letter)

        if (ConfigurationViewModel.isSoundOn.value == true)
        {
            MainMenuActivity.buttonSfxMP?.start()
        }
        else
            MainMenuActivity.buttonSfxMP?.pause()

    }
    private fun onGuessLetterResponse(hangmanLetterGuessResponse : HangmanLetterGuessResponse,
                                      letter : Char)
    {
        if (isGameOver) return

        val isCorrect : Boolean = hangmanLetterGuessResponse.correct
        hangmanWord.value = hangmanLetterGuessResponse.hangman
        gameToken = hangmanLetterGuessResponse.token

        if (isCorrect) { onGuessedLetterCorrectly(letter) }
        else { onGuessedLetterIncorrectly(letter) }


    }
    private fun onGuessLetterFailure()
    {
        Toast.makeText(activityContext,"Something went wrong -> guessLetter()",
            Toast.LENGTH_LONG).show()

        gameKeyboardMap.reenableRemainingLetterButtons()
    }


    private fun onGuessedLetterCorrectly(letter : Char)
    {
        gameKeyboardMap.setLetterCorrect(letter)
        score += CORRECT_LETTER_POINTS

        if (hasGuessedAllLetters())
            doVictory()
        else
            gameKeyboardMap.reenableRemainingLetterButtons()

    }

    private fun onGuessedLetterIncorrectly(letter : Char)
    {
        gameKeyboardMap.setLetterWrong(letter)
        score -= WRONG_LETTER_POINTS

        hangmanDrawer.drawPart(wrongGuessesCount)

        if (++wrongGuessesCount == MAX_WRONG_GUESSES)
            doGameOver()
        else
            gameKeyboardMap.reenableRemainingLetterButtons()
    }

    private fun hasGuessedAllLetters() : Boolean
    {
        hangmanWord.value?.apply {
            return !this.any {
                it == MISSING_LETTER
            }
        }

        return false
    }


    private fun doVictory()
    {
        countDownCurrentTimeSeconds.value?.apply {
            score += ALL_LETTERS_GUESSED_POINTS + this.toInt()
        }

        gameKeyboardMap.disableRemainingLetterButtons()
        countDownTimer?.cancel()

        isPausingDisabled.value = true
        hasVictoryHappened.value = true

        addUserScoreToRanking()
    }

    public fun doGameOver()
    {
        isGameOver = true
        --numRetries
        gameKeyboardMap.disableRemainingLetterButtons()
        countDownTimer?.cancel()

        if (hasRetriesLeft())
        {
            onGameOverSolutionObtained()
        }
        else
        {
            hangmanDrawer.drawRemainingParts()
            getSolution() // this is async.... wait until solution received to do real GameOver
            addUserScoreToRanking()
        }

        isPausingDisabled.value = true
    }

    private fun onGameOverSolutionObtained()
    {
        score = max(0, score) // Make score not negative

        hasGameOverHappened.value = true
    }



    private fun startCountDownTimer(startTime : Long)
    {
        countDownTimer = object : CountDownTimer(startTime, COUNTDOWN_INTERVAL_TIME_MILLISECONDS){
            override fun onTick(millisUntilFinished: Long)
            {
                countDownCurrentTimeSeconds.value = millisUntilFinished / 1000
            }
            override fun onFinish()
            {
                if(countDownCurrentTimeSeconds.value as Long <= 0)
                {
                    hangmanDrawer.drawRemainingParts()
                    wrongGuessesCount = MAX_WRONG_GUESSES
                    doGameOver()
                }
            }
        }.start()
    }

    public fun pauseCountDownTimer()
    {
        countDownTimer?.cancel()
    }

    public fun resumeCountDownTimer()
    {
        startCountDownTimer((countDownCurrentTimeSeconds.value ?: 0) * 1000)
    }

    public fun retryGameReset()
    {
        isGameOver = false
        score -= RETRY_POINTS

        if (wrongGuessesCount >= RESET_GUESS_CHANCES) wrongGuessesCount -= RESET_GUESS_CHANCES

        gameKeyboardMap.reenableRemainingLetterButtons()
        hangmanDrawer.undrawParts(wrongGuessesCount)

        countDownCurrentTimeSeconds.value = COUNTDOWN_RETRY_TIME_MILLISECONDS / 1000
        resumeCountDownTimer()
    }

    public fun hasRetriesLeft() : Boolean
    {
        return numRetries >= 0
    }

    public fun disableRetries()
    {
        numRetries = -1
    }

    public fun analyticsLogAd(ad : RewardedAd?)
    {
        firebaseAnalytics.logEvent(
            SHOW_AD,
            bundleOf(
                LOADED_AD_PARAM to (ad != null)
            )
        )
    }

    private fun addUserScoreToRanking()
    {
        val rankingDbUtils = RankingDatabaseUtils()
        val currentUserId = rankingDbUtils.currentUserId

        val shared = PreferenceManager.getDefaultSharedPreferences(activityContext)
        val currentUserUsername = shared.getString(SharedPrefsUtils.USERNAME, rankingDbUtils.getGuestUsername())


        val rankingRef = Firebase.database(rankingDbUtils.DB_URL).getReference(rankingDbUtils.RANKING_DB_ID)

        val request = rankingRef.child(rankingDbUtils.PLAYERSCORES_DB_ID).child(currentUserId).get()

        request.addOnSuccessListener {

            val currentScore = (it.child(rankingDbUtils.SCORE_DB_ID).value as Long?) ?: 0
            val totalScore = currentScore + score.toLong()

            val rankingPlayerScore = rankingRef.child(rankingDbUtils.PLAYERSCORES_DB_ID).child(currentUserId)

            rankingPlayerScore.child(rankingDbUtils.USERNAME_DB_ID).setValue(currentUserUsername)
            rankingPlayerScore.child(rankingDbUtils.SCORE_DB_ID).setValue(totalScore)
        }

        request.addOnFailureListener {
            // ERROR
            Toast.makeText(activityContext, "Couldn't add score to ranking", Toast.LENGTH_LONG).show()
        }

    }

}