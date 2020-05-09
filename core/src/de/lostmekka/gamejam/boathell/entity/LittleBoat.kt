package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent

fun createLittleBoat(engine: Engine): Entity {
    val littleBoat = Entity()
    littleBoat.add(PositionComponent(x = -2.5f, y = -2.5f, rotation = 0))
    engine.addEntity(littleBoat)
    return littleBoat
}
