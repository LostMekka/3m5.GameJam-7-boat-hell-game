package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class TransformComponent(
    var x: Float = 0.0f,
    var y: Float = 0.0f,
    var rotation: Float = 0.0f
) : Component {

    companion object  {
        val mapper = mapperFor<TransformComponent>()
    }
}
