package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Affine2
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import de.lostmekka.gamejam.boathell.entity.component.RenderComponent
import de.lostmekka.gamejam.boathell.entity.system.WeaponOwnerComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import kotlin.math.sin

class RenderSystem(camera: Camera) : BaseSystem() {
    val batch = SpriteBatch()
    val viewport = ScreenViewport(camera).apply { unitsPerPixel = 1.0f / 32.0f / 4.0f }

    override fun familyBuilder() = allOf(
        TransformComponent::class,
        RenderComponent::class
    )

    override fun update(deltaTime: Float) {
        val entities = entities.sortedBy { RenderComponent.mapper[it].zLayer }

        clearColor()

        batch.begin()
        batch.projectionMatrix = viewport.camera.combined
        for (entity in entities) {
            val trans = TransformComponent.mapper[entity]
            val render = RenderComponent.mapper[entity]

            val finalTrans = Affine2().translate(trans.x, trans.y).rotate(trans.rotation).mul(render.transform)
            val width = render.texRegion.regionWidth.toFloat() / 32.0f // TODO this divide by 32 should not be necessary
            val height = render.texRegion.regionHeight.toFloat() / 32.0f

            batch.color = render.color
            batch.draw(render.texRegion, width, height, finalTrans)
        }
        batch.end()
    }

    fun clearColor() {
        Gdx.gl.glClearColor(0f, 0.5f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }
}
