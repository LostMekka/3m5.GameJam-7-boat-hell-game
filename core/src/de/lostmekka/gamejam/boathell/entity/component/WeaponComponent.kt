package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector3
import ktx.ashley.mapperFor

class WeaponComponent(
    var cooldownTime: Float,
    var offsetX: Float,
    var offsetY: Float,
    var offsetAngle: Float,
    var projectileInit: ShotContext.() -> Unit,
    var cooldownCounter: Float = 0f
) : Component {
    data class ShotContext(
        val x: Float,
        val y: Float,
        val angle: Float,
        val engine: Engine
    )

    fun offsetPositionForParentRotation(parentRotation: Float): Vector3 =
        Vector3(offsetX, offsetY, 0f).rotate(Vector3.Z, parentRotation)

    fun shoot(parentTransform: PositionComponent, engine: Engine) {
        if (cooldownCounter <= 0f) {
            val pos = offsetPositionForParentRotation(parentTransform.rotation)
            val context = ShotContext(
                x = parentTransform.x + pos.x,
                y = parentTransform.y + pos.y,
                angle = parentTransform.rotation + offsetAngle,
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
