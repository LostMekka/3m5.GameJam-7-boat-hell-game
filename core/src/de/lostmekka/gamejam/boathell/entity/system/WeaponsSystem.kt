package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Entity
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.WeaponsComponent
import ktx.ashley.allOf
import kotlin.math.max

class WeaponsSystem : BaseSystem() {
    override fun updateEntity(entity: Entity, deltaTime: Float) {
        val weapons = WeaponsComponent.mapper[entity].weapons
        for (weapon in weapons) {
            weapon.cooldownCounter = max(weapon.cooldownCounter - deltaTime, 0f)
        }
    }

    override fun familyBuilder() = allOf(
        PositionComponent::class,
        WeaponsComponent::class
    )
}
