package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

typealias ProjectileMovementStrategy = ProjectileMovementStrategyContext.() -> Unit

data class ProjectileMovementStrategyContext(
    val pos: PositionComponent,
    val deltaTime: Float,
    val totalLifetime: Float
)

class ProjectileMovementComponent(
    var maxLifeTime: Float,
    var lifeTime: Float = 0f,
    var movementStrategy: ProjectileMovementStrategy
) : Component {
    companion object {
        val mapper = mapperFor<ProjectileMovementComponent>()
    }
}
