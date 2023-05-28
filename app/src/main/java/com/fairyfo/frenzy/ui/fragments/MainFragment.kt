package com.fairyfo.frenzy.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fairyfo.frenzy.R
import com.fairyfo.frenzy.databinding.FragmentMainBinding
import com.fairyfo.frenzy.ui.core.ViewBindingFragment
import com.fairyfo.frenzy.ui.extensions.navigateSafe
import com.fairyfo.frenzy.ui.extensions.setTextGradient
import com.fairyfo.frenzy.ui.fragments.game1.Game1ViewModel

class MainFragment : ViewBindingFragment<FragmentMainBinding>(
    FragmentMainBinding::inflate,
) {
    // TODO Add privacy
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonPlay.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)
            buttonSettings.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)
            buttonPrivacy.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)

            val controller = findNavController()
            buttonPlay.setOnClickListener {
                controller.navigateSafe(
                    MainFragmentDirections.actionMainFragmentToGamesListFragment(),
                )
            }
            buttonSettings.setOnClickListener {
                controller.navigateSafe(
                    MainFragmentDirections.actionMainFragmentToSettingsFragment(),
                )
            }
            buttonPrivacy.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
                startActivity(browserIntent)
            }
            imageViewBack.setOnClickListener {
                controller.navigateSafe(
                    MainFragmentDirections.actionMainFragmentToLoginFragment(),
                )
            }
            imageViewExit.setOnClickListener {
                requireActivity().finish()
            }
        }
    }
}
