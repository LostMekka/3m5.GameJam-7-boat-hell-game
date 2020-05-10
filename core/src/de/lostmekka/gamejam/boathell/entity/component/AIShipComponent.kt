package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.World
import de.lostmekka.gamejam.boathell.entity.AIMovementStrategy
import ktx.ashley.get
import ktx.ashley.mapperFor

class AIShipComponent(
    var movementStrategy: AIMovementStrategy
) : Component {
    companion object {
        val mapper = mapperFor<AIShipComponent>()
    }

    fun firePattern(me: Entity, engine: Engine, physicsWorld: World) {
        val owner = me[WeaponOwnerComponent.mapper]
        if (owner != null) {
            for (weapon in owner.weaponComponents) {
                weapon.shoot(me, engine, physicsWorld)
            }
        }
    }
}
