package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3
import de.lostmekka.gamejam.boathell.entity.WeaponTriggerStrategy
import ktx.ashley.mapperFor

data class ShotContext(
    val x: Float,
    val y: Float,
    val angle: Float,
    val movementVelocity: Vector3,
    val engine: Engine
)

class WeaponComponent(
    var cooldownTime: Float,
    var offsetX: Float,
    var offsetY: Float,
    var offsetAngle: Float,
    var projectileInit: WeaponTriggerStrategy,
    var cooldownCounter: Float = 0f
) : Component {
    fun offsetPositionForParentRotation(parentRotation: Float): Vector3 =
        Vector3(offsetX, offsetY, 0f).rotate(Vector3.Z, parentRotation)

    fun shoot(parent: Entity, engine: Engine) {
        val parentTransform = PositionComponent.mapper[parent]
        val movement = ShipMovementComponent.mapper[parent]

        if (cooldownCounter <= 0f) {
            val pos = offsetPositionForParentRotation(parentTransform.rotation)
            val vel = Vector3(movement.velocity, 0f, 0f).rotate(Vector3.Z, parentTransform.rotation)

            val context = ShotContext(
                x = parentTransform.x + pos.x,
                y = parentTransform.y + pos.y,
                angle = parentTransform.rotation + offsetAngle,
                movementVelocity = vel,
                engine = engine
            )
            projectileInit(context)
            cooldownCounter = cooldownTime
        }
    }

    companion object {
        val mapper = mapperFor<WeaponComponent>()
    }
}
