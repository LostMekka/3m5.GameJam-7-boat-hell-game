package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import ktx.ashley.mapperFor
import ktx.box2d.body
import ktx.box2d.box

class HitBoxComponent(
    physicsWorld: World,
    hitBoxWidth: Float,
    hitBoxHeight: Float,
    hitBoxRotation: Float
) : Component {
    val hitBox = physicsWorld.body(BodyDef.BodyType.DynamicBody) {
        box(hitBoxWidth, hitBoxHeight, Vector2.Zero, hitBoxRotation)
        allowSleep = false
    }
    companion object {
        val mapper = mapperFor<HitBoxComponent>()
    }
}
