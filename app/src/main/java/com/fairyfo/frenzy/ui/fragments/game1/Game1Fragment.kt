package com.fairyfo.frenzy.ui.fragments.game1

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fairyfo.frenzy.R
import com.fairyfo.frenzy.databinding.FragmentGame1Binding
import com.fairyfo.frenzy.ui.core.ViewBindingFragment
import com.fairyfo.frenzy.ui.extensions.addOnBackPressedCallback
import com.fairyfo.frenzy.ui.extensions.navigateSafe
import com.fairyfo.frenzy.ui.extensions.playWinSound
import com.fairyfo.frenzy.ui.extensions.setTextGradient
import com.fairyfo.frenzy.ui.extensions.vibrate
import com.fairyfo.frenzy.ui.fragments.SettingsFragmentDirections
import com.fairyfo.frenzy.utils.SharedPrefs

class Game1Fragment : ViewBindingFragment<FragmentGame1Binding>(
    FragmentGame1Binding::inflate,
) {

    private val viewModel by viewModels<Game1ViewModel>()
    private val prefs by lazy { SharedPrefs.getInstance(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        with(binding) {
            textViewBet.setTextGradient(R.color.pink1, R.color.pink2)
            buttonPlay.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)

            textViewBet.text = prefs.lastBetGame1.toString()
            textViewBalance.text = getString(R.string.balance_placeholder, prefs.balance)
            textViewWin.text = getString(R.string.win_placeholder, prefs.lastWinGame1)

            addOnBackPressedCallback {
                findNavController().navigateSafe(
                    Game1FragmentDirections.actionGame1FragmentToGamesListFragment(),
                )
            }
            imageViewBack.setOnClickListener {
                findNavController().navigateSafe(
                    Game1FragmentDirections.actionGame1FragmentToGamesListFragment(),
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
                prefs.lastBetGame1 = bet
                textViewBet.text = bet.toString()
            }
            imageViewPlus.setOnClickListener {
                var bet = prefs.lastBetGame1 + 100L
                if (bet > prefs.balance) bet = prefs.balance
                prefs.lastBetGame1 = bet
                textViewBet.text = bet.toString()
            }

            viewModel.win.observe(viewLifecycleOwner) { win ->
                if (prefs.lastWinGame1 != win) {
                    playWinSound()
                    vibrate()
                }
                prefs.lastWinGame1 = win
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

    override fun onStop() {
        super.onStop()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
}
