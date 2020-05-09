package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.utils.ImmutableArray
import de.lostmekka.gamejam.boathell.entity.component.AIShipComponent
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import ktx.ashley.allOf


class AIShipSystem : BaseSystem() {

    private lateinit var playerEntites: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        playerEntites = engine.getEntitiesFor(allOf(PlayerControlledComponent::class, PositionComponent::class).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val pos = PositionComponent.mapper[entity]
            val mov = AIShipComponent.mapper[entity]
            var playerPos = PositionComponent(0f, 0f, 0f)
            if (playerEntites.size() > 0) playerPos = PositionComponent.mapper[playerEntites.get(0)]
            mov.movementStrategy(pos, AIShipComponent.MovementStrategyContext(playerPos, pos))
        }
    }

    override fun familyBuilder() = allOf(
        AIShipComponent::class,
        PositionComponent::class
    )
}
