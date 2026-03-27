package de.benkralex.socius.data.import_export.android_system.data

data class StructuredNameData(
    val prefix: String? = null,
    val givenName: String? = null,
    val middleName: String? = null,
    val familyName: String? = null,
    val suffix: String? = null,
    val phoneticGivenName: String? = null,
    val phoneticMiddleName: String? = null,
    val phoneticFamilyName: String? = null
)