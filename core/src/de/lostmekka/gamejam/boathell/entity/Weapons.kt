package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent
import de.lostmekka.gamejam.boathell.entity.component.WeaponComponent

object Weapons {
    fun addWeapon(
        engine: Engine,
        textureRegion: TextureRegion,
        offsetX: Float,
        offsetY: Float,
        offsetAngle: Float,
        cooldownTime: Float,
        projectileInit: WeaponComponent.ShotContext.() -> Unit
    ) = engine.addEntityWithComponents(
        PositionComponent(0f, 0f, 0f), // will be auto set by weapon owner system
        SpriteComponent(Sprite(textureRegion)),
        WeaponComponent(cooldownTime, offsetX, offsetY, offsetAngle, projectileInit)
    )
}
