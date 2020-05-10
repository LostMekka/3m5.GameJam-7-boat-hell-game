package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.physics.box2d.World
import de.lostmekka.gamejam.boathell.entity.Ships.addAIBoat
import de.lostmekka.gamejam.boathell.entity.Ships.addAIPlane
import de.lostmekka.gamejam.boathell.entity.Ships.addAIRosetteShip
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import ktx.ashley.allOf

class EnemySpawnerSystem(
    private val physicsWorld: World
) : BaseSystem() {

    private var timeSinceLastSpawn = 10f
    private var spawnTime = 8f

    override fun update(deltaTime: Float) {
        timeSinceLastSpawn += deltaTime
        if (timeSinceLastSpawn > spawnTime) {
            timeSinceLastSpawn = 0f
            val playerPos = entities
                .firstOrNull()
                ?.let { PositionComponent.mapper[it] }
                ?: PositionComponent(0f, 0f, 0f)

            if (Math.random() * 3 < 1) {
                addAIBoat(
                    engine = engine,
                    physicsWorld = physicsWorld,
                    x = getRandomCoordinateInRangeAroundPlayer(playerPos.x, 10, 5),
                    y = getRandomCoordinateInRangeAroundPlayer(playerPos.y, 10, 5)
                )
            } else if (Math.random() * 2 < 1) {
                addAIPlane(
                    engine = engine,
                    physicsWorld = physicsWorld,
                    x = getRandomCoordinateInRangeAroundPlayer(playerPos.x, 15, 10),
                    y = getRandomCoordinateInRangeAroundPlayer(playerPos.y, 15, 10)
                )
            } else {
                addAIRosetteShip(
                    engine = engine,
                    physicsWorld = physicsWorld,
                    x = getRandomCoordinateInRangeAroundPlayer(playerPos.x, 10, 5),
                    y = getRandomCoordinateInRangeAroundPlayer(playerPos.y, 10, 5)
                )
            }
        }
    }

    private fun getRandomCoordinateInRangeAroundPlayer(x: Float, maxDistance: Int, minDistance: Int): Float =
        (((x.toInt() - maxDistance..x.toInt() - minDistance).toList() + ((x.toInt() + minDistance..x.toInt() + maxDistance))
            .toList())
            .shuffled()
            .first())
            .toFloat()

    override fun familyBuilder() = allOf(
        PlayerControlledComponent::class,
        PositionComponent::class
    )
}
