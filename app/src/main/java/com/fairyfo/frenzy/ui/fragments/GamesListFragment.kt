package com.fairyfo.frenzy.ui.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.fairyfo.frenzy.R
import com.fairyfo.frenzy.databinding.FragmentGamesListBinding
import com.fairyfo.frenzy.ui.core.ViewBindingFragment
import com.fairyfo.frenzy.ui.extensions.navigateSafe
import com.fairyfo.frenzy.ui.extensions.setTextGradient

class GamesListFragment : ViewBindingFragment<FragmentGamesListBinding>(
    FragmentGamesListBinding::inflate,
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonBonusGame.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)
            buttonPlay1.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)
            buttonPlay2.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)

            val controller = findNavController()
            imageViewBack.setOnClickListener {
                controller.navigateSafe(
                    GamesListFragmentDirections.actionGamesListFragmentToMainFragment(),
                )
            }
            imageViewExit.setOnClickListener {
                requireActivity().finish()
            }
            buttonPlay1.setOnClickListener {
                controller.navigateSafe(
                    GamesListFragmentDirections.actionGamesListFragmentToGame1Fragment(),
                )
            }
            buttonPlay2.setOnClickListener {
                controller.navigateSafe(
                    GamesListFragmentDirections.actionGamesListFragmentToGame2Fragment()
                )
            }
            buttonBonusGame.setOnClickListener {
                controller.navigateSafe(
                    GamesListFragmentDirections.actionGamesListFragmentToBonusGameFragment()
                )
            }
        }
    }
}
