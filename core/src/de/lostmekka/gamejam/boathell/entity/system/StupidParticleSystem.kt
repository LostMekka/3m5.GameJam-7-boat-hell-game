package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import de.lostmekka.gamejam.boathell.cosDeg
import de.lostmekka.gamejam.boathell.entity.addEntityWithComponents
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipWaterComp
import de.lostmekka.gamejam.boathell.entity.component.WaterParticlesComponent
import de.lostmekka.gamejam.boathell.pixels
import de.lostmekka.gamejam.boathell.sinDeg
import ktx.ashley.allOf
import kotlin.math.sign
import kotlin.random.Random

class StupidParticleSystem : BaseSystem() {

    override fun update(dt: Float) {
        for (entity in entities) {
            val comp = WaterParticlesComponent.mapper[entity]
            comp.animate(dt)
        }

        for (entity in entities) {
            val comp = WaterParticlesComponent.mapper[entity]
            if (comp.dead()) {
                engine.removeEntity(entity)
            }
        }
    }

    fun draw(batch: SpriteBatch) {
        batch.color = Color(0.7f, 0.9f, 1f, 1f)
        for (entity in entities) {
            val comp = WaterParticlesComponent.mapper[entity]
            batch.draw(comp.regions[comp.frame], comp.pos.x, comp.pos.y, 16.pixels, 16.pixels)
        }
    }

    override fun familyBuilder() = allOf(
        WaterParticlesComponent::class
    )
}

class StupidWaterSpawn : BaseSystem() {
    var dir = 90f

    override fun update(dt: Float) {
        dir = -dir
        for (entity in entities) {
            val pos = PositionComponent.mapper[entity]
            val water = ShipWaterComp.mapper[entity]
            val off = Vector2(water.off).rotate(pos.rotation)
            val spawnPos = Vector2(pos.x + off.x, pos.y + off.y)
            val vel = Vector2(0.1f, 0f).rotate(pos.rotation + dir)
            engine.addEntityWithComponents(WaterParticlesComponent(spawnPos, vel))
        }
    }

    override fun familyBuilder() = allOf(
        ShipWaterComp::class,
        PositionComponent::class
    )
}
