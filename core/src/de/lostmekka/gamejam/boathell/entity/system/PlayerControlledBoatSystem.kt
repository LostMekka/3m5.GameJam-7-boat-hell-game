package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Input.Keys
import de.lostmekka.gamejam.boathell.GameConfig
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.WeaponOwnerComponent
import ktx.ashley.allOf
import kotlin.math.abs

class PlayerControlledBoatSystem : BaseSystem() {

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val position = PositionComponent.mapper.get(entity)
            val movement = ShipMovementComponent.mapper.get(entity)
            val weapons = WeaponOwnerComponent.mapper.get(entity)

            if (input.isKeyPressed(Keys.RIGHT)) {
                position.rotation -= GameConfig.Player.turnSpeed * deltaTime
                while (position.rotation < 0) position.rotation += 360
            }
            if (input.isKeyPressed(Keys.LEFT)) {
                position.rotation += GameConfig.Player.turnSpeed * deltaTime
                while (position.rotation > 360) position.rotation -= 360
            }
            if (input.isKeyPressed(Keys.UP) && movement.velocity < 10.0f) {
                movement.velocity += GameConfig.Player.acceleration * deltaTime
            }
            if (input.isKeyPressed(Keys.DOWN) && movement.velocity > 0.0f) {
                movement.velocity += GameConfig.Player.deceleration * deltaTime
            }

            if (input.isKeyPressed(Keys.SPACE)) {
                weapons.weaponComponents.forEach { it.shoot() }
            }

            if (input.isKeyPressed(Keys.NUM_1)) {
                weapons.weaponComponents.filter { wp -> abs(wp.offsetAngle) != 45f }.forEach { it.shoot() }
            }

            if (input.isKeyPressed(Keys.NUM_2)) {
                weapons.weaponComponents.filter { wp -> abs(wp.offsetAngle) == 45f }.forEach { it.shoot() }
            }

            movement.velocity -= movement.velocity * GameConfig.Player.friction
        }
    }

    override fun familyBuilder() = allOf(
        PlayerControlledComponent::class,
        ShipMovementComponent::class
    )
}
