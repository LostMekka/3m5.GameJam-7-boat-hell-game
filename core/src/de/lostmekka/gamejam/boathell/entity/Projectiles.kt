package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Sprite
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ProjectileMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.ProjectileMovementStrategy
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent
import kotlin.math.cos
import kotlin.math.sin

object Projectiles {
    fun addSimpleProjectile(
        engine: Engine,
        startX: Float,
        startY: Float,
        startAngle: Float,
        speed: Float,
        maxLifetime: Float
    ) = engine.addEntityWithComponents(
        PositionComponent(startX, startY, startAngle),
        SpriteComponent(Sprite(Textures.projectile[0])),
        ProjectileMovementComponent(
            maxLifeTime = maxLifetime,
            movementStrategy = ProjectileMovementStrategies.straight(startAngle, speed)
        )
    )
}

object ProjectileMovementStrategies {
    fun straight(
        startAngle: Float,
        speed: Float
    ): ProjectileMovementStrategy = {
        pos.x += cos(startAngle) * speed * deltaTime
        pos.y += sin(startAngle) * speed * deltaTime
    }
}
