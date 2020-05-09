package de.lostmekka.gamejam.boathell

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import ktx.graphics.use

class WaterLayer(val texture: Texture, val speed: Float, val wildness: Float = 0.6f) {
    private val r1 = MathUtils.random()
    private val r2 = MathUtils.random()

    fun draw(t: Float, batch: SpriteBatch) {
        val f = t * wildness
        val offsetX = speed * MathUtils.sin(r1 + f * wildness)
        val offsetY = speed * MathUtils.sin(r2 + f * wildness)
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        val texW = 64f
        val texH = 64f
        batch.draw(texture,
            0f, 0f, w, h,
            offsetX,
            offsetY,
            offsetX + (w / texW) / 4.0f,
            offsetY + (h / texH) / 4.0f)
    }
}

class Water {
    val batch = SpriteBatch()
    val textureWater1 = Texture(Gdx.files.internal("water1.png")).also {
        it.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
    }
    val water1 = WaterLayer(textureWater1, 0.5f, 0.7f)
    val water2 = WaterLayer(textureWater1, 0.4f, 0.7f)
    val water3 = WaterLayer(textureWater1, 0.3f, 0.8f)
    val water4 = WaterLayer(textureWater1, 0.25f, 0.9f)

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
