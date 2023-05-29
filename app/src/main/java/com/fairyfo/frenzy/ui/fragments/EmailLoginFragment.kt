package com.fairyfo.frenzy.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.fairyfo.frenzy.R
import com.fairyfo.frenzy.databinding.FragmentEmailLoginBinding
import com.fairyfo.frenzy.ui.core.ViewBindingFragment
import com.fairyfo.frenzy.ui.extensions.addOnBackPressedCallback
import com.fairyfo.frenzy.ui.extensions.navigateSafe
import com.fairyfo.frenzy.ui.extensions.setTextGradient
import com.fairyfo.frenzy.utils.SharedPrefs

class EmailLoginFragment : ViewBindingFragment<FragmentEmailLoginBinding>(
    FragmentEmailLoginBinding::inflate,
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonContinue.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)
            buttonContinue.setOnClickListener {
                if (editText.text?.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")) == true) {
                    SharedPrefs.getInstance(requireActivity()).isUserSignedIn = true
                    findNavController().navigateSafe(
                        EmailLoginFragmentDirections.actionEmailLoginFragmentToMainFragment(),
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.email_is_not_valid,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
            addOnBackPressedCallback {
                findNavController().navigateSafe(
                    EmailLoginFragmentDirections.actionEmailLoginFragmentToLoginFragment()
                )
            }
        }
    }
}
