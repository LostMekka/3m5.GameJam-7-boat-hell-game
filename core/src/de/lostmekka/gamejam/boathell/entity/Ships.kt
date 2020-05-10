package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.asset.toCenteredSprite
import de.lostmekka.gamejam.boathell.entity.component.AIMovementStrategy
import de.lostmekka.gamejam.boathell.entity.component.AIShipComponent
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent
import de.lostmekka.gamejam.boathell.entity.component.WeaponOwnerComponent
import de.lostmekka.gamejam.boathell.normalizeAngleDeg
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.abs
import kotlin.math.sign
import kotlin.random.Random

object Ships {
    fun addPlayerBoat(engine: Engine): Entity {
        return engine.addEntityWithComponents(
            PositionComponent(x = 0f, y = 0f, rotation = Random.nextFloat() * 360f),
            SpriteComponent(Textures.boat1.toCenteredSprite()),
            ShipMovementComponent(),
            PlayerControlledComponent(),
            WeaponOwnerComponent(
                Weapons.addBoatFrontCannon1(engine)
            )
        )
    }

    fun addAIBoat(engine: Engine, x: Float = 0f, y: Float = 0f, rotation: Float = 0f) {
        engine.addEntityWithComponents(
            PositionComponent(x = x, y = y, rotation = rotation),
            SpriteComponent(Textures.ship1.toCenteredSprite(), 2),
            ShipMovementComponent(velocity = 0.025f),
            AIShipComponent(AIShipMovementStrategies.followAndCirculatePlayer())
        )
    }
}

object AIShipMovementStrategies {
    fun followAndCirculatePlayer(): AIMovementStrategy = {
        var targetAngle = atan2(playerPos.y - shipPos.y, playerPos.x - shipPos.x) * 180 / PI.toFloat()
        // if ship is in critical distance -> go around player -> add 90 degree
        val distance = sqrt((playerPos.x - shipPos.x).pow(2) + (playerPos.y - shipPos.y).pow(2))
        if (4 > distance) targetAngle += 90

        val currentAngle = shipPos.rotation
        val angleDifference = normalizeAngleDeg(targetAngle - currentAngle)
        val step = 2
        shipPos.rotation = when {
            abs(angleDifference) <= step -> targetAngle
            else -> shipPos.rotation + sign(angleDifference) * step
        }
    }
}
