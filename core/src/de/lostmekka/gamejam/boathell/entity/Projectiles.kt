package de.lostmekka.gamejam.boathell.entity

import com.badlogic.gdx.math.Vector3
import de.lostmekka.gamejam.boathell.cosDeg
import de.lostmekka.gamejam.boathell.entity.component.ProjectileMovementStrategy
import de.lostmekka.gamejam.boathell.sinDeg

object ProjectileMovementStrategies {
    fun straight(
        startAngle: Float,
        speed: Float,
        movementVelocity: Vector3
    ): ProjectileMovementStrategy = {
        pos.x += (cosDeg(startAngle) * speed + movementVelocity.x) * deltaTime
        pos.y += (sinDeg(startAngle) * speed + movementVelocity.y) * deltaTime
    }
}
