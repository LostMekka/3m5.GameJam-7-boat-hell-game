package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.World
import de.lostmekka.gamejam.boathell.GameConfig
import de.lostmekka.gamejam.boathell.entity.component.HitBoxComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.toDegrees
import de.lostmekka.gamejam.boathell.toRadians
import ktx.ashley.allOf
import kotlin.random.Random

class PhysicsUpdateSystem(
    private val physicWorld: World
) : BaseSystem(10) {
    private val stepTime = 1f / GameConfig.Physics.stepsPerSecond.toFloat()
    private var counter = 0f

    override fun update(deltaTime: Float) {
        counter += deltaTime
        if (counter / stepTime >= 2) println("$counter / $stepTime = ${counter / stepTime}")
        while (counter >= stepTime) {
            counter -= stepTime
            entities.forEach { prePhysics(it) }
            physicWorld.step(deltaTime, GameConfig.Physics.velocityIterations, GameConfig.Physics.positionIterations)
            entities.forEach { postPhysics(it) }
        }
    }

    private fun prePhysics(entity: Entity) {
        val box = HitBoxComponent.mapper[entity]
        val pos = PositionComponent.mapper[entity]
        box.hitBox.apply {
            setTransform(pos.x, pos.y, pos.rotation.toRadians())
            userData = entity
        }
    }

    private fun postPhysics(entity: Entity) {
        val box = HitBoxComponent.mapper[entity]
        val pos = PositionComponent.mapper[entity]
        pos.apply {
            val physicsPos = box.hitBox.position
            x = physicsPos.x
            y = physicsPos.y
            rotation = box.hitBox.angle.toDegrees()
        }
    }

    override fun familyBuilder() = allOf(
        HitBoxComponent::class,
        PositionComponent::class
    )
}
