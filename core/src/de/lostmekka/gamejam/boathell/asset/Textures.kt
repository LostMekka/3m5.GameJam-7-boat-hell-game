package de.lostmekka.gamejam.boathell.asset

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

// TODO: use asset manager?

object Textures {
    val boat1 by lazy {
        Texture("boat1.png")
            .filterNearest()
    }
}

fun Texture.filterNearest(): Texture {
    setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
    return this
}

fun Texture.splitSpriteSheet(spriteWidth: Int, spriteHeight: Int): List<TextureRegion> {
    val array = TextureRegion.split(this, spriteWidth, spriteHeight)
    val sprites = mutableListOf<TextureRegion>()
    for (row in array) for (sprite in row) sprites += sprite
    return sprites
}
