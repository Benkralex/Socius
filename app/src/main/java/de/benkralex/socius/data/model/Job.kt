package de.benkralex.socius.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Job(
    var organization: String? = null,
    var department: String? = null,
    var jobTitle: String? = null,
)
