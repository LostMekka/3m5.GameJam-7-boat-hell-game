package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class AIShipComponent(
    var movementStrategy: PositionComponent.(MovementStrategyContext) -> Unit
) : Component {
    data class MovementStrategyContext(
        val playerPos: PositionComponent,
        val shipPos: PositionComponent
    )

    companion object {
        val mapper = mapperFor<AIShipComponent>()
    }
}
