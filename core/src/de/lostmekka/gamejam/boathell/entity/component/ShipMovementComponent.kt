package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class ShipMovementComponent(
    var velocity: Float = 0f
) : Component {
    companion object {
        val mapper = mapperFor<ShipMovementComponent>()
    }
}
