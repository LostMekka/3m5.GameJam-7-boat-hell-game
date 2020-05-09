package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import ktx.ashley.mapperFor

class SpriteComponent(
    var sprite: Sprite
): Component {
    companion object {
        val mapper = mapperFor<SpriteComponent>()
    }
}
