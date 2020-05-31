package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Affine2
import com.badlogic.gdx.math.Vector2
import ktx.ashley.mapperFor

class RenderComponent(
    var texRegion: TextureRegion,
    var zLayer: Int = 100,
    var color: Color = Color.WHITE,
    var transform: Affine2 = Affine2()
) : Component {
    companion object {
        val mapper = mapperFor<RenderComponent>()
    }

    constructor(sprite: Sprite, zLayer: Int = 0)
        : this(sprite, zLayer, Color.WHITE, Affine2().translate(-sprite.originX, -sprite.originY))
}
