package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.World
import de.lostmekka.gamejam.boathell.asset.Sounds
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.asset.toCenteredSprite
import de.lostmekka.gamejam.boathell.entity.addEntityWithComponents
import de.lostmekka.gamejam.boathell.entity.addStraightProjectile
import de.lostmekka.gamejam.boathell.entity.component.*
import de.lostmekka.gamejam.boathell.pixels
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import kotlin.math.max
import kotlin.math.min

fun offsetPositionForParentRotation(weapon: WeaponComponent, parentRotation: Float): Vector3 =
    Vector3(weapon.offsetX, weapon.offsetY, 0f).rotate(Vector3.Z, parentRotation)

class WeaponSystem(
    private val physicsWorld: World
) : BaseSystem() {

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(allOf(ProjectileMovementComponent::class).get(), projectileListener)
    }

    private val projectileListener = object : EntityListener {
        override fun entityRemoved(entity: Entity) {
        }
        override fun entityAdded(entity: Entity) {
            // playing sounds here is not a good idea :drunk:
        }
    }

    override fun updateEntity(entity: Entity, deltaTime: Float) {
        val weapon = WeaponComponent.mapper[entity]
        weapon.cooldownCounter = max(weapon.cooldownCounter - deltaTime, 0f)
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val weapon = WeaponComponent.mapper[entity]
            val parent = weapon.parent ?: continue
            if (weapon.isFiring) {
                val parentTransform = TransformComponent.mapper[parent]
                val movement = ShipMovementComponent.mapper[parent]

                val pos = offsetPositionForParentRotation(weapon, parentTransform.rotation)
                val vel = Vector3(movement.velocity, 0f, 0f).rotate(Vector3.Z, parentTransform.rotation)

                val context = ShotContext(
                    x = parentTransform.x + pos.x,
                    y = parentTransform.y + pos.y,
                    angle = parentTransform.rotation + weapon.offsetAngle,
                    movementVelocity = vel,
                    firingTime = weapon.firingTime,
                    deltaTime = deltaTime,
                    engine = engine,
                    physicsWorld = physicsWorld
                )
                weapon.firingTime += deltaTime
                if (weapon.projectileInit(context)) {
                    weapon.isFiring = false
                    weapon.firingTime = 0f
                }
            } else {
                weapon.cooldownCounter = max(weapon.cooldownCounter - deltaTime, 0f)
            }
        }
    }

    override fun familyBuilder() = allOf(
        TransformComponent::class,
        WeaponComponent::class
    )
}

typealias WeaponTriggerStrategy = ShotContext.() -> Boolean

object WeaponTriggerStrategies {
    fun boring(): WeaponTriggerStrategy = {
        val color = Color.YELLOW
        val category = HitBoxCategory.EnemyProjectile
        for (i in 0..5) engine.addStraightProjectile(this, color, category, i.toFloat() * 0.016f, 1f, 3f, 3f)
        true
    }

    fun fast(): WeaponTriggerStrategy = {
        val color = Color.DARK_GRAY
        val category = HitBoxCategory.PlayerProjectile
        for (i in 0..5) engine.addStraightProjectile(this, color, category, i.toFloat() * 0.016f, 1f, 3f, 10f)
        true
    }

    fun rosette(isPlayerWeapon: Boolean): WeaponTriggerStrategy = {
        val color = Color.RED
        val category = if (isPlayerWeapon) HitBoxCategory.PlayerProjectile else HitBoxCategory.EnemyProjectile
        val totalShots = 200
        val waitTime = 0.04f
        val projectilesFired = (firingTime / waitTime).toInt()
        val projectilesCouldHaveFired = ((firingTime + deltaTime) / waitTime).toInt()
        val projectilesToFire = min(totalShots, projectilesCouldHaveFired) - projectilesFired
        if (projectilesToFire > 0 && projectilesFired % 2 == 0) Sounds.shoot.play(playerDistance())
        for (i in 1..projectilesToFire) {
            val angleOffset = (i + projectilesFired) * 137.5f
            val ctx = this.copy(angle = angle + angleOffset)
            engine.addStraightProjectile(ctx, color, category, i.toFloat() * 0.04f, 0.5f, 10f, 1.7f)
        }
        projectilesToFire + projectilesFired >= totalShots
    }
}

class WeaponComponent(
    var cooldownTime: Float,
    var offsetX: Float,
    var offsetY: Float,
    var offsetAngle: Float,
    var isFiring: Boolean = false,
    var firingTime: Float = 0f,
    var projectileInit: WeaponTriggerStrategy,
    var cooldownCounter: Float = 0f,
    var parent: Entity? = null
) : Component {

    fun shoot(): Boolean {
        return if (cooldownCounter <= 0f && !isFiring) {
            cooldownCounter = cooldownTime
            isFiring = true
            true
        } else {
            false
        }
    }

    companion object {
        val mapper = mapperFor<WeaponComponent>()
    }
}

data class ShotContext(
    val x: Float,
    val y: Float,
    val angle: Float,
    val movementVelocity: Vector3,
    var firingTime: Float,
    val deltaTime: Float,
    val engine: Engine,
    val physicsWorld: World
) {
    fun playerDistance(): Float {
        val playerEntities = engine.getEntitiesFor(allOf(PlayerControlledComponent::class, TransformComponent::class).get())
        val player = playerEntities.firstOrNull()
        if (player != null) {
            val pos = TransformComponent.mapper[player]
            return Vector2(pos.x, pos.y).sub(Vector2(x, y)).len()
        } else {
            return Float.POSITIVE_INFINITY
        }
    }
}
