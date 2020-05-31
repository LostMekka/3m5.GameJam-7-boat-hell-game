package de.lostmekka.gamejam.boathell

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
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
    override fun familyBuilder(): Family.Builder {
        return allOf(PlayerControlledComponent::class, TransformComponent::class)
    }

    override fun update(deltaTime: Float) {
        val player = entities.first()
        if (player != null) {
            val trans = TransformComponent.mapper[player]
            val camera = engine.getSystem<RenderSystem>().camera

            camera.position.add(
                0.033f * (trans.x - camera.position.x),
                0.033f * (trans.y - camera.position.y),
                0f
            )
            camera.update()
        }
    }
}

class GamePlayScreen : KtxScreen {
    private var time = 0f
    private val guiViewport = ScreenViewport(OrthographicCamera())
    private val physicsWorld = createWorld(Vector2.Zero, true)
    private val physicsDebugRenderer = Box2DDebugRenderer(true, true, false, true, false, true)
    private var player: Entity? = null
    private val cameraControl = CameraControl()
    private val renderSystem = RenderSystem()

    val engine = Engine().apply {
        player = Ships.addPlayerBoat(this, physicsWorld)
        createOceanWater(this)

        addSystem(ShipMovementSystem())
        addSystem(WeaponSystem(physicsWorld))
        addSystem(WeaponOwnerSystem())
        addSystem(ProjectileMovementSystem())

        // controllers
        addSystem(PlayerControlledBoatSystem())
        addSystem(AIShipSystem())
        addSystem(cameraControl)

        // special
        addSystem(WaterLayerSystem())
        addSystem(StupidWaterSpawn())
        addSystem(StupidParticleSystem())
        addSystem(renderSystem)
        addSystem(PhysicsUpdateSystem(physicsWorld))
        addSystem(EnemySpawnerSystem(physicsWorld))
    }

    init {
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun scrolled(amount: Int): Boolean {
                val camera = engine.getSystem<RenderSystem>().camera
                camera.zoom *= if (amount > 0) 0.9f else 1.1f
                camera.update()
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
        time += delta
        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        guiViewport.update(width, height, true)
        renderSystem.viewport.update(width, height)
    }

    override fun dispose() {
    }

    private fun draw() {
        val camera = engine.getSystem<RenderSystem>().camera

        @Suppress("ConstantConditionIf")
        if (GameConfig.Debug.drawPhysics) {
            physicsDebugRenderer.render(physicsWorld, camera.combined)
        }
    }
}
