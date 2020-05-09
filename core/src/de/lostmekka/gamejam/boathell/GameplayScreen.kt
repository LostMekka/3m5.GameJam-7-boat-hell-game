package de.lostmekka.gamejam.boathell

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.lostmekka.gamejam.boathell.entity.createLittleBoat
import de.lostmekka.gamejam.boathell.entity.system.RenderSystem
import de.lostmekka.gamejam.boathell.entity.system.ShipMovementSystem
import ktx.app.KtxScreen
import ktx.graphics.use

class GamePlayScreen : KtxScreen {
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    val water = Water()
    var time = 0f;

    private val guiViewport = ScreenViewport()
    private val someFont = FreeTypeFontGenerator(Gdx.files.internal("fonts/UbuntuMono-R.ttf")).let {
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = 40
        val font = it.generateFont(parameter)
        it.dispose()
        font
    }

    private val viewport = ExtendViewport(50f, 25f, OrthographicCamera().also { it.zoom = 0.5f })

    private val stage = Stage(viewport).apply {
        // add actors here
    }

    private val renderSystem = RenderSystem()
    private val engine = Engine().apply {
        addEntity(createLittleBoat())
        addSystem(renderSystem)
        addSystem(ShipMovementSystem())
    }

    private fun handleInput() {
    }

    private fun update(delta: Float) {
        time += delta;
        engine.update(delta)
    }

    private fun draw() {
        water.clearColor()
        water.draw(time)

        batch.use {
            it.projectionMatrix = viewport.camera.projection
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
