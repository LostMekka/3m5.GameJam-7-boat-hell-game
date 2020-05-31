package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.entity.system.RenderComponent
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
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
