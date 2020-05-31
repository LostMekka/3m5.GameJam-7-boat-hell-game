package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.OrthographicCamera
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import ktx.ashley.allOf

class CameraControl : BaseSystem() {
    val camera = OrthographicCamera().apply { zoom = 2f }

    override fun familyBuilder(): Family.Builder {
        return allOf(PlayerControlledComponent::class, TransformComponent::class)
    }

    override fun update(deltaTime: Float) {
        val player = entities.firstOrNull()
        if (player != null) {
            val trans = TransformComponent.mapper[player]

            camera.position.add(
                0.033f * (trans.x - camera.position.x),
                0.033f * (trans.y - camera.position.y),
                0f
            )
            camera.update()
        }
    }

    fun zoomBy(amount: Int) {
        camera.zoom *= if (amount > 0) 0.9f else if (amount < 0) 1.1f else 1.0f
        camera.update()
    }
}
