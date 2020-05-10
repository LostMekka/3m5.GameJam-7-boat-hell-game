package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.physics.box2d.World
import de.lostmekka.gamejam.boathell.entity.Ships.addAIBoat
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import ktx.ashley.allOf

class EnemySpawnerSystem(
    private val physicsWorld: World
) : BaseSystem() {
    override fun update(deltaTime: Float) {
        if (Math.random() < 0.002) {
            val playerPos = entities
                .firstOrNull()
                ?.let { PositionComponent.mapper[it] }
                ?: PositionComponent(0f, 0f, 0f)
            addAIBoat(
                engine = engine,
                physicsWorld = physicsWorld,
                x = ((playerPos.x.toInt() - 10..playerPos.x.toInt() + 10).shuffled().first()).toFloat(),
                y = ((playerPos.y.toInt() - 10..playerPos.y.toInt() + 10).shuffled().first()).toFloat()
            )
        }
    }

    override fun familyBuilder() = allOf(
        PlayerControlledComponent::class,
        PositionComponent::class
    )
}
