package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class PositionComponent(
    var x: Float,
    var y: Float,
    var rotation: Int
): Component {
    companion object {
        val mapper = mapperFor<PositionComponent>()
    }
}
