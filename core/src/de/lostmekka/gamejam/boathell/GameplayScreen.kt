package de.lostmekka.gamejam.boathell

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.lostmekka.gamejam.boathell.asset.Music
import de.lostmekka.gamejam.boathell.entity.Ships
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.system.*
import ktx.app.KtxScreen
import ktx.ashley.*
import ktx.box2d.createWorld
import ktx.graphics.use
import kotlin.reflect.typeOf

class GamePlayScreen : KtxScreen {
    private var time = 0f
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    private val water = Water()
    private val guiViewport = ScreenViewport(OrthographicCamera())
    private val physicsWorld = createWorld(Vector2.Zero, true)
    private val physicsDebugRenderer = Box2DDebugRenderer(true, true, false, true, false, true)
    private var player: Entity? = null
    private val renderSystem = RenderSystem()
    private val particleSystem = StupidParticleSystem()

    val engine = Engine().apply {
        player = Ships.addPlayerBoat(this, physicsWorld)

        addSystem(ShipMovementSystem())
        addSystem(WeaponSystem(physicsWorld))
        addSystem(WeaponOwnerSystem())
        addSystem(ProjectileMovementSystem())

        // controllers
        addSystem(PlayerControlledBoatSystem())
        addSystem(AIShipSystem())

        // special
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
        Music.loop.play()
    }

    override fun render(delta: Float) {
        time += delta
        engine.update(delta)
        //update(delta)
        //draw()
    }

    override fun resize(width: Int, height: Int) {
        guiViewport.update(width, height, true)
    }

    override fun dispose() {
        batch.dispose()
    }

    private fun update(delta: Float) {
        time += delta
        engine.update(delta)

        val player = player
        if (player != null) {
            val pos = player.get<PositionComponent>()
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
        water.clearColor()

        val camera = engine.getSystem<RenderSystem>().camera
        batch.use(camera.combined) {
            water.draw(time, batch)
            particleSystem.draw(batch)
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Line) {
            // draw shapes
        }

        @Suppress("ConstantConditionIf")
        if (GameConfig.Debug.drawPhysics) {
            physicsDebugRenderer.render(physicsWorld, camera.combined)
        }
    }
}
