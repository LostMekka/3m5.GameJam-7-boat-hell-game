package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent


fun createLittleBoat(): Entity {
    val littleBoat = Entity()
    littleBoat.add(PositionComponent(x = -2.5f, y = -2.5f, rotation = 0f))
    val texture = Texture(Gdx.files.internal("boat1.png"))
    val sprite = Sprite(texture)
    littleBoat.add(SpriteComponent(sprite))
    littleBoat.add(ShipMovementComponent())
    return littleBoat
}
