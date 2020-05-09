package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor

class WeaponOwnerComponent(
    val weaponEntities: MutableList<Entity>
) : Component {
    constructor(vararg weaponEntities: Entity) : this(weaponEntities.toMutableList())

    val weaponComponents get() = weaponEntities.map { WeaponComponent.mapper[it] }

    companion object {
        val mapper = mapperFor<WeaponOwnerComponent>()
    }
}
