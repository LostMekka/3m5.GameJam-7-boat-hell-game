package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import de.lostmekka.gamejam.boathell.asset.Sounds
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.asset.toCenteredSprite
import de.lostmekka.gamejam.boathell.entity.system.AIShipComponent
import de.lostmekka.gamejam.boathell.entity.component.HealthComponent
import de.lostmekka.gamejam.boathell.entity.component.HitBoxCategory
import de.lostmekka.gamejam.boathell.entity.component.HitBoxComponent
import de.lostmekka.gamejam.boathell.entity.component.PlayerControlledComponent
import de.lostmekka.gamejam.boathell.entity.component.TransformComponent
import de.lostmekka.gamejam.boathell.entity.component.ShipMovementComponent
import de.lostmekka.gamejam.boathell.entity.component.SoundComponent
import de.lostmekka.gamejam.boathell.entity.component.RenderComponent
import de.lostmekka.gamejam.boathell.entity.component.WeaponOwnerComponent
import de.lostmekka.gamejam.boathell.entity.component.*
import de.lostmekka.gamejam.boathell.entity.system.AIShipMovementStrategies
import de.lostmekka.gamejam.boathell.normalizeAngleDeg
import de.lostmekka.gamejam.boathell.pixels
import ktx.ashley.get
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.abs
import kotlin.math.sign
import kotlin.random.Random

object Ships {
    fun addPlayerBoat(engine: Engine): Entity {
        return engine.addEntityWithComponents(
            TransformComponent(x = 0f, y = 0f, rotation = Random.nextFloat() * 0f),
            RenderComponent(Textures.boat1.toCenteredSprite(), 100),
            HitBoxComponent(
                hitBoxWidth = 28.pixels,
                hitBoxHeight = 14.pixels,
                hitBoxRotation = 0f,
                category = HitBoxCategory.PlayerBoat
            ),
            HealthComponent(100f),
            ShipMovementComponent(),
            PlayerControlledComponent(),
            WeaponOwnerComponent(
                Weapons.addBoatFrontCannon1(engine)
            ),
            SoundComponent(
                deathSound = Sounds.awesomeExplosion,
                hitSound = Sounds.hit
            ),
            ShipWaterComp(Vector2((0).pixels, (0).pixels))
        )
    }

    fun addAIBoat(engine: Engine, x: Float = 0f, y: Float = 0f, rotation: Float = 0f) {
        engine.addEntityWithComponents(
            TransformComponent(x = x, y = y, rotation = rotation),
            RenderComponent(Textures.ship1.toCenteredSprite(), 102),
            HitBoxComponent(
                hitBoxWidth = 2f - 2.pixels,
                hitBoxHeight = 19.pixels,
                hitBoxRotation = 0f,
                category = HitBoxCategory.EnemyBoat
            ),
            HealthComponent(50f),
            ShipMovementComponent(velocity = 0.025f),
            AIShipComponent(AIShipMovementStrategies.followAndCirculatePlayer()),
            WeaponOwnerComponent(
                Weapons.addShip1SideCannons(engine)
            ),
            SoundComponent(
                deathSound = Sounds.awesomeExplosion,
                hitSound = Sounds.hit
            ),
            ShipWaterComp(Vector2((0).pixels, (0).pixels))
        )
    }

    fun addAIPlane(engine: Engine, x: Float = 0f, y: Float = 0f, rotation: Float = 0f) {
        engine.addEntityWithComponents(
            TransformComponent(x = x, y = y, rotation = rotation),
            RenderComponent(Textures.plane1.toCenteredSprite(), 5000),
            HitBoxComponent(
                hitBoxWidth = 16.pixels,
                hitBoxHeight = 16.pixels,
                hitBoxRotation = 0f,
                category = HitBoxCategory.EnemyAir
            ),
            HealthComponent(10f),
            ShipMovementComponent(velocity = 0.15f),
            AIShipComponent(AIShipMovementStrategies.flyDirectlyToAndAwayFromPlayer()),
            WeaponOwnerComponent(
                Weapons.addShip1FrontCannon1(engine)
            ),
            SoundComponent(
                deathSound = Sounds.awesomeExplosion,
                hitSound = Sounds.hit
            )
        )
    }

    fun addAIRosetteShip(engine: Engine, x: Float = 0f, y: Float = 0f, rotation: Float = 0f) {
        engine.addEntityWithComponents(
            TransformComponent(x = x, y = y, rotation = rotation),
            RenderComponent(Textures.ship1.toCenteredSprite(), 102),
            HitBoxComponent(
                hitBoxWidth = 2f - 2.pixels,
                hitBoxHeight = 19.pixels,
                hitBoxRotation = 0f,
                category = HitBoxCategory.EnemyBoat
            ),
            HealthComponent(50f),
            WeaponOwnerComponent(
                Weapons.addShipRosettaCannon(engine, false)
            ),
            ShipMovementComponent(velocity = 0.01f),
            AIShipComponent(AIShipMovementStrategies.followAndCirculatePlayer(6f)),
            SoundComponent(
                deathSound = Sounds.awesomeExplosion,
                hitSound = Sounds.hit
            ),
            ShipWaterComp(Vector2((0).pixels, (0).pixels))
        )
    }
}
