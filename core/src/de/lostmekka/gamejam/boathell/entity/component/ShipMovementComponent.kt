package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class ShipMovementComponent(
) : Component {
    companion object {
        val mapper = mapperFor<ShipMovementComponent>()
    }
}
