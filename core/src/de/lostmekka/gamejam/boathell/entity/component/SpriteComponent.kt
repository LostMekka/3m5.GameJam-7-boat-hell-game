package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import ktx.ashley.mapperFor

class SpriteComponent(
    var sprite: Sprite,
    val width: Float = 5f,
    val height: Float = 5f
): Component {
    companion object {
        val mapper = mapperFor<SpriteComponent>()
    }
}
