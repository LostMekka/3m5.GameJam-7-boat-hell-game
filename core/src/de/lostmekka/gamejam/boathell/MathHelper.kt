package de.lostmekka.gamejam.boathell

import com.badlogic.gdx.math.MathUtils
import kotlin.math.PI

val Number.pixels get() = toFloat() / 32f

private const val pi = PI.toFloat()
private const val tau = PI.toFloat() * 2

fun Float.toDegrees() = this / pi * 180f
fun Float.toRadians() = this / 180f * pi

fun sinDeg(angleInDegrees: Float) = MathUtils.sinDeg(angleInDegrees)
fun cosDeg(angleInDegrees: Float) = MathUtils.cosDeg(angleInDegrees)

fun floorMod(subject: Float, divisor: Float) = ((subject % divisor) + divisor) % divisor

/**
 * Uses modulo to move this angle into the range [-pi..pi]
 */
fun normalizeAngleRad(angle: Float) =
    when {
        angle < -pi -> (angle - pi) % tau + pi
        angle > pi -> (angle + pi) % tau - pi
        else -> angle
    }

/**
 * Uses modulo to move this angle into the range [-180..180]
 */
fun normalizeAngleDeg(angle: Float) =
    when {
        angle < -180f -> (angle - 180f) % 360f + 180f
        angle > 180f -> (angle + 180f) % 360f - 180f
        else -> angle
    }
