package de.lostmekka.gamejam.boathell.asset

import com.badlogic.gdx.Gdx

object Music {
//    val xxx by lazy { music("xxx.ogg") }
}

fun music(path: String, volume: Float = 1f) =
    Gdx.audio.newMusic(Gdx.files.internal("music/$path")).also {
        it.volume = volume
        it.isLooping = true
    }
