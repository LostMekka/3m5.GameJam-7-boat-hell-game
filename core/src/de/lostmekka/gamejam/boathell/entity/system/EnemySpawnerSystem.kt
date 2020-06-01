package de.lostmekka.gamejam.boathell.entity.system

import de.lostmekka.gamejam.boathell.entity.Ships.addAIBoat
import de.lostmekka.gamejam.boathell.entity.Ships.addAIPlane
import de.lostmekka.gamejam.boathell.entity.Ships.addAIRosetteShip
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import ktx.ashley.allOf
import kotlin.math.withSign
import kotlin.random.Random

class EnemySpawnerSystem() : BaseSystem() {

    private var timeSinceLastSpawn = 10f
    private var spawnTime = 8f

    override fun update(deltaTime: Float) {
        timeSinceLastSpawn += deltaTime
        if (timeSinceLastSpawn > spawnTime) {
            timeSinceLastSpawn = 0f

            val trans = entities.firstOrNull()?.let { TransformComponent.mapper[it] }

            if (trans != null) {
                val choice = Math.random()
                val r1 = (Random.nextFloat() - 0.5f) * 5f
                val r2 = (Random.nextFloat() - 0.5f) * 5f
                when {
                    choice < 0.3 -> addAIBoat(engine, trans.x + 5f.withSign(r1) + r1, trans.y + 5f.withSign(r2) + r2)
                    choice < 0.7 -> addAIPlane(engine, trans.x + 5f.withSign(r1) + r1, trans.y + 5f.withSign(r2) + r2)
                    else -> addAIRosetteShip(engine, trans.x + 10f.withSign(r1) + r1, trans.y + 10f.withSign(r2) + r2)
                }
            }
        }
    }

    override fun familyBuilder() = allOf(
        PlayerControlledComponent::class,
        TransformComponent::class
    )
}
