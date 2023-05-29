package com.fairyfo.frenzy.ui.fragments

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.fairyfo.frenzy.R
import com.fairyfo.frenzy.databinding.FragmentPhoneLoginBinding
import com.fairyfo.frenzy.ui.core.ViewBindingFragment
import com.fairyfo.frenzy.ui.extensions.addOnBackPressedCallback
import com.fairyfo.frenzy.ui.extensions.navigateSafe
import com.fairyfo.frenzy.ui.extensions.setTextGradient
import com.fairyfo.frenzy.utils.SharedPrefs
import me.ibrahimsn.lib.PhoneNumberKit

class PhoneLoginFragment : ViewBindingFragment<FragmentPhoneLoginBinding>(
    FragmentPhoneLoginBinding::inflate,
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonContinue.setTextGradient(R.color.gold, R.color.white_86, R.color.gold)

            val phoneNumberKit = PhoneNumberKit.Builder(requireContext())
                .setIconEnabled(true)
                .build()
            val telephonyManager =
                requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            phoneNumberKit.attachToInput(
                textInput,
                telephonyManager.simCountryIso,
            )

            buttonContinue.setOnClickListener {
                if (phoneNumberKit.isValid) {
                    SharedPrefs.getInstance(requireActivity()).isUserSignedIn = true
                    findNavController().navigateSafe(
                        PhoneLoginFragmentDirections.actionPhoneLoginFragmentToMainFragment(),
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.phone_number_is_not_valid,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

            addOnBackPressedCallback {
                findNavController().navigateSafe(
                    PhoneLoginFragmentDirections.actionPhoneLoginFragmentToLoginFragment()
                )
            }
        }
    }
}
