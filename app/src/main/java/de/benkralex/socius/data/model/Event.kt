package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    var day: Int,
    var month: Int,
    var year: Int? = null,
    var type: Type.Event = Type.Event.ANNIVERSARY,
)
