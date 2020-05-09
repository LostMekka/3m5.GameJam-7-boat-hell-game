package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.entity.component.*
import kotlin.math.*

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

    fun addAIBoat(engine: Engine, x: Float = 0f, y: Float = 0f, rotation: Float = 0f) {
        val sprite = Sprite(Textures.ship1)
        val w = 4f
        val h = 4f
        sprite.setBounds(-0.5f * w, -0.5f * h, w, h)
        sprite.setOriginCenter()

        engine.addEntityWithComponents(
            PositionComponent(x = x, y = y, rotation = rotation),
            SpriteComponent(sprite),
            ShipMovementComponent(velocity = 0.1f),
            AIShipComponent { (playerPos, shipPos) ->
                val b = sqrt((shipPos.x - playerPos.x).pow(2) + (shipPos.y - playerPos.y).pow(2))
                val a = sqrt((shipPos.x - playerPos.x).pow(2) + (shipPos.y - playerPos.y + 1).pow(2))
                val alpha = acos((a * a - b * b - 1) / ((-2) * b))
                this.rotation = alpha * 180 / PI.toFloat()
                if (this.rotation > 360) this.rotation -= 360
                if (this.rotation < 0) this.rotation += 360
            }
        )
    }
}

