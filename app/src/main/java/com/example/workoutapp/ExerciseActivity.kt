package com.example.workoutapp

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import org.w3c.dom.Text
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration = 0
    private var exerciseList: ArrayList<ExerciseModel>? = null;
    private var currentExercisePosition = -1;
    private var textToSpeech: TextToSpeech? = null
    private var player: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
 /*       setSupportActionBar(toolbar_exercise_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_exercise_activity.setNavigationOnClickListener {
            onBackPressed()
        }*/
        exerciseView.visibility = View.INVISIBLE
        restView.visibility = View.VISIBLE
        textToSpeech = TextToSpeech(this, this)
        exerciseList = Exercises.defaultExerciseList()
        setupRestView()
    }

    override fun onDestroy() {
        if (restTimer != null) {
            restProgress = 0;
        }
        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0;
        }
        if(textToSpeech!= null){
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
        if(player!=null){
            player!!.stop()
        }
        super.onDestroy()
    }


    private fun setRestProgressBar() {
        progressBar.progress = restProgress
        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 10 - restProgress
                tvTimer.text = (10 - restProgress).toString()
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onFinish() {
                currentExercisePosition++
                restView.visibility = View.GONE;
                setupExerciseView();
            }
        }.start()
    }


    private fun setExerciseProgressBar() {
        exerciseProgressBar.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(30000, 1000) {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                exerciseProgressBar.progress = 30 - exerciseProgress
                exerciseTvTimer.text = (30 - exerciseProgress).toString()
                if((30 - exerciseProgress)<=10)
                    exerciseNameToSpeech((30 - exerciseProgress).toString())
            }

            override fun onFinish() {
                if(currentExercisePosition<exerciseList?.size!! - 1){
                    Log.i("Test","I ran")
                    setupRestView()
                }
                else{

                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }

    private fun goToFinish() {
        Log.i("Test","I ran")
        val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
        startActivity(intent)    }

    private fun setupRestView() {
        try{
            player = MediaPlayer.create(applicationContext, R.raw.success)
            player!!.isLooping = false
            player!!.start()

        }catch(e: Exception){
            e.printStackTrace()
        }
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }
        exerciseView.visibility = View.GONE;
        restView.visibility = View.VISIBLE;
        upcomingExerciseName.text = exerciseList!![currentExercisePosition+1].getName()
        exerciseNameToSpeech("Get Ready for, "+upcomingExerciseName.text.toString())
        setRestProgressBar()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupExerciseView() {
        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        exerciseNameToSpeech(exerciseList!![currentExercisePosition].getName())
        exerciseView.visibility = View.VISIBLE;
        setExerciseProgressBar()
        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val rslt = textToSpeech!!.setLanguage(Locale.US)
            if(rslt == TextToSpeech.LANG_MISSING_DATA || rslt == TextToSpeech.LANG_NOT_SUPPORTED)
                Log.e("TTS", "Language not supported" )
        }
        else{

        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun exerciseNameToSpeech(exerciseName: String){
        textToSpeech!!.speak(exerciseName, TextToSpeech.QUEUE_FLUSH, null, " ")
    }


}