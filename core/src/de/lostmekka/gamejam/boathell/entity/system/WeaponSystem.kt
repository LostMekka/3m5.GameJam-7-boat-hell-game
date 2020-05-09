package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Entity
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.WeaponComponent
import ktx.ashley.allOf
import kotlin.math.max

class WeaponSystem : BaseSystem() {
    override fun updateEntity(entity: Entity, deltaTime: Float) {
        val weapon = WeaponComponent.mapper[entity]
        weapon.cooldownCounter = max(weapon.cooldownCounter - deltaTime, 0f)
    }

    override fun familyBuilder() = allOf(
        PositionComponent::class,
        WeaponComponent::class
    )
}
