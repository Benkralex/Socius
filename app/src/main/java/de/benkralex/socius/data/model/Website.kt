package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Website(
    var value: String = "",
    var type: Type.Website = Type.Website.HOMEPAGE,
)
