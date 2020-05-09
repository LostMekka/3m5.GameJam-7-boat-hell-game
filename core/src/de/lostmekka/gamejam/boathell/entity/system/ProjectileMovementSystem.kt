package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ProjectileMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.ProjectileMovementComponent.MovementStrategyContext
import ktx.ashley.allOf

class ProjectileMovementSystem : BaseSystem() {
    override fun updateEntity(entity: Entity, deltaTime: Float) {
        val pos = PositionComponent.mapper[entity]
        val mov = ProjectileMovementComponent.mapper[entity]
        mov.lifeTime += deltaTime
        mov.movementStrategy(pos, MovementStrategyContext(deltaTime, mov.lifeTime))
        // TODO: despawn after max lifetime
        // TODO: check hit
    }

    override fun familyBuilder() = allOf(
        PositionComponent::class,
        ProjectileMovementComponent::class
    )
}
