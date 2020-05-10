package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class HealthComponent(
    val maxHealth: Float,
    var health: Float = maxHealth
) : Component {
    companion object {
        val mapper = mapperFor<HealthComponent>()
    }
}
