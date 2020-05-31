package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import de.lostmekka.gamejam.boathell.asset.Textures
import ktx.ashley.mapperFor

class WaterParticlesComponent(
    var pos: Vector2,
    var vel: Vector2,
    var color: Color
) : Component {
    var nextFrameTime = 0.1f
    var nextFrame = nextFrameTime
    var frame = 0
    val regions = Textures.explosion1

    companion object {
        val mapper = mapperFor<WaterParticlesComponent>()
    }
}

class ShipWaterComp(
    val off: Vector2
) : Component {
    companion object {
        val mapper = mapperFor<ShipWaterComp>()
    }
}
