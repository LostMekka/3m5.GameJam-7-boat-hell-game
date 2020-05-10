package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.asset.toCenteredSprite
import de.lostmekka.gamejam.boathell.entity.component.*
import de.lostmekka.gamejam.boathell.pixels

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
            WeaponComponent(0.4f, 0.5f - 4f / 32f, 0f, 0f, false, 0f, WeaponTriggerStrategies.boring),
            SpriteComponent(sprite, 1, Textures.cannon1)
        )
    }

    fun addShip1FrontCannon1(engine: Engine): Entity {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        return engine.addEntityWithComponents(
            PositionComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(0.4f, 27f / 32f, 0.0f / 32f, 0f, false, 0f, WeaponTriggerStrategies.boring),
            SpriteComponent(sprite, 3, Textures.cannon1)
        )
    }

    fun addShip1MiddleCannon1(engine: Engine): Entity {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        return engine.addEntityWithComponents(
            PositionComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(3f, 27f / 32f, 0.0f / 32f, 0f, false, 0f, WeaponTriggerStrategies.rosette),
            SpriteComponent(sprite, 3, Textures.cannon1)
        )
    }

    fun addShip1SideCannons(engine: Engine): MutableList<Entity> {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        fun sideCannon(x: Float, y: Float, angle: Float): Entity = engine.addEntityWithComponents(
            PositionComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(0.4f, x, y, angle, false, 0f, WeaponTriggerStrategies.boring),
            SpriteComponent(sprite, 3, Textures.cannon1)
        )

        return mutableListOf(
            sideCannon(10.pixels, 10.pixels, 90f),
            sideCannon(10.pixels, (-10).pixels, -90f),
            sideCannon(3.pixels, 10.pixels, 90f),
            sideCannon(3.pixels, (-10).pixels, -90f),
            sideCannon((-4).pixels, 10.pixels, 90f),
            sideCannon((-4).pixels, (-10).pixels, -90f)
        )
    }
}

typealias WeaponTriggerStrategy = ShotContext.() -> Boolean

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
        true
    }

    val rosette: WeaponTriggerStrategy = {
        val waitTime = 0.227f
        val projectilesFired: Int = (firingTime / waitTime).toInt()
        var projectilesToFire: Int = ((firingTime + deltaTime) / waitTime).toInt() - projectilesFired
        if (projectilesToFire + projectilesFired > 36) projectilesToFire = 36 - projectilesFired
        for (i in 1..projectilesToFire) {
            val angleOffset = (i + projectilesFired) * 20
            engine.addEntityWithComponents(
                PositionComponent(x, y, angle + angleOffset),
                SpriteComponent(Textures.projectile[0].toCenteredSprite(), 999),
                ProjectileMovementComponent(
                    waitTime = i.toFloat() * waitTime,
                    maxLifeTime = 15f,
                    movementStrategy = ProjectileMovementStrategies.straight(angle + angleOffset, 0.5f, movementVelocity)
                )
            )
        }
        projectilesToFire + projectilesFired >= 36
    }
}
