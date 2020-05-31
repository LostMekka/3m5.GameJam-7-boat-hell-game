package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.physics.box2d.*
import de.lostmekka.gamejam.boathell.GameConfig
import de.lostmekka.gamejam.boathell.entity.addExplosion
import de.lostmekka.gamejam.boathell.entity.component.*
import de.lostmekka.gamejam.boathell.toDegrees
import de.lostmekka.gamejam.boathell.toRadians
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.box2d.create

class PhysicsUpdateSystem(
    private val physicsWorld: World
) : BaseSystem(10) {
    private val stepTime = 1f / GameConfig.Physics.stepsPerSecond.toFloat()
    private var counter = 0f

    private val cleanupFamily = allOf(HitBoxComponent::class).get()

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        physicsWorld.setContactListener(contactListener)
        engine.addEntityListener(cleanupFamily, cleanupEntityListener)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        physicsWorld.setContactListener(null)
        engine.removeEntityListener(cleanupEntityListener)
    }

    override fun update(deltaTime: Float) {
        counter += deltaTime
        while (counter >= stepTime) {
            counter -= stepTime
            entities.forEach { prePhysics(it) }
            physicsWorld.step(deltaTime, GameConfig.Physics.velocityIterations, GameConfig.Physics.positionIterations)
            entities.forEach { postPhysics(it) }
        }
    }

    private fun prePhysics(entity: Entity) {
        val box = HitBoxComponent.mapper[entity]
        val pos = TransformComponent.mapper[entity]
        box.body?.apply {
            setTransform(pos.x, pos.y, pos.rotation.toRadians())
        }
    }

    private fun postPhysics(entity: Entity) {
        val box = HitBoxComponent.mapper[entity]
        val pos = TransformComponent.mapper[entity]
        box.body?.apply {
            pos.x = position.x
            pos.y = position.y
            pos.rotation = angle.toDegrees()
        }
    }

    override fun familyBuilder() = allOf(
        HitBoxComponent::class,
        TransformComponent::class
    )

    private val contactListener = object : ContactListener {
        override fun endContact(contact: Contact) {}
        override fun preSolve(contact: Contact, oldManifold: Manifold) {}
        override fun postSolve(contact: Contact, impulse: ContactImpulse) {}

        override fun beginContact(contact: Contact) {
            val e1 = contact.fixtureA.userData as? Entity ?: return
            val e2 = contact.fixtureB.userData as? Entity ?: return

            // this assumes that there are no entities with health AND projectile movement
            val (projectile, pComponent) = e1.projectile ?: e2.projectile ?: return
            val (ship, healthComponent) = e1.health ?: e2.health ?: return

            engine.removeEntity(projectile)
            healthComponent.health -= pComponent.damage
            val sounds = ship[SoundComponent.mapper]
            if (healthComponent.health <= 0) {
                val trans = TransformComponent.mapper[ship]
                engine.addExplosion(trans.vec())
                removeWeapons(ship, engine)
                engine.removeEntity(ship)
                sounds?.deathSound?.play()
            } else {
                sounds?.hitSound?.play()
            }
        }

        private val Entity.projectile get() = this[ProjectileMovementComponent.mapper]?.let { this to it }
        private val Entity.health get() = this[HealthComponent.mapper]?.let { this to it }
    }

    private val cleanupEntityListener = object : EntityListener {
        override fun entityAdded(entity: Entity) {
            val hitbox = HitBoxComponent.mapper[entity]
            val body = physicsWorld.create(hitbox.def)

            hitbox.body = body
            for (fixture in body.fixtureList) {
                fixture.userData = entity
            }
        }

        override fun entityRemoved(entity: Entity) {
            val body = HitBoxComponent.mapper[entity].body
            if (body != null) {
                physicsWorld.destroyBody(body)
            }
        }
    }
}

fun removeWeapons(e: Entity, engine: Engine) {
    val comp = WeaponOwnerComponent.mapper[e]
    if (comp != null) {
        for (w in comp.weaponEntities) {
            engine.removeEntity(w)
        }
    }
}
