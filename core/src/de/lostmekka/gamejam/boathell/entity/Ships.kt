package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.World
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.asset.toCenteredSprite
import de.lostmekka.gamejam.boathell.entity.component.AIShipComponent
import de.lostmekka.gamejam.boathell.entity.component.HitBoxComponent
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent
import de.lostmekka.gamejam.boathell.entity.component.WeaponOwnerComponent
import de.lostmekka.gamejam.boathell.normalizeAngleDeg
import de.lostmekka.gamejam.boathell.pixels
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.abs
import kotlin.math.sign
import kotlin.random.Random

object Ships {
    fun addPlayerBoat(engine: Engine, physicsWorld: World): Entity {
        return engine.addEntityWithComponents(
            PositionComponent(x = 0f, y = 0f, rotation = Random.nextFloat() * 360f),
            SpriteComponent(Textures.boat1.toCenteredSprite()),
            HitBoxComponent(
                physicsWorld = physicsWorld,
                hitBoxWidth = 28.pixels,
                hitBoxHeight = 14.pixels,
                hitBoxRotation = 0f
            ),
            ShipMovementComponent(),
            PlayerControlledComponent(),
            WeaponOwnerComponent(
                Weapons.addBoatFrontCannon1(engine)
            )
        )
    }

    fun addAIBoat(engine: Engine, physicsWorld: World, x: Float = 0f, y: Float = 0f, rotation: Float = 0f) {
        engine.addEntityWithComponents(
            PositionComponent(x = x, y = y, rotation = rotation),
            SpriteComponent(Textures.ship1.toCenteredSprite(), 2),
            HitBoxComponent(
                physicsWorld = physicsWorld,
                hitBoxWidth = 2f - 2.pixels,
                hitBoxHeight = 19.pixels,
                hitBoxRotation = 0f
            ),
            ShipMovementComponent(velocity = 0.025f),
            AIShipComponent(AIShipMovementStrategies.followAndCirculatePlayer()),
            WeaponOwnerComponent(
                Weapons.addShip1SideCannons(engine)
            )
        )
    }

    fun addAIPlane(engine: Engine, physicsWorld: World, x: Float = 0f, y: Float = 0f, rotation: Float = 0f) {
        engine.addEntityWithComponents(
            PositionComponent(x = x, y = y, rotation = rotation),
            SpriteComponent(Textures.plane1.toCenteredSprite()),
            HitBoxComponent(
                physicsWorld = physicsWorld,
                hitBoxWidth = 2f - 2.pixels,
                hitBoxHeight = 19.pixels,
                hitBoxRotation = 0f
            ),
            WeaponOwnerComponent(
                Weapons.addBoatFrontCannon1(engine)
            ),
            ShipMovementComponent(velocity = 0.05f),
            AIShipComponent(AIShipMovementStrategies.flyDirectlyToAndAwayFromPlayer())
        )
    }

    fun addAIRosetteShip(engine: Engine, physicsWorld: World, x: Float = 0f, y: Float = 0f, rotation: Float = 0f) {
        engine.addEntityWithComponents(
            PositionComponent(x = x, y = y, rotation = rotation),
            SpriteComponent(Textures.plane1.toCenteredSprite()),
            HitBoxComponent(
                physicsWorld = physicsWorld,
                hitBoxWidth = 2f - 2.pixels,
                hitBoxHeight = 19.pixels,
                hitBoxRotation = 0f
            ),
            WeaponOwnerComponent(
                Weapons.addShip1MiddleCannon1(engine)
            ),
            ShipMovementComponent(velocity = 0.01f),
            AIShipComponent(AIShipMovementStrategies.followAndCirculatePlayer(6f))
        )
    }
}

typealias AIMovementStrategy = MovementStrategyContext.() -> Unit

data class MovementStrategyContext(
    val me: Entity,
    val target: Entity?
)

object AIShipMovementStrategies {
    fun followAndCirculatePlayer(criticalDistance: Float = 4f): AIMovementStrategy = {
        val playerPos = PositionComponent.mapper[target]
        val shipPos = PositionComponent.mapper[me]
        if (target != null && playerPos != null && shipPos != null) {
            var targetAngle = atan2(playerPos.y - shipPos.y, playerPos.x - shipPos.x) * 180 / PI.toFloat()
            // if ship is in critical distance -> go around player -> add 90 degree
            val distance = sqrt((playerPos.x - shipPos.x).pow(2) + (playerPos.y - shipPos.y).pow(2))
            if (criticalDistance > distance) targetAngle += 90

            val currentAngle = shipPos.rotation
            val angleDifference = normalizeAngleDeg(targetAngle - currentAngle)
            val step = 2
            shipPos.rotation = when {
                abs(angleDifference) <= step -> targetAngle
                else -> shipPos.rotation + sign(angleDifference) * step
            }
        }
    }

    fun flyDirectlyToAndAwayFromPlayer(): AIMovementStrategy = {
        val playerPos = PositionComponent.mapper[target]
        val shipPos = PositionComponent.mapper[me]
        if (target != null && playerPos != null && shipPos != null) {
            var targetAngle = atan2(playerPos.y - shipPos.y, playerPos.x - shipPos.x) * 180 / PI.toFloat()
            val distance = sqrt((playerPos.x - shipPos.x).pow(2) + (playerPos.y - shipPos.y).pow(2))
            val currentAngle = shipPos.rotation
            var angleDifference = normalizeAngleDeg(targetAngle - currentAngle)
            // if ship is in critical distance -> turn around -> add 180 degree
            if (4 > distance) targetAngle += 180
            // if ship is on retreat -> add 180 degree (continue retreat)
            else if (8 > distance && (-160 > angleDifference || angleDifference > 160)) targetAngle += 180

            angleDifference = normalizeAngleDeg(targetAngle - currentAngle)
            val step = 2
            shipPos.rotation = when {
                abs(angleDifference) <= step -> targetAngle
                else -> shipPos.rotation + sign(angleDifference) * step
            }
        }
    }
}
