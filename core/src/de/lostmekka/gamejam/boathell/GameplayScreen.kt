package de.lostmekka.gamejam.boathell

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import de.lostmekka.gamejam.boathell.asset.Music
import de.lostmekka.gamejam.boathell.entity.Ships
import de.lostmekka.gamejam.boathell.entity.system.*
import ktx.app.KtxScreen
import ktx.ashley.*
import ktx.box2d.createWorld

class GamePlayScreen : KtxScreen {
    val engine = Engine().apply {
        val physicsWorld = createWorld(Vector2.Zero, true)

        Ships.addPlayerBoat(this, physicsWorld)
        createOceanWater(this)

        addSystem(ShipMovementSystem())
        addSystem(WeaponSystem(physicsWorld))
        addSystem(WeaponOwnerSystem())
        addSystem(ProjectileMovementSystem())

        // controllers
        addSystem(PlayerControlledBoatSystem())
        addSystem(AIShipSystem())
        val cameraControl = CameraControl()
        addSystem(cameraControl)

        // special
        addSystem(WaterLayerSystem())
        addSystem(StupidWaterSpawn())
        addSystem(StupidParticleSystem())
        addSystem(RenderSystem(cameraControl.camera))
        addSystem(PhysicsUpdateSystem(physicsWorld))
        addSystem(EnemySpawnerSystem(physicsWorld))
        addSystem(PhysicsDebugRenderer(cameraControl.camera, physicsWorld))

        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun scrolled(amount: Int): Boolean {
                cameraControl.zoomBy(amount)
                return true
            }

            override fun keyDown(keycode: Int): Boolean {
                when (keycode) {
                    Input.Keys.ESCAPE -> Gdx.app.exit()
                }
                return false
            }
        }
    }

    override fun show() {
        // Music.loop.play()
    }

    override fun hide() {
        Music.loop.pause()
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        try {
            val render = engine.getSystem<RenderSystem>()
            render.viewport.update(width, height)
        } catch (err: MissingEntitySystemException) {
            // ignore
        }
    }
}
