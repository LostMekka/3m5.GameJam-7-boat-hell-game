package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use

class RenderSystem : BaseSystem() {
    val batch = SpriteBatch()
    val camera = OrthographicCamera().apply { zoom = 2f }
    val viewport = ScreenViewport(camera).apply { unitsPerPixel = 1.0f / 32.0f / 4.0f }

    override fun familyBuilder() = allOf(
        PositionComponent::class,
        SpriteComponent::class
    )

    override fun update(deltaTime: Float) {
        val entities = entities.sortedBy { SpriteComponent.mapper[it].zLayer }

        batch.begin()
        batch.projectionMatrix = viewport.camera.combined
        for (entity in entities) {
            val pos = PositionComponent.mapper[entity]
            val spriteComp = SpriteComponent.mapper[entity]
            val sprite = spriteComp.sprite

            sprite.translate(pos.x, pos.y)
            sprite.rotation += pos.rotation
            sprite.draw(batch)
            sprite.rotation -= pos.rotation
            sprite.translate(-pos.x, -pos.y)
        }
        batch.end()
    }
}
