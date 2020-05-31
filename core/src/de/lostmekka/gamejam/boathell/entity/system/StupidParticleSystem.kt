package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.entity.addEntityWithComponents
import de.lostmekka.gamejam.boathell.entity.component.RenderComponent
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipWaterComp
import de.lostmekka.gamejam.boathell.entity.component.WaterParticlesComponent
import ktx.ashley.allOf
import ktx.ashley.get
import kotlin.math.min

class StupidParticleSystem : BaseSystem() {
    override fun familyBuilder() = allOf(
        WaterParticlesComponent::class,
        TransformComponent::class
    )

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val particle = WaterParticlesComponent.mapper[entity]
            val trans = TransformComponent.mapper[entity]

            trans.x += particle.vel.x * deltaTime
            trans.y += particle.vel.y * deltaTime
            particle.nextFrame -= deltaTime

            while (particle.nextFrame < 0f) {
                particle.nextFrame += particle.nextFrameTime
                particle.frame++
            }

            val render = entity.get(RenderComponent.mapper)
            if (render != null) {
                render.texRegion = particle.regions[min(particle.frame, particle.regions.lastIndex)]
                render.color = particle.color
            }
        }

        for (entity in entities) {
            val particle = WaterParticlesComponent.mapper[entity]
            if (particle.frame > particle.regions.lastIndex) {
                engine.removeEntity(entity)
            }
        }
    }
}

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
            engine.addEntityWithComponents(
                WaterParticlesComponent(spawnPos, vel, Color(0.7f, 0.9f, 1f, 1f)),
                RenderComponent(Textures.explosion1[0], 90),
                TransformComponent(spawnPos.x, spawnPos.y, 0f)
            )
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
            WaterParticlesComponent(Vector2(pos), vel, Color(1.0f, 0.6f + 0.3f * MathUtils.random(), 0f, 1f)),
            RenderComponent(Textures.explosion1[0], 91),
            TransformComponent(pos.x, pos.y, 0f)
        )
    }
}
