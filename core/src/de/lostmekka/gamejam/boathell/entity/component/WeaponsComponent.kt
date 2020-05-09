package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import de.lostmekka.gamejam.boathell.entity.component.WeaponsComponent.ShotContext
import ktx.ashley.mapperFor

class WeaponsComponent(
    val weapons: MutableList<Weapon>
) : Component {
    data class ShotContext(
        val x: Float,
        val y: Float,
        val angle: Float
    )
    companion object {
        val mapper = mapperFor<WeaponsComponent>()
    }
}

class Weapon(
    val cooldownTime: Float,
    var angleOffset: Float,
    val projectileInit: Engine.(ShotContext) -> Unit
) {
    var cooldownCounter = 0f

    fun shoot(x: Float, y: Float, engine: Engine) {
        if (cooldownCounter <= 0f) {
            engine.projectileInit(ShotContext(x, y, angleOffset))
            cooldownCounter = cooldownTime
        }
    }
}
