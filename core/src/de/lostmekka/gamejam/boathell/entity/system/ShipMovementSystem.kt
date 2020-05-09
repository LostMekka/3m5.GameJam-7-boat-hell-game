package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import ktx.ashley.allOf
import ktx.ashley.get
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ShipMovementSystem : BaseSystem() {

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val pos = entity[PositionComponent.mapper]!!
            val vel = entity[ShipMovementComponent.mapper]!!
            if (vel.velocity > 0) {
                val alpha = pos.rotation / 180 * PI.toFloat()
                pos.y += cos(alpha) * vel.velocity
                pos.x -= sin(alpha) * vel.velocity
            }
        }
    }

    override fun familyBuilder() = allOf(
        PositionComponent::class,
        ShipMovementComponent::class
    )
}
