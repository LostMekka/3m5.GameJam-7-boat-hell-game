package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import de.lostmekka.gamejam.boathell.entity.addEntityWithComponents
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipWaterComp
import de.lostmekka.gamejam.boathell.entity.component.WaterParticlesComponent
import de.lostmekka.gamejam.boathell.pixels
import ktx.ashley.allOf

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
        for (entity in entities) {
            val comp = WaterParticlesComponent.mapper[entity]
            if (comp.color != batch.color) batch.color = comp.color
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
            val pos = TransformComponent.mapper[entity]
            val water = ShipWaterComp.mapper[entity]
            val off = Vector2(water.off).rotate(pos.rotation)
            val spawnPos = Vector2(pos.x + off.x, pos.y + off.y)
            val vel = Vector2(0.1f, 0f).rotate(pos.rotation + dir)
            engine.addEntityWithComponents(WaterParticlesComponent(spawnPos, vel, Color(0.7f, 0.9f, 1f, 1f)))
        }
    }

    override fun familyBuilder() = allOf(
        ShipWaterComp::class,
        TransformComponent::class
    )
}

fun createExplosion(entity: Entity, engine: Engine) {
    val posComp = TransformComponent.mapper[entity]
    val pos = Vector2(posComp.x, posComp.y)
    for (i in 0..40) {
        val vel = Vector2(MathUtils.random(0.2f, 1.5f), 0f).rotate(MathUtils.random(360f))
        engine.addEntityWithComponents(
            WaterParticlesComponent(Vector2(pos), vel, Color(1.0f, 0.6f + 0.3f * MathUtils.random(), 0f, 1f))
        )
    }
}
