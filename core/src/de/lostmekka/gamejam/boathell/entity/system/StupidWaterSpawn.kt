package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.math.Vector2
import de.lostmekka.gamejam.boathell.entity.addWaterSplash
import de.lostmekka.gamejam.boathell.entity.component.ShipWaterComp
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import ktx.ashley.allOf

class StupidWaterSpawn : BaseSystem() {
    var dir = 90f

    override fun update(deltaTime: Float) {
        dir = -dir
        for (entity in entities) {
            val pos = TransformComponent.mapper[entity]
            val water = ShipWaterComp.mapper[entity]
            val off = Vector2(water.off).rotate(pos.rotation)
            val spawnPos = Vector2(pos.x + off.x, pos.y + off.y)
            val vel = Vector2(0.1f, 0f).rotate(pos.rotation + dir)
            engine.addWaterSplash(spawnPos, vel)
        }
    }

    override fun familyBuilder() = allOf(
            ShipWaterComp::class,
            TransformComponent::class
    )
}
