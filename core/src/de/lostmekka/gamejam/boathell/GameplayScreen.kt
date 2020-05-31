package de.lostmekka.gamejam.boathell

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.lostmekka.gamejam.boathell.asset.Music
import de.lostmekka.gamejam.boathell.entity.Ships
import de.lostmekka.gamejam.boathell.entity.component.RenderComponent
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import de.lostmekka.gamejam.boathell.entity.system.*
import ktx.app.KtxScreen
import ktx.ashley.*
import ktx.box2d.createWorld
import kotlin.math.sin

class GamePlayScreen : KtxScreen {
    private var time = 0f
    private val guiViewport = ScreenViewport(OrthographicCamera())
    private val physicsWorld = createWorld(Vector2.Zero, true)
    private val physicsDebugRenderer = Box2DDebugRenderer(true, true, false, true, false, true)
    private var player: Entity? = null
    private val renderSystem = RenderSystem()
    private val particleSystem = StupidParticleSystem()

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

        // special
        addSystem(WaterLayerSystem())
        addSystem(StupidWaterSpawn())
        addSystem(particleSystem)
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
        cameraFollowsPlayer()
        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        guiViewport.update(width, height, true)
        renderSystem.viewport.update(width, height)
    }

    override fun dispose() {
    }

    fun cameraFollowsPlayer() {
        val player = player
        if (player != null) {
            val pos = player.get<TransformComponent>()
            if (pos != null) {
                val camera = engine.getSystem<RenderSystem>().camera
                camera.position.add(
                    0.033f * (pos.x - camera.position.x),
                    0.033f * (pos.y - camera.position.y),
                    0f
                )
                camera.update()
            }
        }
    }

    private fun draw() {
        val camera = engine.getSystem<RenderSystem>().camera

        @Suppress("ConstantConditionIf")
        if (GameConfig.Debug.drawPhysics) {
            physicsDebugRenderer.render(physicsWorld, camera.combined)
        }
    }
}
