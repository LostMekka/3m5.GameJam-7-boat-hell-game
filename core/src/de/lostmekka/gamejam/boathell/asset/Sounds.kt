package de.lostmekka.gamejam.boathell.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import kotlin.math.max
import kotlin.math.pow

object Sounds {
    var volume = 0.4f
    val awesomeExplosion by lazy { sound("awesome_explosion.wav", 0.5f) }
    val hit by lazy { sound("hit1.wav", 0.35f) }
    val shoot by lazy { sound("shoot.wav", 0.35f) }
}

class SoundWithVolume(val sound: Sound, val volume: Float) {
    fun play(dist: Float = 0f) = sound.play(volume * Sounds.volume * (max(50f - dist, 0f) / 50f).pow(2))
    fun playDirect(volume: Float) = sound.play(this.volume * volume)
}

fun sound(path: String, volume: Float = 1f) = SoundWithVolume(
    Gdx.audio.newSound(Gdx.files.internal("sound/$path")),
    volume
)
