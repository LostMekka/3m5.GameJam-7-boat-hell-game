package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Sprite
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.SpriteComponent

fun createLittleBoat(): Entity {
    val littleBoat = Entity()
    littleBoat.add(PositionComponent(x = -2.5f, y = -2.5f, rotation = 0f))
    littleBoat.add(SpriteComponent(Sprite(Textures.boat1)))
    littleBoat.add(ShipMovementComponent())
    return littleBoat
}
