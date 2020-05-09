package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Input.Keys
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import ktx.ashley.allOf

class PlayerControlledBoatSystem : BaseSystem() {

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val accel = 0.1f
            val deccel = -0.1f
            val friction = 0.01f

            val position = PositionComponent.mapper.get(entity)
            val movement = ShipMovementComponent.mapper.get(entity)

            if (input.isKeyPressed(Keys.RIGHT)) {
                position.rotation -= 50 * deltaTime
                while (position.rotation < 0) position.rotation += 360
            }
            if (input.isKeyPressed(Keys.LEFT)) {
                position.rotation += 50 * deltaTime
                while (position.rotation > 360) position.rotation -= 360
            }
            if (input.isKeyPressed(Keys.UP) && movement.velocity < 10.0f) {
                movement.velocity += accel * deltaTime
            }
            if (input.isKeyPressed(Keys.DOWN) && movement.velocity > 0.0f) {
                movement.velocity += deccel * deltaTime
            }

            movement.velocity -= movement.velocity * friction
        }
    }

    override fun familyBuilder() = allOf(
        PlayerControlledComponent::class,
        ShipMovementComponent::class
    )
}
