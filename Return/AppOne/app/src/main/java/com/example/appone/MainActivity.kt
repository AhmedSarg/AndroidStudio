package com.example.appone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import java.util.Random

class MainActivity : AppCompatActivity() {
    lateinit var dice1 : ImageView
    lateinit var dice2 : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rollButton : Button = findViewById(R.id.roll_button)
        dice1 = findViewById(R.id.diceView1)
        dice2 = findViewById(R.id.diceView2)
        rollButton.setOnClickListener {
            rollDice(dice1)
            rollDice(dice2)
        }
        dice1.setOnClickListener {
            rollDice(dice1)
        }
        dice2.setOnClickListener {
            rollDice(dice2)
        }
    }

    private fun rollDice(dice : ImageView) {
        val drawableResRand = when (Random().nextInt(6) + 1) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.empty_dice
        }
        dice.setImageResource(drawableResRand)
    }
}