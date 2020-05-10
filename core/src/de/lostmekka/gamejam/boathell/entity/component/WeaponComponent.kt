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
    var firingTime: Float,
    val deltaTime: Float,
    val engine: Engine
)

class WeaponComponent(
    var cooldownTime: Float,
    var offsetX: Float,
    var offsetY: Float,
    var offsetAngle: Float,
    var isFiring: Boolean = false,
    var firingTime: Float = 0f,
    var projectileInit: WeaponTriggerStrategy,
    var cooldownCounter: Float = 0f,
    var parent: Entity? = null
) : Component {

    fun shoot() {
        if (cooldownCounter <= 0f && !isFiring) {
            cooldownCounter = cooldownTime
            isFiring = true
        }
    }

    companion object {
        val mapper = mapperFor<WeaponComponent>()
    }
}
