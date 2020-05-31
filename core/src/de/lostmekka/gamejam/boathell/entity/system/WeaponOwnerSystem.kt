package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class WeaponOwnerSystem : BaseSystem() {
    override fun updateEntity(entity: Entity, deltaTime: Float) {
        val ownerPos = TransformComponent.mapper[entity]
        val weaponEntities = WeaponOwnerComponent.mapper[entity].weaponEntities
        for (weaponEntity in weaponEntities) {
            val weaponPos = TransformComponent.mapper[weaponEntity]
            val weapon = WeaponComponent.mapper[weaponEntity]
            weapon.parent = entity
            val localOffset = offsetPositionForParentRotation(weapon, ownerPos.rotation)
            weaponPos.apply {
                x = ownerPos.x + localOffset.x
                y = ownerPos.y + localOffset.y
                rotation = ownerPos.rotation + weapon.offsetAngle
            }
        }
    }

    override fun familyBuilder() = allOf(
        TransformComponent::class,
        WeaponOwnerComponent::class
    )
}

class WeaponOwnerComponent(
    val weaponEntities: MutableList<Entity>
) : Component {
    constructor(vararg weaponEntities: Entity) : this(weaponEntities.toMutableList())

    val weaponComponents get() = weaponEntities.map { WeaponComponent.mapper[it] }

    companion object {
        val mapper = mapperFor<WeaponOwnerComponent>()
    }
}
