package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

enum class EntityTypeComponent : Component {
    Ship, Air, Projectile;

    companion object {
        val mapper = mapperFor<EntityTypeComponent>()
    }
}
