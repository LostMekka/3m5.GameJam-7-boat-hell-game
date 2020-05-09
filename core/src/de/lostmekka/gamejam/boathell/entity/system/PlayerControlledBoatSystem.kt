package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import ktx.ashley.allOf

class PlayerControlledBoatSystem : BaseSystem() {

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val position: PositionComponent = PositionComponent.mapper.get(entity)
            val velocity: ShipMovementComponent = ShipMovementComponent.mapper.get(entity)
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                position.rotation -= 50 * deltaTime
                if (position.rotation < 0) position.rotation += 360
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                position.rotation += 50 * deltaTime
                if (position.rotation > 360) position.rotation -= 360
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                velocity.velocity = 0.1f
            } else {
                velocity.velocity = 0f
            }
        }
    }

    override fun familyBuilder() = allOf(
        PlayerControlledComponent::class,
        ShipMovementComponent::class
    )
}
