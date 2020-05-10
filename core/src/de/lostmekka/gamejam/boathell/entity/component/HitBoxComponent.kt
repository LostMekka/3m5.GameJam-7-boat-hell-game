package de.lostmekka.gamejam.boathell.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import ktx.ashley.mapperFor
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.filter
import kotlin.experimental.and
import kotlin.experimental.or

enum class HitBoxCategory(val bits: Short) {
    EnemyProjectile(1 shl 0),
    PlayerProjectile(1 shl 1),
    EnemyBoat(1 shl 2),
    PlayerBoat(1 shl 3),
    EnemyAir(1 shl 4),
    PlayerAir(1 shl 5),
    ;

    val mask by lazy {
        when (this) {
            EnemyProjectile -> PlayerBoat + PlayerAir
            PlayerProjectile -> EnemyBoat + EnemyAir
            EnemyBoat -> PlayerProjectile + EnemyBoat + PlayerBoat
            PlayerBoat -> EnemyProjectile + EnemyBoat + PlayerBoat
            EnemyAir -> PlayerProjectile + PlayerAir
            PlayerAir -> EnemyProjectile + PlayerAir
        }
    }

    operator fun HitBoxCategory.plus(other: HitBoxCategory) = bits or other.bits
    operator fun Short.plus(other: HitBoxCategory) = this or other.bits
}

class HitBoxComponent(
    physicsWorld: World,
    hitBoxWidth: Float,
    hitBoxHeight: Float,
    hitBoxRotation: Float,
    category: HitBoxCategory
) : Component {
    val hitBox = physicsWorld.body(BodyDef.BodyType.DynamicBody) {
        box(hitBoxWidth, hitBoxHeight, Vector2.Zero, hitBoxRotation) {
            filter {
                categoryBits = category.bits
                maskBits = category.mask
                println("creating fixture with ${category.bits} / ${category.mask}")
            }
        }
        allowSleep = false
    }

    companion object {
        val mapper = mapperFor<HitBoxComponent>()
    }
}
