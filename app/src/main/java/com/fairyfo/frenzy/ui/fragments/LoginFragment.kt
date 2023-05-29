package com.fairyfo.frenzy.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.fairyfo.frenzy.R
import com.fairyfo.frenzy.databinding.FragmentLoginBinding
import com.fairyfo.frenzy.ui.core.ViewBindingFragment
import com.fairyfo.frenzy.ui.extensions.navigateSafe
import com.fairyfo.frenzy.ui.extensions.setTextGradient
import com.fairyfo.frenzy.utils.SharedPrefs
import kotlinx.coroutines.launch

class LoginFragment : ViewBindingFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate,
) {

    private val prefs by lazy {
        SharedPrefs.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonContinue.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)
        setListeners()
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                when (prefs.signInOption) {
                    0 -> onPhoneClicked()
                    1 -> onMailClicked()
                    2 -> onAnonymousClicked()
                }
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            buttonContinue.setOnClickListener {
                val controller = findNavController()
                when (prefs.signInOption) {
                    0 -> {
                        controller.navigateSafe(
                            LoginFragmentDirections.actionLoginFragmentToPhoneLoginFragment(),
                        )
                    }

                    1 -> {
                        controller.navigateSafe(
                            LoginFragmentDirections.actionLoginFragmentToEmailLoginFragment(),
                        )
                    }

                    2 -> {
                        controller.navigateSafe(
                            LoginFragmentDirections.actionLoginFragmentToMainFragment(),
                        )
                    }
                }
            }
            imageViewRadioEmail.setOnClickListener(::onMailClicked)
            textViewMail.setOnClickListener(::onMailClicked)
            imageViewRadioPhone.setOnClickListener(::onPhoneClicked)
            textViewPhone.setOnClickListener(::onPhoneClicked)
            imageViewRadioAnonymous.setOnClickListener(::onAnonymousClicked)
            textViewAnonymous.setOnClickListener(::onAnonymousClicked)
        }
    }

    private fun onMailClicked(view: View? = null) {
        binding.imageViewRadioPhone.setUnChecked()
        binding.imageViewRadioEmail.setChecked()
        binding.imageViewRadioAnonymous.setUnChecked()
        prefs.signInOption = 1
    }

    private fun onPhoneClicked(view: View? = null) {
        binding.imageViewRadioEmail.setUnChecked()
        binding.imageViewRadioAnonymous.setUnChecked()
        binding.imageViewRadioPhone.setChecked()
        prefs.signInOption = 0
    }

    private fun onAnonymousClicked(view: View? = null) {
        binding.imageViewRadioPhone.setUnChecked()
        binding.imageViewRadioEmail.setUnChecked()
        binding.imageViewRadioAnonymous.setChecked()
        prefs.signInOption = 2
    }

    private fun ImageView.setChecked() {
        setImageResource(R.drawable.ic_radio_checked)
    }

    private fun ImageView.setUnChecked() {
        setImageResource(R.drawable.ic_radio_unchecked)
    }
}
