package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Sprite
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.asset.toCenteredSprite
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ProjectileMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent
import de.lostmekka.gamejam.boathell.entity.component.WeaponComponent
import de.lostmekka.gamejam.boathell.entity.component.WeaponTriggerStrategy

object Weapons {
    fun addBoatFrontCannon1(engine: Engine): Entity {
        val sprite = Textures.cannon1[0].toCenteredSprite()

        return engine.addEntityWithComponents(
            PositionComponent(0f, 0f, 0f), // will be auto set by weapon owner system
            WeaponComponent(0.23f, 0.5f - 4f/32f, 0f, 0f, WeaponTriggerStrategies.boring),
            SpriteComponent(sprite, 1, Textures.cannon1)
        )

//        engine = engine,
//        cooldownTime = 1f,
//        offsetAngle = y * 90f,
//        offsetX = x * 0.3f,
//        offsetY = y * 0.5f,
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

object WeaponTriggerStrategies {
    val boring: WeaponTriggerStrategy = {
        engine.addEntityWithComponents(
            PositionComponent(x, y, angle),
            SpriteComponent(Textures.projectile[0].toCenteredSprite()),
            ProjectileMovementComponent(
                maxLifeTime = 2f,
                movementStrategy = ProjectileMovementStrategies.straight(angle, 10f)
            )
        )
    }
}
