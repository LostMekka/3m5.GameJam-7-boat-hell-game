package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.ashley.mapperFor

class SpriteComponent(
    var sprite: Sprite,
    var zLayer: Int = 0,
    var regions: List<TextureRegion> = ArrayList()
) : Component {
    companion object {
        val mapper = mapperFor<SpriteComponent>()
    }
}
