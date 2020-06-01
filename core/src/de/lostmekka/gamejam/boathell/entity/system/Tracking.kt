package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Affine2
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor

class TrackingSystem : BaseSystem() {
    override fun familyBuilder(): Family.Builder = allOf(TransformComponent::class, TrackingComponent::class)

    override fun updateEntity(entity: Entity, deltaTime: Float) {
        val trans = TransformComponent.mapper[entity]
        val track = TrackingComponent.mapper[entity]
        val trackedTrans = track.tracked.get<TransformComponent>()

        if (!engine.entities.contains(track.tracked)) {
            engine.removeEntity(entity)
        } else if (trackedTrans != null) {
            val newPos = trackedTrans.vec()
            track.offset.applyTo(newPos)
            trans.x = newPos.x
            trans.y = newPos.y
        }
    }
}

class TrackingComponent(val tracked: Entity, val offset: Affine2 = Affine2()) : Component {
    companion object {
        val mapper = mapperFor<TrackingComponent>()
    }
}
