package com.bigwinlab.bingo.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bigwinlab.bingo.R
import com.bigwinlab.bingo.databinding.ActivityGammBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Gamm : AppCompatActivity() {
    lateinit var enemyPic: ImageView
    lateinit var playerPic: ImageView
    val choices = mutableListOf<String>("Lion", "Buffalo", "Deer")
    var playerscore = 0
    var enemyscore = 0
    lateinit var playerScoreText: TextView
    lateinit var enemyScoreText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gamm)

        MaterialAlertDialogBuilder(this@Gamm)
            .setTitle("Simple Rules")
            .setMessage("Lion defeats Deer, Buffalo defeats Lion, Deer defeats Buffalo")
            .setCancelable(false)
            .setPositiveButton("Play"){dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()

        enemyPic = findViewById(R.id.enemy_pic)
        playerPic = findViewById(R.id.player_pic)

        playerScoreText = findViewById(R.id.player_score_text)
        enemyScoreText = findViewById(R.id.enemy_score_text)
        win()

    }
    fun rock(v: View){
        playerPic.setImageResource(R.drawable.cat)
        val enemychoice = choices.random()
        when (enemychoice){
            "Lion" -> {enemyPic.setImageResource(R.drawable.cat)}

            "Buffalo" -> {enemyPic.setImageResource(R.drawable.buffalo)
                enemyscore++
                enemyScoreText.text = enemyscore.toString()

            }

            "Deer" -> {enemyPic.setImageResource(R.drawable.deer)
                playerscore++
                playerScoreText.text = playerscore.toString()

            }

        }


    }

    fun paper(v: View){
        playerPic.setImageResource(R.drawable.buffalo)
        val enemychoice = choices.random()
        when (enemychoice){
            "Lion" -> {enemyPic.setImageResource(R.drawable.cat)
                playerscore++
                playerScoreText.text = playerscore.toString()}

            "Buffalo" -> {enemyPic.setImageResource(R.drawable.buffalo)}

            "Deer" -> {enemyPic.setImageResource(R.drawable.deer)
                enemyscore++
                enemyScoreText.text = enemyscore.toString()}

        }
        win()
    }

    fun scissors(v: View) {
        playerPic.setImageResource(R.drawable.deer)
        val enemychoice = choices.random()
        when (enemychoice) {
            "Lion" -> {
                enemyPic.setImageResource(R.drawable.cat)
                enemyscore++
                enemyScoreText.text = enemyscore.toString()
            }

            "Buffalo" -> {
                enemyPic.setImageResource(R.drawable.buffalo)
                playerscore++
                playerScoreText.text = playerscore.toString()
            }

            "Deer" -> {
                enemyPic.setImageResource(R.drawable.deer)
            }

        }
        win()
    }

    private fun win() {
        val intent = Intent(this@Gamm, Finish::class.java)

        if (playerscore > 2 && playerscore > enemyscore) {

            startActivity(intent)

        } else if (enemyscore > 2 && enemyscore > playerscore ) {

            startActivity(intent)

        }
    }

}