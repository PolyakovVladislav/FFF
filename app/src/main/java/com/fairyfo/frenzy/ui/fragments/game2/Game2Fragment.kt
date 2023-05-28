package com.fairyfo.frenzy.ui.fragments.game2

import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fairyfo.frenzy.R
import com.fairyfo.frenzy.databinding.FragmentGame2Binding
import com.fairyfo.frenzy.ui.core.ViewBindingFragment
import com.fairyfo.frenzy.ui.extensions.addOnBackPressedCallback
import com.fairyfo.frenzy.ui.extensions.navigateSafe
import com.fairyfo.frenzy.ui.extensions.playWinSound
import com.fairyfo.frenzy.ui.extensions.setTextGradient
import com.fairyfo.frenzy.ui.extensions.vibrate
import com.fairyfo.frenzy.ui.fragments.game1.Game1FragmentDirections
import com.fairyfo.frenzy.utils.SharedPrefs
import java.io.IOException


class Game2Fragment : ViewBindingFragment<FragmentGame2Binding>(
    FragmentGame2Binding::inflate,
) {

    private val viewModel by viewModels<Game2ViewModel>()
    private val prefs by lazy { SharedPrefs.getInstance(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        with(binding) {
            textViewBet.setTextGradient(R.color.pink1, R.color.pink2)
            buttonPlay.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)

            textViewBet.text = prefs.lastBetGame2.toString()
            textViewBalance.text = getString(R.string.balance_placeholder, prefs.balance)
            textViewWin.text = getString(R.string.win_placeholder, prefs.lastWinGame2)

            addOnBackPressedCallback {
                findNavController().navigateSafe(
                    Game2FragmentDirections.actionGame2FragmentToGamesListFragment(),
                )
            }
            imageViewBack.setOnClickListener {
                findNavController().navigateSafe(
                    Game2FragmentDirections.actionGame2FragmentToGamesListFragment(),
                )
            }
            buttonPlay.setOnClickListener {
                val bet = textViewBet.text.toString().toLong()
                if (bet == 0L) return@setOnClickListener
                viewModel.play(
                    bet,
                    prefs,
                )
            }
            imageViewMinus.setOnClickListener {
                var bet = textViewBet.text.toString().toLong() - 100L
                if (bet < 0) bet = 0
                prefs.lastBetGame2 = bet
                textViewBet.text = bet.toString()
            }
            imageViewPlus.setOnClickListener {
                var bet = prefs.lastBetGame2 + 100L
                if (bet > prefs.balance) bet = prefs.balance
                prefs.lastBetGame2 = bet
                textViewBet.text = bet.toString()
            }

            viewModel.win.observe(viewLifecycleOwner) { win ->
                if (prefs.lastWinGame2 != win) {
                    playWinSound()
                    vibrate()
                }
                prefs.lastWinGame2 = win
                textViewWin.text = getString(R.string.win_placeholder, win)
            }
            viewModel.balance.observe(viewLifecycleOwner) { balance ->
                if (balance <= 0) {
                    prefs.balance = 5000L
                } else {
                    prefs.balance = balance
                }
                if (prefs.balance <= textViewBet.text.toString().toLong()) {
                    textViewBet.text = prefs.balance.toString()
                }
                textViewBalance.text = getString(R.string.balance_placeholder, prefs.balance)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.slot1LiveData.observe(viewLifecycleOwner) {
            binding.slotView1.update(it)
        }
        viewModel.slot2LiveData.observe(viewLifecycleOwner) {
            binding.slotView2.update(it)
        }
        viewModel.slot3LiveData.observe(viewLifecycleOwner) {
            binding.slotView3.update(it)
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}
