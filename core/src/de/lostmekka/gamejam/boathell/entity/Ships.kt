package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent

object Ships {
    fun addPlayerBoat(engine: Engine) {
        val sprite = Sprite(Textures.boat1)
        val w = 4f
        val h = 4f
        sprite.setBounds(-0.5f * w, -0.5f * h, w, h)
        sprite.setOriginCenter()

        engine.addEntityWithComponents(
            PositionComponent(x = 0f, y = 0f, rotation = MathUtils.random(360f)),
            SpriteComponent(sprite),
            ShipMovementComponent(),
            PlayerControlledComponent()
        )
    }
}

