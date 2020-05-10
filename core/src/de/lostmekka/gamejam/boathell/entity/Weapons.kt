package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.asset.toCenteredSprite
import de.lostmekka.gamejam.boathell.entity.component.*

// engine = engine,
// cooldownTime = 1f,
// offsetAngle = y * 90f,
// offsetX = x * 0.3f,
// offsetY = y * 0.5f,

object Weapons {
    fun addBoatFrontCannon1(engine: Engine): Entity {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        return engine.addEntityWithComponents(
            PositionComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(0.4f, 0.5f - 4f/32f, 0f, 0f, WeaponTriggerStrategies.boring),
            SpriteComponent(sprite, 1, Textures.cannon1)
        )
    }

    fun addShip1FrontCannon1(engine: Engine): Entity {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        return engine.addEntityWithComponents(
            PositionComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(0.4f, 27f/32f, 0.0f/32f, 0f, WeaponTriggerStrategies.boring),
            SpriteComponent(sprite, 3, Textures.cannon1)
        )
    }

    fun addWeapon(
        engine: Engine,
        offsetX: Float,
        offsetY: Float,
        offsetAngle: Float,
        cooldownTime: Float,
        projectileInit: WeaponTriggerStrategy
    ) = engine.addEntityWithComponents(
        PositionComponent(0f, 0f, 0f), // will be auto set by weapon owner system
        WeaponComponent(cooldownTime, offsetX, offsetY, offsetAngle, projectileInit)
        // TODO: add sprite
    )
}

typealias WeaponTriggerStrategy = ShotContext.() -> Unit

object WeaponTriggerStrategies {
    val boring: WeaponTriggerStrategy = {
        for (i in 0..5) {
            engine.addEntityWithComponents(
                PositionComponent(x, y, angle),
                SpriteComponent(Textures.projectile[0].toCenteredSprite(), 999),
                ProjectileMovementComponent(
                    waitTime = i.toFloat() * 0.016f,
                    maxLifeTime = 3f,
                    movementStrategy = ProjectileMovementStrategies.straight(angle, 3f, movementVelocity)
                )
            )
        }
    }
}
