package com.fairyfo.frenzy.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.fairyfo.frenzy.R
import com.fairyfo.frenzy.databinding.FragmentLoginBinding
import com.fairyfo.frenzy.ui.core.ViewBindingFragment
import com.fairyfo.frenzy.ui.extensions.navigateSafe
import com.fairyfo.frenzy.ui.extensions.setTextGradient
import com.fairyfo.frenzy.utils.SharedPrefs

class LoginFragment : ViewBindingFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate,
) {

    private var option = 2

    private val prefs by lazy {
        SharedPrefs.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonContinue.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)
        option = prefs.signInOption
        setListeners()
        when (option) {
            0 -> onPhoneClicked()
            1 -> onMailClicked()
            2 -> onAnonymousClicked()
        }
    }

    private fun setListeners() {
        with(binding) {
            buttonContinue.setOnClickListener {
                val controller = findNavController()
                when (option) {
                    0 -> {
                        controller.navigateSafe(
                            LoginFragmentDirections.actionLoginFragmentToPhoneLoginFragment(),
                        )
                    }
                    1 -> {
                        controller.navigateSafe(
                            LoginFragmentDirections.actionLoginFragmentToEmailLoginFragment()
                        )
                    }
                    2 -> {
                        controller.navigateSafe(
                            LoginFragmentDirections.actionLoginFragmentToMainFragment()
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
        option = 1
        prefs.signInOption = option
    }

    private fun onPhoneClicked(view: View? = null) {
        binding.imageViewRadioEmail.setUnChecked()
        binding.imageViewRadioAnonymous.setUnChecked()
        binding.imageViewRadioPhone.setChecked()
        option = 0
        prefs.signInOption = option
    }

    private fun onAnonymousClicked(view: View? = null) {
        binding.imageViewRadioPhone.setUnChecked()
        binding.imageViewRadioEmail.setUnChecked()
        binding.imageViewRadioAnonymous.setChecked()
        option = 2
        prefs.signInOption = option
    }

    private fun ImageView.setChecked() {
        setImageResource(R.drawable.ic_radio_checked)
    }

    private fun ImageView.setUnChecked() {
        setImageResource(R.drawable.ic_radio_unchecked)
    }
}
