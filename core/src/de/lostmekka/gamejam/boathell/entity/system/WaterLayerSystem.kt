package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import de.lostmekka.gamejam.boathell.entity.component.RenderComponent
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import kotlin.math.sin
import kotlin.random.Random

class WaterLayerSystem : BaseSystem() {
    var time = 0.0f

    override fun familyBuilder() = allOf(
        WaterLayerComponent::class,
        RenderComponent::class,
        TransformComponent::class
    )

    override fun update(deltaTime: Float) {
        time += deltaTime
        for (entity in entities) {
            val water = WaterLayerComponent.mapper[entity]
            val render = RenderComponent.mapper[entity]
            val trans = TransformComponent.mapper[entity]

            val f = time * water.wildness
            val offsetX = water.speed * sin(water.r1 + f * water.wildness)
            val offsetY = water.speed * sin(water.r2 + f * water.wildness)
            val w = Gdx.graphics.width.toFloat()
            val h = Gdx.graphics.height.toFloat()

            trans.x = -0.5f * w
            trans.y = -0.5f * h
            render.texRegion.u = offsetX
            render.texRegion.v = offsetY
            render.texRegion.u2 = offsetX + w
            render.texRegion.v2 = offsetY + h
        }
    }
}

class WaterLayerComponent(
    val speed: Float,
    val wildness: Float,
    val r1: Float = Random.nextFloat(),
    val r2: Float = Random.nextFloat())
    : Component {
    companion object {
        val mapper = mapperFor<WaterLayerComponent>()
    }
}

fun createOceanWater(engine: Engine) {
    val textureWater1 = Texture(Gdx.files.internal("water1.png")).also {
        it.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
    }

    engine.addEntity(Entity()
        .add(WaterLayerComponent(0.5f, 0.7f))
        .add(RenderComponent(TextureRegion(textureWater1), 0, Color(0.2f, 0.4f, 0.5f, 1.0f)))
        .add(TransformComponent()))

    engine.addEntity(Entity()
        .add(WaterLayerComponent(0.4f, 0.7f))
        .add(RenderComponent(TextureRegion(textureWater1), 1, Color(0.35f, 0.45f, 0.9f, 1.0f)))
        .add(TransformComponent()))

    engine.addEntity(Entity()
        .add(WaterLayerComponent(0.3f, 0.8f))
        .add(RenderComponent(TextureRegion(textureWater1), 2, Color(0.4f, 0.6f, 1.0f, 1.0f)))
        .add(TransformComponent()))

    engine.addEntity(Entity()
        .add(WaterLayerComponent(0.25f, 0.9f))
        .add(RenderComponent(TextureRegion(textureWater1), 3, Color(0.4f, 0.6f, 1.0f, 1.0f)))
        .add(TransformComponent()))
}
