package com.fairyfo.frenzy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fairyfo.frenzy.databinding.ActivityMainBinding
import com.fairyfo.frenzy.ui.extensions.navigateSafe
import com.fairyfo.frenzy.utils.SharedPrefs
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class OpenActivity : AppCompatActivity() {

    private val navController: NavController
        get() = binding.fragmentContainer.getFragment<NavHostFragment>().navController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (SharedPrefs.getInstance(this).isUserSignedIn) {
            navController.popBackStack()
            navController.navigateSafe(R.id.mainFragment)
        }
    }
}