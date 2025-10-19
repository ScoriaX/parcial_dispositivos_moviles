package com.example.parcialv2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.core.text.HtmlCompat

class WelcomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inicio, container, false)
        val btnIniciarJuego: Button = view.findViewById(R.id.btnIniciarJuego)

        showWelcomeDialog()

        btnIniciarJuego.setOnClickListener {
            findNavController().navigate(R.id.action_inicio_to_juego)
        }

        return view
    }

    private fun showWelcomeDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.dialog_title))
        builder.setMessage(HtmlCompat.fromHtml(getString(R.string.dialog_message), HtmlCompat.FROM_HTML_MODE_LEGACY))
        builder.setPositiveButton(getString(R.string.dialog_button_start)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
    }
}