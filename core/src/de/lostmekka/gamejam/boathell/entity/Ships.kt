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
import de.lostmekka.gamejam.boathell.entity.component.WeaponOwnerComponent
import kotlin.math.*

private fun ensureCircleRange(input: Float): Float {
    var result = input
    if (input > 360) result -= 360
    if (input < 0) result += 360
    return result
}

object Ships {
    fun addPlayerBoat(engine: Engine) {
        engine.addEntityWithComponents(
            PositionComponent(x = 0f, y = 0f, rotation = MathUtils.random(360f)),
            SpriteComponent(Textures.boat1.toCenteredSprite()),
            ShipMovementComponent(),
            PlayerControlledComponent(),
            WeaponOwnerComponent(
                Weapons.addWeapon(
                    engine = engine,
                    cooldownTime = 1f,
                    offsetAngle = 0f,
                    offsetX = 0f,
                    offsetY = 0.5f,
                    projectileInit = WeaponTriggerStrategies.boring
                )
            )
        )
    }

    fun addAIBoat(engine: Engine, x: Float = 0f, y: Float = 0f, rotation: Float = 0f) {
        engine.addEntityWithComponents(
            PositionComponent(x = x, y = y, rotation = rotation),
            SpriteComponent(Textures.ship1.toCenteredSprite()),
            ShipMovementComponent(velocity = 0.025f),
            AIShipComponent { (playerPos, shipPos) ->
                val b = sqrt((shipPos.x - playerPos.x).pow(2) + (shipPos.y - playerPos.y).pow(2))
                val a = sqrt((shipPos.x - playerPos.x).pow(2) + (shipPos.y - playerPos.y + 1).pow(2))
                val alpha = acos((a * a - b * b - 1) / ((-2) * b))
                var rotationGoal = alpha * 180 / PI.toFloat()
                if (playerPos.x - shipPos.x > 0) rotationGoal *= -1
                rotationGoal = ensureCircleRange(rotationGoal)

                // approach desired rotation stepwise
                val step = 2
                if (abs(rotationGoal - this.rotation) > 180) {
                    if (rotationGoal > this.rotation + 2) this.rotation -= step
                    else if (rotationGoal < this.rotation - 2) this.rotation += step
                } else {
                    if (rotationGoal > this.rotation + 2) this.rotation += step
                    else if (rotationGoal < this.rotation - 2) this.rotation -= step
                }
                this.rotation = ensureCircleRange(this.rotation)
            }
        )
    }
}
