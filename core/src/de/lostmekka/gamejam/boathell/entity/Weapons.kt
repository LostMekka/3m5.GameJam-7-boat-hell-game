package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
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
    fun addBoatFrontCannon1(engine: Engine, isPlayerWeapon: Boolean): Entity {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        return engine.addEntityWithComponents(
            PositionComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(
                cooldownTime = 0.4f,
                offsetX = 0.5f - 4f / 32f,
                offsetY = 0f,
                offsetAngle = 0f,
                isFiring = false,
                firingTime = 0f,
                projectileInit = WeaponTriggerStrategies.boring(isPlayerWeapon)
            ),
            SpriteComponent(sprite, 1, Textures.cannon1)
        )
    }

    fun addShip1FrontCannon1(engine: Engine, isPlayerWeapon: Boolean): Entity {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        return engine.addEntityWithComponents(
            PositionComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(
                cooldownTime = 0.4f,
                offsetX = 27f / 32f,
                offsetY = 0.0f / 32f,
                offsetAngle = 0f,
                isFiring = false,
                firingTime = 0f,
                projectileInit = WeaponTriggerStrategies.boring(isPlayerWeapon)
            ),
            SpriteComponent(sprite, 3, Textures.cannon1)
        )
    }

    fun addShip1MiddleCannon1(engine: Engine, isPlayerWeapon: Boolean): Entity {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        return engine.addEntityWithComponents(
            PositionComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(
                cooldownTime = 3.6f,
                offsetX = 27f / 32f,
                offsetY = 0.0f / 32f,
                offsetAngle = 0f,
                isFiring = false,
                firingTime = 0f,
                projectileInit = WeaponTriggerStrategies.rosette(isPlayerWeapon)
            ),
            SpriteComponent(sprite, 3, Textures.cannon1)
        )
    }

    fun addShip1SideCannons(engine: Engine, isPlayerWeapon: Boolean): MutableList<Entity> {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        fun sideCannon(x: Float, y: Float, angle: Float): Entity = engine.addEntityWithComponents(
            PositionComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(
                cooldownTime = 0.4f,
                offsetX = x,
                offsetY = y,
                offsetAngle = angle,
                isFiring = false,
                firingTime = 0f,
                projectileInit = WeaponTriggerStrategies.boring(isPlayerWeapon)
            ),
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
    fun boring(isPlayerWeapon: Boolean): WeaponTriggerStrategy = {
        for (i in 0..5) {
            engine.addEntityWithComponents(
                PositionComponent(x, y, angle),
                SpriteComponent(Textures.projectile[0].toCenteredSprite().apply { color = com.badlogic.gdx.graphics.Color.YELLOW }, 999),
                HitBoxComponent(
                    physicsWorld = physicsWorld,
                    hitBoxWidth = 4.pixels,
                    hitBoxHeight = 4.pixels,
                    hitBoxRotation = 0f,
                    category = if (isPlayerWeapon) HitBoxCategory.PlayerProjectile else HitBoxCategory.EnemyProjectile
                ),
                ProjectileMovementComponent(
                    damage = 1f,
                    waitTime = i.toFloat() * 0.016f,
                    maxLifeTime = 3f,
                    movementStrategy = ProjectileMovementStrategies.straight(angle, 3f, movementVelocity)
                )
            )
        }
        true
    }

    fun rosette(isPlayerWeapon: Boolean): WeaponTriggerStrategy = {
        val waitTime = 0.3f
        val projectilesFired: Int = (firingTime / waitTime).toInt()
        var projectilesToFire: Int = ((firingTime + deltaTime) / waitTime).toInt() - projectilesFired
        if (projectilesToFire + projectilesFired > 36) projectilesToFire = 36 - projectilesFired
        for (i in 1..projectilesToFire * 3) {
            val angleOffset = (i + projectilesFired) * 30
            engine.addEntityWithComponents(
                PositionComponent(x, y, angle + angleOffset),
                SpriteComponent(Textures.projectile[0].toCenteredSprite().apply { color = Color.RED }, 999),
                HitBoxComponent(
                    physicsWorld = physicsWorld,
                    hitBoxWidth = 4.pixels,
                    hitBoxHeight = 4.pixels,
                    hitBoxRotation = 0f,
                    category = if (isPlayerWeapon) HitBoxCategory.PlayerProjectile else HitBoxCategory.EnemyProjectile
                ),
                ProjectileMovementComponent(
                    waitTime = i.toFloat() * waitTime,
                    maxLifeTime = 15f,
                    damage = 1f,
                    movementStrategy = ProjectileMovementStrategies.straight(angle + angleOffset, 0.5f, movementVelocity)
                )
            )
        }
        projectilesToFire + projectilesFired >= 36
    }
}
