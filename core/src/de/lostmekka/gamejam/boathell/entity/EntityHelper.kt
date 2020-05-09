package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity

fun Engine.addEntityWithComponents(vararg components: Component) =
    entityWithComponents(*components).also { addEntity(it) }

fun entityWithComponents(vararg components: Component) =
    Entity().apply { for (it in components) add(it) }
