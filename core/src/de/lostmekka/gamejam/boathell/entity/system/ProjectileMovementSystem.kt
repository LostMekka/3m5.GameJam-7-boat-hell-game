package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.cosDeg
import de.lostmekka.gamejam.boathell.entity.addEntityWithComponents
import de.lostmekka.gamejam.boathell.entity.addWaterSplash
import de.lostmekka.gamejam.boathell.entity.component.*
import de.lostmekka.gamejam.boathell.sinDeg
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class ProjectileMovementSystem : BaseSystem() {
    override fun updateEntity(entity: Entity, deltaTime: Float) {
        val pos = TransformComponent.mapper[entity]
        val mov = ProjectileMovementComponent.mapper[entity]
        if (mov.waitTime > 0) {
            mov.waitTime -= deltaTime
        } else {
            mov.lifeTime += deltaTime
            mov.movementStrategy(ProjectileMovementStrategyContext(pos, deltaTime, mov.lifeTime))
            if (mov.lifeTime > mov.maxLifeTime) {
                engine.removeEntity(entity)
                engine.addWaterSplash(pos.vec())
            }
            // TODO: check hit
        }
    }

    override fun familyBuilder() = allOf(
        TransformComponent::class,
        ProjectileMovementComponent::class
    )
}

typealias ProjectileMovementStrategy = ProjectileMovementStrategyContext.() -> Unit

data class ProjectileMovementStrategyContext(
    val pos: TransformComponent,
    val deltaTime: Float,
    val totalLifetime: Float
)

class ProjectileMovementComponent(
    var damage: Float,
    var waitTime: Float,
    var maxLifeTime: Float,
    var lifeTime: Float = 0f,
    var movementStrategy: ProjectileMovementStrategy
) : Component {
    companion object {
        val mapper = mapperFor<ProjectileMovementComponent>()
    }
}

object ProjectileMovementStrategies {
    fun straight(
        startAngle: Float,
        speed: Float,
        movementVelocity: Vector3
    ): ProjectileMovementStrategy = {
        pos.x += (cosDeg(startAngle) * speed + movementVelocity.x) * deltaTime
        pos.y += (sinDeg(startAngle) * speed + movementVelocity.y) * deltaTime
    }
}
