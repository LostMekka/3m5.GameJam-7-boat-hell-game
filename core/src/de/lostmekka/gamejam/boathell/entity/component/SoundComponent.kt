package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import de.lostmekka.gamejam.boathell.asset.SoundWithVolume
import ktx.ashley.mapperFor

class SoundComponent(
    var deathSound: SoundWithVolume? = null,
    var hitSound: SoundWithVolume? = null
) : Component {
    companion object {
        val mapper = mapperFor<SoundComponent>()
    }
}
