package de.lostmekka.gamejam.boathell

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.lostmekka.gamejam.boathell.entity.Ships
import de.lostmekka.gamejam.boathell.entity.system.AIShipSystem
import de.lostmekka.gamejam.boathell.entity.system.PlayerControlledBoatSystem
import de.lostmekka.gamejam.boathell.entity.system.ProjectileMovementSystem
import de.lostmekka.gamejam.boathell.entity.system.RenderSystem
import de.lostmekka.gamejam.boathell.entity.system.ShipMovementSystem
import de.lostmekka.gamejam.boathell.entity.system.WeaponOwnerSystem
import de.lostmekka.gamejam.boathell.entity.system.WeaponSystem
import ktx.app.KtxScreen
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

    val camera = OrthographicCamera().apply { zoom = 4f }
    private val viewport = ScreenViewport(camera).apply { unitsPerPixel = 1.0f / 32.0f / 4.0f }
    private val stage = Stage(viewport)
    private val renderSystem = RenderSystem()
    private val engine = Engine().apply {
        Ships.addPlayerBoat(this)
        Ships.addAIBoat(this, -5f, -5f)

        addSystem(renderSystem)
        addSystem(ShipMovementSystem())
        addSystem(WeaponSystem())
        addSystem(WeaponOwnerSystem())
        addSystem(ProjectileMovementSystem())

        // controllers
        addSystem(PlayerControlledBoatSystem())
        addSystem(AIShipSystem())
    }

    init {
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun scrolled(amount: Int): Boolean {
                camera.zoom *= if (amount > 0) 0.9f else 1.1f
                println(camera.zoom)
                camera.update()
                return true
            }
        }
    }

    private fun update(delta: Float) {
        time += delta
        engine.update(delta)
    }

    private fun draw() {
        water.clearColor()
        water.draw(time)

        batch.use(viewport.camera.projection) {
            renderSystem.render(it)
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Line) {
            // draw shapes
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
        water.dispose()
        batch.dispose()
    }
}
