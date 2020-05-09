package de.lostmekka.gamejam.boathell.entity.system

import de.lostmekka.gamejam.boathell.cosDeg
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import de.lostmekka.gamejam.boathell.sinDeg
import ktx.ashley.allOf
import ktx.ashley.get

class ShipMovementSystem : BaseSystem() {

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val pos = entity[PositionComponent.mapper]!!
            val vel = entity[ShipMovementComponent.mapper]!!
            if (vel.velocity > 0) {
                pos.x += cosDeg(pos.rotation) * vel.velocity
                pos.y += sinDeg(pos.rotation) * vel.velocity
            }
        }
    }

    override fun familyBuilder() = allOf(
        PositionComponent::class,
        ShipMovementComponent::class
    )
}
