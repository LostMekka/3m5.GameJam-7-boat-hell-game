package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.asset.toCenteredSprite
import de.lostmekka.gamejam.boathell.entity.component.AIShipComponent
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

object Ships {
    fun addPlayerBoat(engine: Engine) {
        engine.addEntityWithComponents(
            PositionComponent(x = 0f, y = 0f, rotation = MathUtils.random(360f)),
            SpriteComponent(Textures.boat1.toCenteredSprite()),
            ShipMovementComponent(),
            PlayerControlledComponent()
        )
    }

    fun addAIBoat(engine: Engine, x: Float = 0f, y: Float = 0f, rotation: Float = 0f) {
        engine.addEntityWithComponents(
            PositionComponent(x = x, y = y, rotation = rotation),
            SpriteComponent(Textures.ship1.toCenteredSprite()),
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
