package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.World
import de.lostmekka.gamejam.boathell.entity.WeaponTriggerStrategy
import ktx.ashley.allOf
import ktx.ashley.mapperFor

data class ShotContext(
    val x: Float,
    val y: Float,
    val angle: Float,
    val movementVelocity: Vector3,
    var firingTime: Float,
    val deltaTime: Float,
    val engine: Engine,
    val physicsWorld: World
) {
    fun playerDistance(): Float {
        val playerEntities = engine.getEntitiesFor(allOf(PlayerControlledComponent::class, TransformComponent::class).get())
        val player = playerEntities.firstOrNull()
        val pos = TransformComponent.mapper[player]
        return Vector2(pos.x, pos.y).sub(Vector2(x, y)).len()
    }
}

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

    fun shoot(): Boolean {
        return if (cooldownCounter <= 0f && !isFiring) {
            cooldownCounter = cooldownTime
            isFiring = true
            true
        } else {
            false
        }
    }

    companion object {
        val mapper = mapperFor<WeaponComponent>()
    }
}
