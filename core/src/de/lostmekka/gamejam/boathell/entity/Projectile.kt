package de.lostmekka.gamejam.boathell.entity

import com.badlogic.gdx.graphics.g2d.Sprite
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ProjectileMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent
import kotlin.math.cos
import kotlin.math.sin

object Projectiles {
    fun simple(
        startX: Float,
        startY: Float,
        startAngle: Float,
        speed: Float,
        maxLifetime: Float
    ) = entity(
        PositionComponent(startX, startY, startAngle),
        SpriteComponent(Sprite(Textures.projectile[0])),
        ProjectileMovementComponent(maxLifetime) { context ->
            x += cos(startAngle) * speed * context.deltaTime
            y += sin(startAngle) * speed * context.deltaTime
        }
    )
}
