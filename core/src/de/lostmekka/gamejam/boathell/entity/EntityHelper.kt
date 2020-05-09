package de.lostmekka.gamejam.boathell.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity

fun entity(vararg components: Component) =
    Entity().apply { for (it in components) add(it) }
