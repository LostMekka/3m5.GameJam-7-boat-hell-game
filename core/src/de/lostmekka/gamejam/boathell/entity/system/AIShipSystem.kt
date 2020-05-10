package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.physics.box2d.World
import de.lostmekka.gamejam.boathell.entity.MovementStrategyContext
import de.lostmekka.gamejam.boathell.entity.component.AIShipComponent
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import ktx.ashley.allOf

class AIShipSystem(
    private val physicsWorld: World
) : BaseSystem() {
    private lateinit var playerEntities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        playerEntities = engine.getEntitiesFor(allOf(PlayerControlledComponent::class, PositionComponent::class).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val mov = AIShipComponent.mapper[entity]
            mov.movementStrategy(MovementStrategyContext(entity, playerEntities.firstOrNull()))
            mov.firePattern(entity, engine, physicsWorld)
        }
    }

    override fun familyBuilder() = allOf(
        AIShipComponent::class,
        PositionComponent::class
    )
}
