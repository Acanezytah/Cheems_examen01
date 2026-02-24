package mx.itson.cheems

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var gameOverCard = 0

    var cheemsMasterCard = 0
    var goodCardsFlipped = 0
    var gameFinished = false
    lateinit var flippedCards: BooleanArray


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Toast.makeText(this,
            getString(R.string.Welcome),
            Toast.LENGTH_LONG).show()

        start()
    }


    //Función para que vibre

    fun vibrate(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Si la versión del sistema operativo instalado en el teléfono es igual o mayor a Android 12 (API 31)
            val vibratorAdmin = applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorAdmin.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(1500)
        }
    }


    fun start(){

        goodCardsFlipped = 0
        gameFinished = false
        flippedCards = BooleanArray(13) // Usamos 13 para ignorar el índice 0

        for (i in 1 .. 12) {
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)
            ) as ImageButton
            btnCard.setOnClickListener(this)
            btnCard.setBackgroundResource(R.drawable.cheems_question)
        }

        gameOverCard = (1 .. 12).random()

        //Adición de la carta cheems master a un id random de carta y evitar que se repita el id de gameovercard

        cheemsMasterCard = (1 .. 12).random()

        while (cheemsMasterCard == gameOverCard) {
            cheemsMasterCard = (1 .. 12).random()
        }

        Log.d("Carta perdedora", "La carta perdedora es: $gameOverCard")
        Log.d("Carta cheems_master", "La carta cheems_master es: $cheemsMasterCard")
    }
    fun flip(card: Int){

        if(gameFinished) return  // Si ya terminó el juego, no hace nada

        // Si ya estaba volteada, no hacer nada
        if(flippedCards[card]) return

        flippedCards[card] = true

        if(card == gameOverCard) {

            gameFinished = true

            vibrate()

            Toast.makeText(
                this,
                getString(R.string.lose),
                Toast.LENGTH_LONG
            ).show()

            for (i in 1..12) {
                val btnCard = findViewById<View>(
                    resources.getIdentifier("card$i", "id", this.packageName)
                ) as ImageButton

                if (i == card) {
                    btnCard.setBackgroundResource(R.drawable.cheems_bad)
                }
                else if (i == cheemsMasterCard){
                    btnCard.setBackgroundResource(R.drawable.cheems_master)
                }
                else {
                    btnCard.setBackgroundResource(R.drawable.cheems_ok)
                }
            }
        }
            else if(card == cheemsMasterCard) {

                gameFinished = true

                vibrate()

                Toast.makeText(this,
                    getString(R.string.chemms_master),
                    Toast.LENGTH_LONG).show()

                for(i in 1 .. 12){
                    val btnCard = findViewById<View>(
                        resources.getIdentifier("card$i", "id", this.packageName)
                    ) as ImageButton

                    if (i == card) {
                        btnCard.setBackgroundResource(R.drawable.cheems_master)
                    }
                    else if (i == gameOverCard){
                        btnCard.setBackgroundResource(R.drawable.cheems_bad)
                    }else {
                        btnCard.setBackgroundResource(R.drawable.cheems_ok)
                    }
                }

            }

         else {

            val btnCard = findViewById<View>(
                resources.getIdentifier("card$card", "id", this.packageName)
            ) as ImageButton

            btnCard.setBackgroundResource(R.drawable.cheems_ok)

            goodCardsFlipped++

            // CONDICIÓN DE VICTORIA
            if(goodCardsFlipped == 10){
                gameFinished = true
                vibrate()

                Toast.makeText(this,
                    getString(R.string.cheems_master),
                    Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onClick(v: View) {
        when(v.id) {
            R.id.card1 -> flip(1)
            R.id.card2 -> flip(2)
            R.id.card3 -> flip(3)
            R.id.card4 -> flip(4)
            R.id.card5 -> flip(5)
            R.id.card6 -> flip(6)
            R.id.card7 -> flip(7)
            R.id.card8 -> flip(8)
            R.id.card9 -> flip(9)
            R.id.card10 -> flip(10)
            R.id.card11 -> flip(11)
            R.id.card12 -> flip(12)
            R.id.restart -> this.start()
        }
    }
}