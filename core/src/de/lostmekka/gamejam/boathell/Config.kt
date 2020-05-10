package de.lostmekka.gamejam.boathell

object GameConfig {
    object Debug {
        const val drawPhysics = true
    }
    object Physics {
        const val stepsPerSecond = 30
        const val velocityIterations = 6
        const val positionIterations = 2
    }
}
