package com.example.parcialv2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResultFragment : Fragment() {

    private lateinit var tvFinalScore: TextView
    private lateinit var tvHighScore: TextView
    private lateinit var rvHistory: RecyclerView
    private lateinit var btnPlayAgain: Button

    companion object {
        val scoreHistory = mutableListOf<Int>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        val btnPlayAgain: Button = view.findViewById(R.id.btnPlayAgain)

        tvFinalScore = view.findViewById(R.id.tvFinalScore)
        tvHighScore = view.findViewById(R.id.tvHighScore)
        rvHistory = view.findViewById(R.id.rvHistory)

        val finalScore = arguments?.getInt("score") ?: 0

        mostrarPuntajes(finalScore)
        actualizarHistorial(finalScore)

        btnPlayAgain.setOnClickListener {
            findNavController().navigate(R.id.action_result_to_inicio)
        }

        return view
    }

    private fun mostrarPuntajes(finalScore: Int) {
        val prefs = requireActivity().getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        val highScore = prefs.getInt("high_score", 0)

        if (finalScore > highScore) {
            prefs.edit().putInt("high_score", finalScore).apply()
        }

        val best = prefs.getInt("high_score", 0)
        tvFinalScore.text = "PUNTAJE: $finalScore"
        tvHighScore.text = "PUNTAJE MÁS ALTO: $best"
    }

    private fun actualizarHistorial(finalScore: Int) {
        scoreHistory.add(finalScore)

        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        rvHistory.adapter = HistoryAdapter(scoreHistory)
    }
}
