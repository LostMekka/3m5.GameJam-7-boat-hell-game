package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import ktx.ashley.allOf

class ShipMovementSystem : BaseSystem() {

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val position: PositionComponent = PositionComponent.mapper.get(entity)
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                position.rotation += 10 * deltaTime
                println(position.rotation)
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                position.rotation -= 10 * deltaTime
                println(position.rotation)
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                position.y += 1 * deltaTime
            }
        }
    }

    override fun familyBuilder() = allOf(
        PositionComponent::class,
        ShipMovementComponent::class
    )
}
