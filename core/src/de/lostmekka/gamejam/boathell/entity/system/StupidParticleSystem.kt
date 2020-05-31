package de.lostmekka.gamejam.boathell.entity.system

import de.lostmekka.gamejam.boathell.entity.component.RenderComponent
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
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
