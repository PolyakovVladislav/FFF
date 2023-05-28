package com.fairyfo.frenzy.ui.extensions

import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fairyfo.frenzy.utils.SharedPrefs
import java.io.IOException

internal inline fun Fragment.addOnBackPressedCallback(
    crossinline onBackPressed: () -> Unit,
) {
    requireActivity().onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        },
    )
}

fun Fragment.playWinSound() {
    val assetManager = requireActivity().assets
    val mediaPlayer = MediaPlayer()
    val volume = SharedPrefs.getInstance(requireActivity()).soundLevel / 100f
    mediaPlayer.setVolume(volume, volume)

    try {
        val afd = assetManager.openFd("win.wav")
        mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        mediaPlayer.isLooping = false
        mediaPlayer.prepare()
        mediaPlayer.start()
    } catch (ex: IOException) {
        Log.e("MediaPlayer", ex.localizedMessage, ex)
    }
}

@Suppress("DEPRECATION")
fun Fragment.vibrate(duration: Long = 150L) {
    val v = ContextCompat.getSystemService(requireContext(), Vibrator::class.java)
    if (v?.hasVibrator() == true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(
                VibrationEffect.createOneShot(
                    duration,
                    (255 * SharedPrefs.getInstance(requireActivity()).vibratingLevel / 100f).toInt(),
                ),
            )
        } else {
            v.vibrate((duration * SharedPrefs.getInstance(requireActivity()).vibratingLevel / 100f).toLong())
        }
    }
}
