package com.example.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var resImg: ImageView
    private lateinit var rollBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*val rollBtn: Button = findViewById(R.id.roll_btn)*/
        rollBtn = findViewById(R.id.roll_btn)
        rollBtn.setOnClickListener {
            rollDice()
        }
        resImg = findViewById(R.id.dice)
    }
    private fun rollDice() {
        val diceRes = when (Random().nextInt(6) + 1) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.empty_dice
        }
        resImg.setImageResource(diceRes)
    }
}