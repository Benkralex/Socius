package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Relation(
    var value: String = "",
    var type: Type.Relation = Type.Relation.OTHER,
)
