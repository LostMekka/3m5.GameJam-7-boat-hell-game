package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
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
    private val physicsWorld: World,
    private val projectileHitHandler: ProjectileHitHandler
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
        box.contacts.clear()
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

            HitBoxComponent.mapper[e1].contacts.add(e2)
            HitBoxComponent.mapper[e2].contacts.add(e1)

            val family: Family = projectileHitHandler.familyBuilder().get()
            if (family.matches(e1) || family.matches(e2)) {
                val (eMatch, eOther) = if (family.matches(e1)) Pair(e1, e2) else Pair(e2, e1)
                projectileHitHandler.onHit(eMatch, eOther)
            }
        }
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
