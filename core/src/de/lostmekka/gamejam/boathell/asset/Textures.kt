package de.lostmekka.gamejam.boathell.asset

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
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
    val ship1 by lazy {
        Texture("ship1.png")
            .filterNearest()
    }
    val cannon1 by lazy {
        Texture("cannon1.png")
            .filterNearest()
            .splitSpriteSheet(8, 8, 2, 1)
    }
    val plane1 by lazy {
        Texture("ship1.png")
            .filterNearest()
    }
}

fun Texture.filterNearest(): Texture {
    setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
    return this
}

fun Texture.toCenteredSprite() = Sprite(this).also { sprite ->
    val w = width / 32f
    val h = height / 32f
    sprite.setBounds(-0.5f * w, -0.5f * h, w, h)
    sprite.setOriginCenter()
}

fun TextureRegion.toCenteredSprite() = Sprite(this).also { sprite ->
    val w = regionWidth / 32f
    val h = regionHeight / 32f
    sprite.setBounds(-0.5f * w, -0.5f * h, w, h)
    sprite.setOriginCenter()
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
