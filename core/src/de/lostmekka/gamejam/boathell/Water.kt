package de.lostmekka.gamejam.boathell

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import ktx.graphics.use

class WaterLayer(val texture: Texture) {
    private val r1 = MathUtils.random()
    private val r2 = MathUtils.random()

    fun draw(t: Float, batch: SpriteBatch) {
        val offsetX = r1 * MathUtils.sin(r1 + t)
        val offsetY = r2 * MathUtils.sin(r2 + t)
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        val texW = 64f
        val texH = 64f
        batch.draw(texture,
            0f, 0f, w, h,
            offsetX,
            offsetY,
            offsetX + w / texW,
            offsetY + h / texH)
    }
}

class Water {
    val batch = SpriteBatch()
    val textureWater1 = Texture(Gdx.files.internal("water1.png")).also {
        it.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
    }
    val water1 = WaterLayer(textureWater1)
    val water2 = WaterLayer(textureWater1)
    val water3 = WaterLayer(textureWater1)
    val water4 = WaterLayer(textureWater1)

    fun clearColor() {
        Gdx.gl.glClearColor(0f, 0.5f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    fun draw(t: Float) {
        batch.use {
            batch.setColor(0.2f, 0.4f, 0.5f, 1.0f)
            water1.draw(t, batch);
            batch.setColor(0.35f, 0.45f, 0.9f, 1.0f)
            water2.draw(t, batch);
            batch.setColor(0.4f, 0.6f, 1.0f, 1.0f)
            water3.draw(t, batch);
            batch.setColor(0.4f, 0.6f, 1.0f, 1.0f)
            water4.draw(t, batch);
        }
    }

    fun dispose() {
        batch.dispose()
    }
}
