package de.lostmekka.gamejam.boathell.asset

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

// TODO: use asset manager?

object Textures {
    val boat1 by lazy {
        Texture("boat1.png")
            .filterNearest()
    }
    val projectile by lazy {
        Texture("projectile1.png")
            .filterNearest()
            .splitSpriteSheet(8, 8, 2, 1)
    }
}

fun Texture.filterNearest(): Texture {
    setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
    return this
}

fun Texture.splitSpriteSheet(
    spriteWidth: Int,
    spriteHeight: Int,
    countX: Int,
    countY: Int
): List<TextureRegion> = TextureRegion
    .split(this, spriteWidth, spriteHeight)
    .take(countY)
    .flatMap { it.take(countX) }
