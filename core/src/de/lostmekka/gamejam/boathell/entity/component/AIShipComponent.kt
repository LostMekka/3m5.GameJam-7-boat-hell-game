package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import de.lostmekka.gamejam.boathell.entity.AIMovementStrategy
import ktx.ashley.get
import ktx.ashley.mapperFor

class AIShipComponent(
    var movementStrategy: AIMovementStrategy
) : Component {
    companion object {
        val mapper = mapperFor<AIShipComponent>()
    }

    fun firePattern(me: Entity) {
        val owner = me[WeaponOwnerComponent.mapper]
        if (owner != null) {
            for (weapon in owner.weaponComponents) {
                weapon.shoot()
            }
        }
    }
}
