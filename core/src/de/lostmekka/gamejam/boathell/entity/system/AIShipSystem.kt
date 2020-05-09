package de.lostmekka.gamejam.boathell.entity.system

import de.lostmekka.gamejam.boathell.entity.component.AIShipComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import ktx.ashley.allOf

class AIShipSystem : BaseSystem() {

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val pos = PositionComponent.mapper[entity]
            val mov = AIShipComponent.mapper[entity]
            // todo: insert player position
            mov.movementStrategy(pos, AIShipComponent.MovementStrategyContext(PositionComponent(5f, 5f, 0f), pos))
        }
    }

    override fun familyBuilder() = allOf(
        AIShipComponent::class,
        PositionComponent::class
    )
}
