package com.fairyfo.frenzy.ui.fragments.bonusGame

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fairyfo.frenzy.R
import com.fairyfo.frenzy.databinding.FragmentBonusGameBinding
import com.fairyfo.frenzy.ui.core.ViewBindingFragment
import com.fairyfo.frenzy.ui.extensions.addOnBackPressedCallback
import com.fairyfo.frenzy.ui.extensions.navigateSafe
import com.fairyfo.frenzy.ui.extensions.setTextGradient
import com.fairyfo.frenzy.ui.extensions.vibrate
import com.fairyfo.frenzy.ui.fragments.game2.Game2FragmentDirections
import com.fairyfo.frenzy.ui.models.Miner
import com.fairyfo.frenzy.utils.SharedPrefs

class BonusGameFragment : ViewBindingFragment<FragmentBonusGameBinding>(
    FragmentBonusGameBinding::inflate,
) {

    private val viewModel by viewModels<BonusGameViewModel>()
    private val prefs by lazy { SharedPrefs.getInstance(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        with(binding) {
            textViewBet.setTextGradient(R.color.pink1, R.color.pink2)
            buttonSpin.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)

            textViewBet.text = prefs.lastBetGame3.toString()
            textViewBalance.text = getString(R.string.balance_placeholder, prefs.balance)
            textViewWin.text = getString(R.string.win_placeholder, prefs.lastWinGame3)

            addOnBackPressedCallback {
                findNavController().navigateSafe(
                    BonusGameFragmentDirections.actionBonusGameFragmentToGamesListFragment(),
                )
            }
            imageViewBack.setOnClickListener {
                findNavController().navigateSafe(
                    BonusGameFragmentDirections.actionBonusGameFragmentToGamesListFragment(),
                )
            }
            buttonSpin.setOnClickListener {
                val bet = prefs.lastBetGame3
                if (bet == 0L) return@setOnClickListener
                viewModel.play(
                    bet,
                    prefs.balance,
                )
            }
            imageViewMinus.setOnClickListener {
                var bet = textViewBet.text.toString().toLong() - 100L
                if (bet < 0) bet = 0
                prefs.lastBetGame3 = bet
                textViewBet.text = bet.toString()
            }
            imageViewPlus.setOnClickListener {
                var bet = prefs.lastBetGame3 + 100L
                if (bet > prefs.balance) bet = prefs.balance
                prefs.lastBetGame3 = bet
                textViewBet.text = bet.toString()
            }
            minerView.setOnItemClickListener(viewModel::onItemClicked)

            viewModel.game.observe(viewLifecycleOwner) { miner ->
                minerView.setGame(miner)
                if (miner.gameState == Miner.GameState.Finished) {
                    vibrate(800)
                }
            }
            viewModel.win.observe(viewLifecycleOwner) { win ->
                prefs.lastWinGame3 = win
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
}
