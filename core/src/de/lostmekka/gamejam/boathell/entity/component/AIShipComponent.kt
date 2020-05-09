package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

typealias AIMovementStrategy = MovementStrategyContext.() -> Unit

data class MovementStrategyContext(
    val playerPos: PositionComponent,
    val shipPos: PositionComponent
)

class AIShipComponent(
    var movementStrategy: AIMovementStrategy
) : Component {
    companion object {
        val mapper = mapperFor<AIShipComponent>()
    }
}
