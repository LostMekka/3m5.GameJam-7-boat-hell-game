package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import de.lostmekka.gamejam.boathell.GameConfig

class PhysicsDebugRenderer(val camera: Camera, val world: World) : EntitySystem() {
    private val renderer = Box2DDebugRenderer(true, true, false, true, false, true)

    override fun update(deltaTime: Float) {
        @Suppress("ConstantConditionIf")
        if (GameConfig.Debug.drawPhysics) {
            renderer.render(world, camera.combined)
        }
    }
}
