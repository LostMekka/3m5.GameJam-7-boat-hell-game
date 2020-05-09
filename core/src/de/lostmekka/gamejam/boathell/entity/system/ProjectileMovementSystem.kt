package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Entity
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ProjectileMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.ProjectileMovementStrategyContext
import ktx.ashley.allOf

class ProjectileMovementSystem : BaseSystem() {
    override fun updateEntity(entity: Entity, deltaTime: Float) {
        val pos = PositionComponent.mapper[entity]
        val mov = ProjectileMovementComponent.mapper[entity]
        mov.lifeTime += deltaTime
        mov.movementStrategy(ProjectileMovementStrategyContext(pos, deltaTime, mov.lifeTime))
        // TODO: despawn after max lifetime
        // TODO: check hit
    }

    override fun familyBuilder() = allOf(
        PositionComponent::class,
        ProjectileMovementComponent::class
    )
}
