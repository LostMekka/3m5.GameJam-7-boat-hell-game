package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray

abstract class BaseSystem(priority: Int = 0) : EntitySystem(priority) {
    private lateinit var entitiesInternal: ImmutableArray<Entity>
    val entities get() = entitiesInternal

    override fun addedToEngine(engine: Engine) {
        entitiesInternal = engine.getEntitiesFor(familyBuilder().get())
    }

    abstract fun familyBuilder(): Family.Builder
}
