package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Affine2
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.asset.toCenteredSprite
import de.lostmekka.gamejam.boathell.entity.component.HitBoxCategory
import de.lostmekka.gamejam.boathell.entity.component.HitBoxComponent
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import de.lostmekka.gamejam.boathell.entity.system.*
import de.lostmekka.gamejam.boathell.pixels

fun Engine.addEntityWithComponents(vararg components: Component) =
    entityWithComponents(*components).also { addEntity(it) }

fun entityWithComponents(vararg components: Component) =
    Entity().apply { for (it in components) add(it) }

fun Engine.addWaterSplash(pos: Vector2, vel: Vector2 = Vector2.Zero) = addEntityWithComponents(
    WaterParticlesComponent(Vector2(pos.x, pos.y), vel, Color(0.7f, 0.9f, 1f, 1f)),
    RenderComponent(Textures.explosion1[0], 90),
    TransformComponent(pos.x, pos.y, 0f)
)

fun Engine.addExplosion(pos: Vector2) {
    for (i in 0..40) {
        val vel = Vector2(MathUtils.random(0.2f, 1.5f), 0f).rotate(MathUtils.random(360f))
        addEntityWithComponents(
            WaterParticlesComponent(Vector2(pos), vel, Color(1.0f, 0.6f + 0.3f * MathUtils.random(), 0f, 1f)),
            RenderComponent(Textures.explosion1[0], 91),
            TransformComponent(pos.x, pos.y, 0f)
        )
    }
}

fun Engine.addStraightProjectile(ctx: ShotContext, color: Color, category: HitBoxCategory, waitTime: Float, damage: Float, maxLifeTime: Float, speed: Float) {
    val projectile = addEntityWithComponents(
        TransformComponent(ctx.x, ctx.y, ctx.angle),
        RenderComponent(Textures.projectile[0].toCenteredSprite(), 999, color),
        ProjectileMovementComponent(damage, waitTime, maxLifeTime, ProjectileMovementStrategies.straight(ctx.angle, speed, ctx.movementVelocity)),
        HitBoxComponent(4.pixels, 4.pixels, 0f, category)
    )
    // simple shadow
    addEntityWithComponents(
        TransformComponent(),
        TrackingComponent(projectile, Affine2().translate(0f, -0.3f)),
        RenderComponent(Textures.projectile[0].toCenteredSprite(), 20, Color(0f, 0f, 0f, 0.1f))
    )
}
