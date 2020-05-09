package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class PlayerControlledComponent(
) : Component {
    companion object {
        val mapper = mapperFor<PlayerControlledComponent>()
    }
}
