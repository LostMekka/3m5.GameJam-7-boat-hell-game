package de.lostmekka.gamejam.boathell

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.lostmekka.gamejam.boathell.entity.Ships
import de.lostmekka.gamejam.boathell.entity.system.*
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

    private val viewport = ScreenViewport(OrthographicCamera()).also { it.unitsPerPixel = 1.0f / 32.0f / 4.0f }

    private val stage = Stage(viewport).apply {
        // add actors here
    }

    private val renderSystem = RenderSystem()
    private val engine = Engine().apply {
        Ships.addPlayerBoat(this)
        Ships.addAIBoat(this, 10f, 10f)

        addSystem(renderSystem)
        addSystem(ShipMovementSystem())
        addSystem(WeaponsSystem())
        addSystem(PlayerControlledBoatSystem())
        addSystem(AIShipSystem())
    }

    private fun handleInput() {
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
        handleInput()
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
