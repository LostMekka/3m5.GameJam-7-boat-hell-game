package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import de.lostmekka.gamejam.boathell.entity.addEntityWithComponents
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import de.lostmekka.gamejam.boathell.entity.component.ProjectileMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.ProjectileMovementStrategyContext
import de.lostmekka.gamejam.boathell.entity.component.WaterParticlesComponent
import ktx.ashley.allOf

class ProjectileMovementSystem : BaseSystem() {
    override fun updateEntity(entity: Entity, deltaTime: Float) {
        val pos = TransformComponent.mapper[entity]
        val mov = ProjectileMovementComponent.mapper[entity]
        if (mov.waitTime > 0) {
            mov.waitTime -= deltaTime
        } else {
            mov.lifeTime += deltaTime
            mov.movementStrategy(ProjectileMovementStrategyContext(pos, deltaTime, mov.lifeTime))
            if (mov.lifeTime > mov.maxLifeTime) {
                engine.removeEntity(entity)

                engine.addEntityWithComponents(
                    WaterParticlesComponent(Vector2(pos.x, pos.y), Vector2.Zero, Color(0.7f, 0.9f, 1f, 1f))
                )
            }
            // TODO: check hit
        }
    }

    override fun familyBuilder() = allOf(
        TransformComponent::class,
        ProjectileMovementComponent::class
    )
}
