package com.fairyfo.frenzy.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.fairyfo.frenzy.databinding.FragmentSettingsBinding
import com.fairyfo.frenzy.ui.core.ViewBindingFragment
import com.fairyfo.frenzy.ui.extensions.addOnBackPressedCallback
import com.fairyfo.frenzy.ui.extensions.navigateSafe
import com.fairyfo.frenzy.utils.SharedPrefs

class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate,
) {

    private val prefs by lazy {
        SharedPrefs.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            imageViewBack.setOnClickListener {
                findNavController().navigateSafe(
                    SettingsFragmentDirections.actionSettingsFragmentToMainFragment()
                )
            }
            progressBarMusic.progress = prefs.soundLevel
            progressBarVibration.progress = prefs.vibratingLevel

            progressBarMusic.setOnChangeListener {
                prefs.soundLevel = it
            }

            progressBarVibration.setOnChangeListener {
                prefs.vibratingLevel = it
            }
        }
        addOnBackPressedCallback {
            findNavController().navigateSafe(
                SettingsFragmentDirections.actionSettingsFragmentToMainFragment()
            )
        }
    }
}
