package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.utils.ImmutableArray
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import de.lostmekka.gamejam.boathell.entity.component.WeaponOwnerComponent
import de.lostmekka.gamejam.boathell.normalizeAngleDeg
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import kotlin.math.*

class AIShipSystem : BaseSystem() {
    private lateinit var playerEntities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        playerEntities = engine.getEntitiesFor(allOf(PlayerControlledComponent::class, TransformComponent::class).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val mov = AIShipComponent.mapper[entity]
            mov.movementStrategy(MovementStrategyContext(entity, playerEntities.firstOrNull()))
            mov.firePattern(entity)
        }
    }

    override fun familyBuilder() = allOf(
        AIShipComponent::class,
        TransformComponent::class
    )
}

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

typealias AIMovementStrategy = MovementStrategyContext.() -> Unit

data class MovementStrategyContext(
    val me: Entity,
    val target: Entity?
)

object AIShipMovementStrategies {
    fun followAndCirculatePlayer(criticalDistance: Float = 4f): AIMovementStrategy = {
        val playerPos = target?.get(TransformComponent.mapper)
        val shipPos = TransformComponent.mapper[me]
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
        val playerPos = target?.get(TransformComponent.mapper)
        val shipPos = TransformComponent.mapper[me]
        if (target != null && playerPos != null && shipPos != null) {
            var targetAngle = atan2(playerPos.y - shipPos.y, playerPos.x - shipPos.x) * 180 / PI.toFloat()
            val distance = sqrt((playerPos.x - shipPos.x).pow(2) + (playerPos.y - shipPos.y).pow(2))
            val currentAngle = shipPos.rotation
            var angleDifference = normalizeAngleDeg(targetAngle - currentAngle)
            // if ship is in critical distance -> turn around -> add 180 degree
            if (4 > distance) targetAngle += 180
            // if ship is on retreat -> add 180 degree (continue retreat)
            else if (12 > distance && (-160 > angleDifference || angleDifference > 160)) targetAngle += 180

            angleDifference = normalizeAngleDeg(targetAngle - currentAngle)
            val step = 2
            shipPos.rotation = when {
                abs(angleDifference) <= step -> targetAngle
                else -> shipPos.rotation + sign(angleDifference) * step
            }
        }
    }
}
