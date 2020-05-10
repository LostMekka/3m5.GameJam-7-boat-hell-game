package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent
import ktx.ashley.allOf
import ktx.ashley.get

class RenderSystem : BaseSystem() {
    // deliberately has no update method. the render system is called manually.
    fun render(spriteBatch: SpriteBatch) {
        val entities = entities.sortedBy { SpriteComponent.mapper[it].zLayer }
        for (entity in entities) {
            val pos = PositionComponent.mapper[entity]
            val spriteComp = SpriteComponent.mapper[entity]
            val sprite = spriteComp.sprite

            sprite.translate(pos.x, pos.y)
            sprite.rotation += pos.rotation
            sprite.draw(spriteBatch)
            sprite.rotation -= pos.rotation
            sprite.translate(-pos.x, -pos.y)
        }
    }

    override fun familyBuilder() = allOf(
        PositionComponent::class,
        SpriteComponent::class
    )
}
