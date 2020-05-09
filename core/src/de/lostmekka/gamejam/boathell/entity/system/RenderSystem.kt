package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent
import ktx.ashley.allOf
import ktx.ashley.get

class RenderSystem(
    var spriteBatch: SpriteBatch,
    var shapeRenderer: ShapeRenderer,
    var viewport: Viewport
) : BaseSystem() {
    // deliberately has no update method. the render system is called manually.
    fun render() {
        spriteBatch.projectionMatrix = viewport.camera.projection
        for (entity in entities) {
            val pos = entity[PositionComponent.mapper]!!
            val sprite = entity[SpriteComponent.mapper]!!.sprite
            spriteBatch.draw(sprite, pos.x, pos.y)
        }
    }

    override fun familyBuilder() = allOf(
        PositionComponent::class,
        SpriteComponent::class
    )
}
