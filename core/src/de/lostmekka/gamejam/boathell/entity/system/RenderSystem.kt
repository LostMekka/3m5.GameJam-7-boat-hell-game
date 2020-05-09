package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent
import ktx.ashley.allOf
import ktx.ashley.get

class RenderSystem : BaseSystem() {
    // deliberately has no update method. the render system is called manually.
    fun render(spriteBatch: SpriteBatch) {
        for (entity in entities) {
            val pos = entity[PositionComponent.mapper]!!
            val sprite = entity[SpriteComponent.mapper]!!
            spriteBatch.draw(sprite.sprite, pos.x, pos.y, sprite.width, sprite.height)
        }
    }

    override fun familyBuilder() = allOf(
        PositionComponent::class,
        SpriteComponent::class
    )
}
