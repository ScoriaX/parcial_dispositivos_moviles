// Descripción: Fragmento de juego que presenta la logica principal,
// muestra tiempo, puntaje, el texto del color y los botones.
// Autor: Piero Poblete
// Fecha creación: 17/10/2025
// Última modificación: 19/10/2025

package com.example.parcialv2

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlin.random.Random
import android.media.SoundPool
import android.media.AudioAttributes

class GameFragment : Fragment() {

    // VARIABLES
    private lateinit var tvColorName: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btnRed: Button
    private lateinit var btnBlue: Button
    private lateinit var btnGreen: Button
    private lateinit var btnYellow: Button
    private lateinit var btnExit: Button
    private lateinit var soundPool: SoundPool
    private var soundCorrect: Int = 0
    private var soundWrong: Int = 0

    // LISTA DE COLORES
    private val colorNames = listOf("ROJO", "AZUL", "VERDE", "AMARILLO")
    private val colorValues = mapOf(
        "ROJO" to R.color.red,
        "AZUL" to R.color.blue,
        "VERDE" to R.color.green,
        "AMARILLO" to R.color.yellow
    )

    private var currentColorName = ""
    private var currentTextColor = 0
    private var score = 0
    private var timeLeft = 30
    private lateinit var timer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_juego, container, false)

        super.onCreate(savedInstanceState)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(audioAttributes)
            .build()

        // ASIGNACION DE AUDIOS
        soundCorrect = soundPool.load(requireContext(), R.raw.correct_sound, 1)
        soundWrong = soundPool.load(requireContext(), R.raw.wrong_sound, 1)

        // ASIGNACIONES
        tvColorName = view.findViewById(R.id.tvColorName)
        tvScore = view.findViewById(R.id.tvScore)
        tvTimer = view.findViewById(R.id.tvTimer)
        btnRed = view.findViewById(R.id.btnRed)
        btnBlue = view.findViewById(R.id.btnBlue)
        btnGreen = view.findViewById(R.id.btnGreen)
        btnYellow = view.findViewById(R.id.btnYellow)
        btnExit = view.findViewById(R.id.btnExit)

        btnRed.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        btnBlue.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
        btnGreen.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
        btnYellow.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yellow))

        // EMPIEZA EL JUEGO
        startGame()

        // IMPLEMENTACION DE LOS BOTONES
        btnRed.setOnClickListener { checkAnswer("ROJO") }
        btnBlue.setOnClickListener { checkAnswer("AZUL") }
        btnGreen.setOnClickListener { checkAnswer("VERDE") }
        btnYellow.setOnClickListener { checkAnswer("AMARILLO") }
        btnExit.setOnClickListener { endGame() }

        return view
    }

    // IMPLEMENTACIONES
    // EMPIEZA EL JUEGO INICIANDO LAS DEMAS VARIABLES A TRAVÉS DE LAS FUNCIONES
    private fun startGame() {
        score = 0
        updateScore()
        nextColor()
        startTimer()
    }

    // INICIAR TIMER
    private fun startTimer() {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()
                tvTimer.text = "TIEMPO: $timeLeft"
            }

            override fun onFinish() {
                endGame()
            }
        }.start()
    }

    // ESTABLECER NUEVO COLOR
    private fun nextColor() {
        currentColorName = colorNames.random()
        val randomColorNameForText = colorNames.random()
        currentTextColor = ContextCompat.getColor(requireContext(), colorValues[randomColorNameForText]!!)

        tvColorName.text = currentColorName
        tvColorName.setTextColor(currentTextColor)
    }

    // COMPROBAR RESPUESTA
    private fun checkAnswer(selected: String) {
        if (selected == currentColorName) {
            score++
            updateScore()
            // SUENA EL SONIDO DE SELECCION CORRECTA
            soundPool.play(soundCorrect, 2f, 2f, 0, 0, 1f)
        }
        nextColor()
        // SUENA EL SONIDO DE SELECCION INCORRECTA
        soundPool.play(soundWrong, 3f, 3f, 0, 0, 1f)
    }

    // ACTUALIZAR EL PUNTAJE
    private fun updateScore() {
        tvScore.text = "PUNTAJE: $score"
    }

    // FINALIZAR EL JUEGO
    private fun endGame() {
        timer.cancel()
        val bundle = Bundle().apply { putInt("score", score) }
        findNavController().navigate(R.id.action_juego_to_result, bundle)
    }
}
