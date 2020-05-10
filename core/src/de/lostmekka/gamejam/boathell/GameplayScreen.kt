package de.lostmekka.gamejam.boathell

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.lostmekka.gamejam.boathell.entity.Ships
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.system.AIShipSystem
import de.lostmekka.gamejam.boathell.entity.system.EnemySpawnerSystem
import de.lostmekka.gamejam.boathell.entity.system.PhysicsUpdateSystem
import de.lostmekka.gamejam.boathell.entity.system.PlayerControlledBoatSystem
import de.lostmekka.gamejam.boathell.entity.system.ProjectileMovementSystem
import de.lostmekka.gamejam.boathell.entity.system.RenderSystem
import de.lostmekka.gamejam.boathell.entity.system.ShipMovementSystem
import de.lostmekka.gamejam.boathell.entity.system.WeaponOwnerSystem
import de.lostmekka.gamejam.boathell.entity.system.WeaponSystem
import ktx.app.KtxScreen
import ktx.ashley.get
import ktx.box2d.createWorld
import ktx.graphics.use

class GamePlayScreen : KtxScreen {
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    private val water = Water()
    private var time = 0f

    private val guiViewport = ScreenViewport()
    private val someFont = FreeTypeFontGenerator(Gdx.files.internal("fonts/UbuntuMono-R.ttf")).let {
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = 40
        val font = it.generateFont(parameter)
        it.dispose()
        font
    }

    private val physicsWorld = createWorld()
    private val physicsDebugRenderer = Box2DDebugRenderer()

    private val camera = OrthographicCamera().apply { zoom = 1f }
    private val viewport = ScreenViewport(camera).apply { unitsPerPixel = 1.0f / 32.0f / 4.0f }
    private val stage = Stage(viewport)

    private var player: Entity? = null
    private val renderSystem = RenderSystem()
    private val engine = Engine().apply {
        player = Ships.addPlayerBoat(this, physicsWorld)
        Ships.addAIBoat(this, physicsWorld, -5f, -5f)

        addSystem(ShipMovementSystem())
        addSystem(WeaponSystem())
        addSystem(WeaponOwnerSystem())
        addSystem(ProjectileMovementSystem())

        // controllers
        addSystem(PlayerControlledBoatSystem())
        addSystem(AIShipSystem())

        // special
        addSystem(renderSystem)
        addSystem(PhysicsUpdateSystem(physicsWorld))
        addSystem(EnemySpawnerSystem(physicsWorld))
    }

    init {
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun scrolled(amount: Int): Boolean {
                camera.zoom *= if (amount > 0) 0.9f else 1.1f
                camera.update()
                return true
            }
        }
    }

    private fun update(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit()

        time += delta
        engine.update(delta)

        val player = player
        if (player != null) {
            val pos = player.get<PositionComponent>()
            if (pos != null) {
                camera.position.add(
                    0.033f * (pos.x - camera.position.x),
                    0.033f * (pos.y - camera.position.y),
                    0f)
                camera.update()
            }
        }
    }

    private fun draw() {
        water.clearColor()

        batch.use(viewport.camera.combined) {
            water.draw(time, batch)
            renderSystem.render(it)
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Line) {
            // draw shapes
        }

        @Suppress("ConstantConditionIf")
        if (GameConfig.Debug.drawPhysics) {
            physicsDebugRenderer.render(physicsWorld, viewport.camera.combined)
        }
    }

    override fun render(delta: Float) {
        update(delta)
        draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        guiViewport.update(width, height, true)
    }

    override fun dispose() {
        batch.dispose()
    }
}
