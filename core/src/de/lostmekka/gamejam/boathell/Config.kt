package de.lostmekka.gamejam.boathell

object GameConfig {
    object Debug {
        const val drawPhysics = false
    }
    object Physics {
        const val stepsPerSecond = 30
        const val velocityIterations = 6
        const val positionIterations = 2
    }
    object Player {
        const val turnSpeed = 130f
        const val acceleration = 0.1f
        const val deceleration = -0.1f
        const val friction = 0.01f
    }
}
