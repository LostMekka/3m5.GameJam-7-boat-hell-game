package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import de.lostmekka.gamejam.boathell.asset.Sounds
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
    fun addBoatFrontCannon1(engine: Engine): MutableList<Entity> {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        val frontCanon = engine.addEntityWithComponents(
            TransformComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(
                cooldownTime = 0.4f,
                offsetX = 0.5f - 4f / 32f,
                offsetY = 0f,
                offsetAngle = 0f,
                isFiring = false,
                firingTime = 0f,
                projectileInit = WeaponTriggerStrategies.fast()
            ),
            RenderComponent(sprite, 1 /*Textures.cannon1*/)
        )

        fun sideCannon(x: Float, y: Float, angle: Float): Entity = engine.addEntityWithComponents(
            TransformComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(
                cooldownTime = 0.4f,
                offsetX = x,
                offsetY = y,
                offsetAngle = angle,
                isFiring = false,
                firingTime = 0f,
                projectileInit = WeaponTriggerStrategies.fast()
            ),
            RenderComponent(sprite, 3 /*Textures.cannon1*/)
        )

        return mutableListOf(
            frontCanon,
            sideCannon(10.pixels, 10.pixels, 45f),
            sideCannon(10.pixels, (-10).pixels, -45f),
            sideCannon(3.pixels, 10.pixels, 45f),
            sideCannon(3.pixels, (-10).pixels, -45f)
        )
    }

    fun addShip1FrontCannon1(engine: Engine): Entity {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        return engine.addEntityWithComponents(
            TransformComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(
                cooldownTime = 0.4f,
                offsetX = 27f / 32f,
                offsetY = 0.0f / 32f,
                offsetAngle = 0f,
                isFiring = false,
                firingTime = 0f,
                projectileInit = WeaponTriggerStrategies.boring()
            ),
            RenderComponent(sprite, 3 /*Textures.cannon1*/)
        )
    }

    fun addShipRosettaCannon(engine: Engine, isPlayerWeapon: Boolean): Entity {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        return engine.addEntityWithComponents(
            TransformComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(
                cooldownTime = 15f,
                offsetX = 27.pixels,
                offsetY = 0f,
                offsetAngle = 0f,
                isFiring = false,
                firingTime = 0f,
                projectileInit = WeaponTriggerStrategies.rosette(isPlayerWeapon)
            ),
            RenderComponent(sprite, 3 /*Textures.cannon1*/)
        )
    }

    fun addShip1SideCannons(engine: Engine): MutableList<Entity> {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        fun sideCannon(x: Float, y: Float, angle: Float): Entity = engine.addEntityWithComponents(
            TransformComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(
                cooldownTime = 0.4f,
                offsetX = x,
                offsetY = y,
                offsetAngle = angle,
                isFiring = false,
                firingTime = 0f,
                projectileInit = WeaponTriggerStrategies.boring()
            ),
            RenderComponent(sprite, 3 /*Textures.cannon1*/)
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
    fun boring(): WeaponTriggerStrategy = {
        for (i in 0..5) {
            engine.addEntityWithComponents(
                TransformComponent(x, y, angle),
                RenderComponent(Textures.projectile[0].toCenteredSprite().apply { color = Color.YELLOW }, 999),
                HitBoxComponent(
                    physicsWorld = physicsWorld,
                    hitBoxWidth = 4.pixels,
                    hitBoxHeight = 4.pixels,
                    hitBoxRotation = 0f,
                    category = HitBoxCategory.EnemyProjectile
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

    fun fast(): WeaponTriggerStrategy = {
        for (i in 0..5) {
            engine.addEntityWithComponents(
                TransformComponent(x, y, angle),
                RenderComponent(Textures.projectile[0].toCenteredSprite().apply { color = Color.DARK_GRAY }, 999),
                HitBoxComponent(
                    physicsWorld = physicsWorld,
                    hitBoxWidth = 4.pixels,
                    hitBoxHeight = 4.pixels,
                    hitBoxRotation = 0f,
                    category = HitBoxCategory.PlayerProjectile
                ),
                ProjectileMovementComponent(
                    damage = 1f,
                    waitTime = i.toFloat() * 0.016f,
                    maxLifeTime = 3f,
                    movementStrategy = ProjectileMovementStrategies.straight(angle, 10f, movementVelocity)
                )
            )
        }
        true
    }

    fun rosette(isPlayerWeapon: Boolean): WeaponTriggerStrategy = {
        val totalShots = 200
        val waitTime = 0.04f
        val projectilesFired: Int = (firingTime / waitTime).toInt()
        var projectilesToFire: Int = ((firingTime + deltaTime) / waitTime).toInt() - projectilesFired
        if (projectilesToFire + projectilesFired > totalShots) projectilesToFire = totalShots - projectilesFired
        if (projectilesToFire > 0 && projectilesFired % 2 == 0) Sounds.shoot.play(playerDistance())
        for (i in 1..projectilesToFire) {
            val angleOffset = (i + projectilesFired) * 137.5f
            engine.addEntityWithComponents(
                TransformComponent(x, y, angle + angleOffset),
                RenderComponent(Textures.projectile[0].toCenteredSprite().apply { color = Color.RED }, 999),
                HitBoxComponent(
                    physicsWorld = physicsWorld,
                    hitBoxWidth = 4.pixels,
                    hitBoxHeight = 4.pixels,
                    hitBoxRotation = 0f,
                    category = if (isPlayerWeapon) HitBoxCategory.PlayerProjectile else HitBoxCategory.EnemyProjectile
                ),
                ProjectileMovementComponent(
                    waitTime = i.toFloat() * waitTime,
                    maxLifeTime = 10f,
                    damage = 0.5f,
                    movementStrategy = ProjectileMovementStrategies.straight(angle + angleOffset, 1.7f, movementVelocity)
                )
            )
        }
        projectilesToFire + projectilesFired >= totalShots
    }
}
