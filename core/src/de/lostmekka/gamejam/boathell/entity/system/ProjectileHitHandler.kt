package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import de.lostmekka.gamejam.boathell.entity.addExplosion
import de.lostmekka.gamejam.boathell.entity.component.HealthComponent
import de.lostmekka.gamejam.boathell.entity.component.SoundComponent
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class ProjectileHitHandler(val engine: Engine) {
    fun familyBuilder(): Family.Builder = allOf(ProjectileMovementComponent::class)

    fun onHit(entity: Entity, hit: Entity) {
        val pComponent = ProjectileMovementComponent.mapper[entity]
        engine.removeEntity(entity)

        val healthComponent = hit.get<HealthComponent>()
        if (healthComponent != null) {
            healthComponent.health -= pComponent.damage
            val sounds = hit[SoundComponent.mapper]
            if (healthComponent.health <= 0) {
                val trans = TransformComponent.mapper[hit]
                engine.addExplosion(trans.vec())
                removeWeapons(hit, engine)
                engine.removeEntity(hit)
                sounds?.deathSound?.play()
            } else {
                sounds?.hitSound?.play()
            }
        }
    }
}
