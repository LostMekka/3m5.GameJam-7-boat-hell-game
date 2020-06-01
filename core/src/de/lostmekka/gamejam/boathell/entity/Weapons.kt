package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.asset.toCenteredSprite
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import de.lostmekka.gamejam.boathell.entity.system.RenderComponent
import de.lostmekka.gamejam.boathell.entity.system.WeaponComponent
import de.lostmekka.gamejam.boathell.entity.system.WeaponTriggerStrategies
import de.lostmekka.gamejam.boathell.pixels

fun Engine.addBoatFrontCannon1(): MutableList<Entity> {
    val sprite = Textures.cannon1[0].toCenteredSprite()

    val frontCanon = this.addEntityWithComponents(
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
        RenderComponent(sprite, 101 /*Textures.cannon1*/)
    )

    fun sideCannon(x: Float, y: Float, angle: Float): Entity = this.addEntityWithComponents(
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
        RenderComponent(sprite, 103 /*Textures.cannon1*/)
    )

    return mutableListOf(
        frontCanon,
        sideCannon(10.pixels, 10.pixels, 45f),
        sideCannon(10.pixels, (-10).pixels, -45f),
        sideCannon(3.pixels, 10.pixels, 45f),
        sideCannon(3.pixels, (-10).pixels, -45f)
    )
}

fun Engine.addShip1FrontCannon1(): Entity {
    val sprite = Textures.cannon1[0].toCenteredSprite()

    return this.addEntityWithComponents(
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
        RenderComponent(sprite, 103 /*Textures.cannon1*/)
    )
}

fun Engine.addShipRosettaCannon(isPlayerWeapon: Boolean): Entity {
    val sprite = Textures.cannon1[0].toCenteredSprite()

    return this.addEntityWithComponents(
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
        RenderComponent(sprite, 103 /*Textures.cannon1*/)
    )
}

fun Engine.addShip1SideCannons(): MutableList<Entity> {
    val sprite = Textures.cannon1[0].toCenteredSprite()

    fun sideCannon(x: Float, y: Float, angle: Float): Entity = this.addEntityWithComponents(
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
        RenderComponent(sprite, 103 /*Textures.cannon1*/)
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
