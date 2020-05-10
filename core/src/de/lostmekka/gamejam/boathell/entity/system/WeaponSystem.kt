package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.ShotContext
import de.lostmekka.gamejam.boathell.entity.component.WeaponComponent
import ktx.ashley.allOf
import kotlin.math.max

fun offsetPositionForParentRotation(weapon: WeaponComponent, parentRotation: Float): Vector3 =
    Vector3(weapon.offsetX, weapon.offsetY, 0f).rotate(Vector3.Z, parentRotation)

class WeaponSystem : BaseSystem() {
    override fun updateEntity(entity: Entity, deltaTime: Float) {
        val weapon = WeaponComponent.mapper[entity]
        weapon.cooldownCounter = max(weapon.cooldownCounter - deltaTime, 0f)
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val weapon = WeaponComponent.mapper[entity]
            val parent = weapon.parent ?: continue
            if (weapon.isFiring) {
                val parentTransform = PositionComponent.mapper[parent]
                val movement = ShipMovementComponent.mapper[parent]

                val pos = offsetPositionForParentRotation(weapon, parentTransform.rotation)
                val vel = Vector3(movement.velocity, 0f, 0f).rotate(Vector3.Z, parentTransform.rotation)

                val context = ShotContext(
                    x = parentTransform.x + pos.x,
                    y = parentTransform.y + pos.y,
                    angle = parentTransform.rotation + weapon.offsetAngle,
                    movementVelocity = vel,
                    firingTime = weapon.firingTime,
                    deltaTime = deltaTime,
                    engine = engine
                )
                weapon.firingTime += deltaTime
                if (weapon.projectileInit(context)) {
                    weapon.isFiring = false
                    weapon.firingTime = 0f
                }
            } else {
                weapon.cooldownCounter = max(weapon.cooldownCounter - deltaTime, 0f)
            }
        }
    }

    override fun familyBuilder() = allOf(
        PositionComponent::class,
        WeaponComponent::class
    )
}
