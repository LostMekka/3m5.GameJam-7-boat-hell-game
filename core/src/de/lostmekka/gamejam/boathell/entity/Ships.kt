package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Sprite
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent

object Ships {
    fun addPlayerBoat(engine: Engine) {
        engine.addEntityWithComponents(
            PositionComponent(x = 0f, y = 0f, rotation = 0f),
            SpriteComponent(Sprite(Textures.boat1)),
            ShipMovementComponent(),
            PlayerControlledComponent()
        )
    }
}

