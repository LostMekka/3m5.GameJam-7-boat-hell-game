package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class ProjectileMovementComponent(
    var maxLifeTime: Float,
    var lifeTime: Float = 0f,
    var movementStrategy: PositionComponent.(MovementStrategyContext) -> Unit
) : Component {
    data class MovementStrategyContext(
        val deltaTime: Float,
        val totalLifetime: Float
    )
    companion object {
        val mapper = mapperFor<ProjectileMovementComponent>()
    }
}
