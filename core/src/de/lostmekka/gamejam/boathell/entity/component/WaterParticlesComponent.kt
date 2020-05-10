package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.cosDeg
import de.lostmekka.gamejam.boathell.entity.addEntityWithComponents
import de.lostmekka.gamejam.boathell.sinDeg
import ktx.ashley.mapperFor

class WaterParticlesComponent(
    var pos: Vector2,
    var vel: Vector2
) : Component {
    var nextFrameTime = 0.1f
    var nextFrame = nextFrameTime
    var frame = 0
    val regions = Textures.explosion1

    fun animate(dt: Float) {
        nextFrame -= dt

        while (nextFrame < 0f) {
            nextFrame += nextFrameTime
            frame++
        }

        pos.mulAdd(vel, dt)
    }

    fun dead() = frame >= regions.size
    fun life() = frame.toFloat() / regions.size.toFloat()

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
