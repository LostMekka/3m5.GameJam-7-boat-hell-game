package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector2
import de.lostmekka.gamejam.boathell.asset.Sounds
import de.lostmekka.gamejam.boathell.asset.Textures
import de.lostmekka.gamejam.boathell.asset.toCenteredSprite
import de.lostmekka.gamejam.boathell.entity.component.*
import de.lostmekka.gamejam.boathell.entity.system.*
import de.lostmekka.gamejam.boathell.pixels
import kotlin.random.Random

fun Engine.addPlayerBoat() = addEntityWithComponents(
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
    WeaponOwnerComponent(this.addBoatFrontCannon1()),
    SoundComponent(
        deathSound = Sounds.awesomeExplosion,
        hitSound = Sounds.hit
    ),
    ShipWaterComp(Vector2((0).pixels, (0).pixels))
)

fun Engine.addAIBoat(x: Float = 0f, y: Float = 0f, rotation: Float = 0f) = addEntityWithComponents(
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
    WeaponOwnerComponent(this.addShip1SideCannons()),
    SoundComponent(
        deathSound = Sounds.awesomeExplosion,
        hitSound = Sounds.hit
    ),
    ShipWaterComp(Vector2((0).pixels, (0).pixels))
)

fun Engine.addAIPlane(x: Float = 0f, y: Float = 0f, rotation: Float = 0f) = addEntityWithComponents(
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
    WeaponOwnerComponent(this.addShip1FrontCannon1()),
    SoundComponent(
        deathSound = Sounds.awesomeExplosion,
        hitSound = Sounds.hit
    )
)


fun Engine.addAIRosetteShip(x: Float = 0f, y: Float = 0f, rotation: Float = 0f) = addEntityWithComponents(
    TransformComponent(x = x, y = y, rotation = rotation),
    RenderComponent(Textures.ship1.toCenteredSprite(), 102),
    HitBoxComponent(
        hitBoxWidth = 2f - 2.pixels,
        hitBoxHeight = 19.pixels,
        hitBoxRotation = 0f,
        category = HitBoxCategory.EnemyBoat
    ),
    HealthComponent(50f),
    WeaponOwnerComponent(this.addShipRosettaCannon(false)),
    ShipMovementComponent(velocity = 0.01f),
    AIShipComponent(AIShipMovementStrategies.followAndCirculatePlayer(6f)),
    SoundComponent(
        deathSound = Sounds.awesomeExplosion,
        hitSound = Sounds.hit
    ),
    ShipWaterComp(Vector2((0).pixels, (0).pixels))
)
