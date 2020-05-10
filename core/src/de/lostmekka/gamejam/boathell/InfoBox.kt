package de.lostmekka.gamejam.boathell

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor

class InfoBox(
    private val region: TextureRegion = TextureRegion()
): Actor() {

    override fun draw (batch: Batch, parentAlpha: Float) {
        batch.setColor(0f, 0f, 0f, 0f * parentAlpha)
        batch.draw(region, 0f, 0f, originX, originY,
            width, height, scaleX, scaleY, rotation)
    }
}
