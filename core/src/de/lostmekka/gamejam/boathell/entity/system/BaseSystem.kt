package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent
import ktx.ashley.allOf

abstract class BaseSystem(priority: Int = 0): EntitySystem(priority) {
    private lateinit var entitiesInternal: ImmutableArray<Entity>
    val entities get() = entitiesInternal

    override fun addedToEngine(engine: Engine) {
        entitiesInternal = engine.getEntitiesFor(
            allOf(
                PositionComponent::class,
                SpriteComponent::class
            ).get())
    }
}
