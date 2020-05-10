package de.lostmekka.gamejam.boathell.entity.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.World
import de.lostmekka.gamejam.boathell.GameConfig
import de.lostmekka.gamejam.boathell.entity.component.HealthComponent
import de.lostmekka.gamejam.boathell.entity.component.HitBoxComponent
import de.lostmekka.gamejam.boathell.entity.component.PositionComponent
import de.lostmekka.gamejam.boathell.entity.component.ProjectileMovementComponent
import de.lostmekka.gamejam.boathell.toDegrees
import de.lostmekka.gamejam.boathell.toRadians
import ktx.ashley.allOf
import ktx.ashley.get

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
        val pos = PositionComponent.mapper[entity]
        box.hitBox.apply {
            setTransform(pos.x, pos.y, pos.rotation.toRadians())
            for (fixture in fixtureList) {
                fixture.userData = entity
            }
        }
    }

    private fun postPhysics(entity: Entity) {
        val box = HitBoxComponent.mapper[entity]
        val pos = PositionComponent.mapper[entity]
        pos.apply {
            val physicsPos = box.hitBox.position
            x = physicsPos.x
            y = physicsPos.y
            rotation = box.hitBox.angle.toDegrees()
        }
    }

    override fun familyBuilder() = allOf(
        HitBoxComponent::class,
        PositionComponent::class
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
            if (healthComponent.health <= 0) engine.removeEntity(ship)
        }

        private val Entity.projectile get() = this[ProjectileMovementComponent.mapper]?.let { this to it }
        private val Entity.health get() = this[HealthComponent.mapper]?.let { this to it }
    }

    private val cleanupEntityListener = object : EntityListener {
        override fun entityAdded(entity: Entity) {}
        override fun entityRemoved(entity: Entity) {
            val body = HitBoxComponent.mapper[entity].hitBox
            physicsWorld.destroyBody(body)
        }
    }
}
