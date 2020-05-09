package de.lostmekka.gamejam.boathell.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

object Sounds {
    var volume = 0.4f
//    val xxx by lazy { sound("xxx.ogg", 0.35f) }
}

class SoundWithVolume(val sound: Sound, val volume: Float) {
    fun play() = sound.play(volume * Sounds.volume)
}

fun sound(path: String, volume: Float = 1f) = SoundWithVolume(
    Gdx.audio.newSound(Gdx.files.internal("sounds/$path")),
    volume
)
