package de.lostmekka.gamejam.boathell

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.lostmekka.gamejam.boathell.asset.Music
import de.lostmekka.gamejam.boathell.entity.Ships
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import de.lostmekka.gamejam.boathell.entity.system.*
import ktx.app.KtxScreen
import ktx.ashley.*
import ktx.box2d.createWorld

class CameraControl : BaseSystem() {
    val camera = OrthographicCamera().apply { zoom = 2f }

    override fun familyBuilder(): Family.Builder {
        return allOf(PlayerControlledComponent::class, TransformComponent::class)
    }

    override fun update(deltaTime: Float) {
        val player = entities.first()
        if (player != null) {
            val trans = TransformComponent.mapper[player]

            camera.position.add(
                0.033f * (trans.x - camera.position.x),
                0.033f * (trans.y - camera.position.y),
                0f
            )
            camera.update()
        }
    }

    fun zoomBy(amount: Int) {
        camera.zoom *= if (amount > 0) 0.9f else if (amount < 0) 1.1f else 1.0f
        camera.update()
    }
}

class PhysicsDebugRenderer(val camera: Camera, val world: World) : EntitySystem() {
    private val renderer = Box2DDebugRenderer(true, true, false, true, false, true)

    override fun update(deltaTime: Float) {
        @Suppress("ConstantConditionIf")
        if (GameConfig.Debug.drawPhysics) {
            renderer.render(world, camera.combined)
        }
    }
}

class GamePlayScreen : KtxScreen {
    val engine = Engine().apply {
        val physicsWorld = createWorld(Vector2.Zero, true)

        Ships.addPlayerBoat(this, physicsWorld)
        createOceanWater(this)

        addSystem(ShipMovementSystem())
        addSystem(WeaponSystem(physicsWorld))
        addSystem(WeaponOwnerSystem())
        addSystem(ProjectileMovementSystem())

        // controllers
        addSystem(PlayerControlledBoatSystem())
        addSystem(AIShipSystem())
        val cameraControl = CameraControl()
        addSystem(cameraControl)

        // special
        addSystem(WaterLayerSystem())
        addSystem(StupidWaterSpawn())
        addSystem(StupidParticleSystem())
        addSystem(RenderSystem(cameraControl.camera))
        addSystem(PhysicsUpdateSystem(physicsWorld))
        addSystem(EnemySpawnerSystem(physicsWorld))
        addSystem(PhysicsDebugRenderer(cameraControl.camera, physicsWorld))

        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun scrolled(amount: Int): Boolean {
                cameraControl.zoomBy(amount)
                return true
            }

            override fun keyDown(keycode: Int): Boolean {
                when (keycode) {
                    Input.Keys.ESCAPE -> Gdx.app.exit()
                }
                return false
            }
        }
    }

    override fun show() {
        // Music.loop.play()
    }

    override fun hide() {
        Music.loop.pause()
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        try {
            val render = engine.getSystem<RenderSystem>()
            render.viewport.update(width, height)
        } catch (err: MissingEntitySystemException) {
            // ignore
        }
    }
}
