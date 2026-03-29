package de.benkralex.socius.data.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.benkralex.socius.R
import kotlinx.serialization.Serializable

class Type {
    @Serializable
    enum class Email {
        HOME,
        WORK,
        OTHER,
        CUSTOM;

        var label: String? = null
    }
    @Serializable
    enum class Address {
        HOME,
        WORK,
        OTHER,
        CUSTOM;

        var label: String? = null
    }
    @Serializable
    enum class Phone {
        HOME,
        WORK,
        MOBILE_HOME,
        MOBILE_WORK,
        FAX_HOME,
        FAX_WORK,
        PAGER_HOME,
        PAGER_WORK,
        OTHER,
        CUSTOM;

        var label: String? = null
    }
    @Serializable
    enum class Event {
        BIRTHDAY,
        ANNIVERSARY,
        OTHER,
        CUSTOM;

        var label: String? = null
    }
    @Serializable
    enum class Website {
        HOMEPAGE,
        BLOG,
        OTHER,
        CUSTOM;

        var label: String? = null
    }
    @Serializable
    enum class Relation {
        SPOUSE,
        CHILD,
        MOTHER,
        FATHER,
        PARENT,
        BROTHER,
        SISTER,
        FRIEND,
        RELATIVE,
        DOMESTIC_PARTNER,
        MANAGER,
        ASSISTANT,
        REFERRED_BY,
        PARTNER,
        OTHER,
        CUSTOM;

        var label: String? = null
    }

    companion object {
        /*
        * Convert String to Type
        * */
        fun convertEmailType(stringType: String): Email {
            return when (stringType.lowercase()) {
                "home" -> Email.HOME
                "work" -> Email.WORK
                "other" -> Email.OTHER
                else -> {
                    val type = Email.CUSTOM
                    type.label = stringType
                    type
                }
            }
        }

        fun convertAddressType(stringType: String): Address {
            return when (stringType.lowercase()) {
                "home" -> Address.HOME
                "work" -> Address.WORK
                "other" -> Address.OTHER
                else -> {
                    val type = Address.CUSTOM
                    type.label = stringType
                    type
                }
            }
        }

        fun convertWebsiteType(stringType: String): Website {
            return when (stringType.lowercase()) {
                "homepage" -> Website.HOMEPAGE
                "blog" -> Website.BLOG
                "other" -> Website.OTHER
                else -> {
                    val type = Website.CUSTOM
                    type.label = stringType
                    type
                }
            }
        }

        fun convertPhoneType(stringType: String): Phone {
            return when (stringType.lowercase()) {
                "home" -> Phone.HOME
                "work" -> Phone.WORK
                "other" -> Phone.OTHER
                "mobile" -> Phone.MOBILE_HOME
                "work_mobile" -> Phone.MOBILE_WORK
                "fax_home" -> Phone.FAX_HOME
                "fax_work" -> Phone.FAX_WORK
                "pager" -> Phone.PAGER_HOME
                "work_pager" -> Phone.PAGER_WORK
                else -> {
                    val type = Phone.CUSTOM
                    type.label = stringType
                    type
                }
            }
        }

        fun convertEventType(stringType: String): Event {
            return when (stringType.lowercase()) {
                "birthday" -> Event.BIRTHDAY
                "anniversary" -> Event.ANNIVERSARY
                "other" -> Event.OTHER
                else -> {
                    val type = Event.CUSTOM
                    type.label = stringType
                    type
                }
            }
        }

        fun convertRelationType(stringType: String): Relation {
            return when (stringType.lowercase()) {
                "spouse" -> Relation.SPOUSE
                "child" -> Relation.CHILD
                "mother" -> Relation.MOTHER
                "father" -> Relation.FATHER
                "parent" -> Relation.PARENT
                "brother" -> Relation.BROTHER
                "sister" -> Relation.SISTER
                "friend" -> Relation.FRIEND
                "relative" -> Relation.RELATIVE
                "domestic_partner" -> Relation.DOMESTIC_PARTNER
                "manager" -> Relation.MANAGER
                "assistant" -> Relation.ASSISTANT
                "referred_by" -> Relation.REFERRED_BY
                "partner" -> Relation.PARTNER
                "other" -> Relation.OTHER
                else -> {
                    val type = Relation.CUSTOM
                    type.label = stringType
                    type
                }
            }
        }

        /*
        * Convert Type to String
        * */
        fun convertToString(type: Email): String {
            return when (type) {
                Email.HOME -> "home"
                Email.WORK -> "work"
                Email.OTHER -> "other"
                Email.CUSTOM -> type.label ?: "custom"
            }
        }

        fun convertToString(type: Phone): String {
            return when (type) {
                Phone.HOME -> "home"
                Phone.WORK -> "work"
                Phone.MOBILE_HOME ->  "mobile"
                Phone.MOBILE_WORK -> "work_mobile"
                Phone.FAX_HOME -> "fax_home"
                Phone.FAX_WORK -> "fax_work"
                Phone.PAGER_HOME -> "pager"
                Phone.PAGER_WORK -> "work_pager"
                Phone.OTHER -> "other"
                Phone.CUSTOM -> type.label ?: "custom"
            }
        }

        fun convertToString(type: Address): String {
            return when (type) {
                Address.HOME -> "home"
                Address.WORK -> "work"
                Address.OTHER -> "other"
                Address.CUSTOM -> type.label ?: "custom"
            }
        }

        fun convertToString(type: Event): String {
            return when (type) {
                Event.BIRTHDAY -> "birthday"
                Event.ANNIVERSARY -> "anniversary"
                Event.OTHER -> "other"
                Event.CUSTOM -> type.label ?: "custom"
            }
        }

        fun convertToString(type: Website): String {
            return when (type) {
                Website.HOMEPAGE -> "homepage"
                Website.BLOG -> "blog"
                Website.OTHER -> "other"
                Website.CUSTOM -> type.label ?: "custom"
            }
        }

        fun convertToString(type: Relation): String {
            return when (type) {
                Relation.SPOUSE -> "spouse"
                Relation.CHILD -> "child"
                Relation.MOTHER -> "mother"
                Relation.FATHER -> "father"
                Relation.PARENT -> "parent"
                Relation.BROTHER -> "brother"
                Relation.SISTER -> "sister"
                Relation.FRIEND -> "friend"
                Relation.RELATIVE -> "relative"
                Relation.DOMESTIC_PARTNER -> "domestic_partner"
                Relation.MANAGER -> "manager"
                Relation.ASSISTANT -> "assistant"
                Relation.REFERRED_BY -> "referred_by"
                Relation.PARTNER -> "partner"
                Relation.OTHER -> "other"
                Relation.CUSTOM -> type.label ?: "custom"
            }
        }
        
        /*
        * Convert Type to StringRes id
        * */
        fun translateType(type: Email): Int {
            return when (type) {
                Email.HOME -> R.string.type_home
                Email.WORK -> R.string.type_work
                Email.OTHER -> R.string.type_other
                Email.CUSTOM -> R.string.type_custom
            }
        }

        fun translateType(type: Phone): Int {
            return when (type) {
                Phone.HOME -> R.string.type_home
                Phone.WORK -> R.string.type_work
                Phone.MOBILE_HOME -> R.string.type_mobile
                Phone.MOBILE_WORK -> R.string.type_work_mobile
                Phone.FAX_HOME -> R.string.type_fax_home
                Phone.FAX_WORK -> R.string.type_fax_work
                Phone.PAGER_HOME -> R.string.type_pager
                Phone.PAGER_WORK -> R.string.type_work_pager
                Phone.OTHER -> R.string.type_other
                Phone.CUSTOM -> R.string.type_custom
            }
        }

        fun translateType(type: Address): Int {
            return when (type) {
                Address.HOME -> R.string.type_home
                Address.WORK -> R.string.type_work
                Address.OTHER -> R.string.type_other
                Address.CUSTOM -> R.string.type_custom
            }
        }

        fun translateType(type: Event): Int {
            return when (type) {
                Event.BIRTHDAY -> R.string.type_birthday
                Event.ANNIVERSARY -> R.string.type_anniversary
                Event.OTHER -> R.string.type_other
                Event.CUSTOM -> R.string.type_custom
            }
        }

        fun translateType(type: Website): Int {
            return when (type) {
                Website.HOMEPAGE -> R.string.type_homepage
                Website.BLOG -> R.string.type_blog
                Website.OTHER -> R.string.type_other
                Website.CUSTOM -> R.string.type_custom
            }
        }

        fun translateType(type: Relation): Int {
            return when (type) {
                Relation.SPOUSE -> R.string.type_spouse
                Relation.CHILD -> R.string.type_child
                Relation.MOTHER -> R.string.type_mother
                Relation.FATHER -> R.string.type_father
                Relation.PARENT -> R.string.type_parent
                Relation.BROTHER -> R.string.type_brother
                Relation.SISTER -> R.string.type_sister
                Relation.FRIEND -> R.string.type_friend
                Relation.RELATIVE -> R.string.type_relative
                Relation.DOMESTIC_PARTNER -> R.string.type_domestic_partner
                Relation.MANAGER -> R.string.type_manager
                Relation.ASSISTANT -> R.string.type_assistant
                Relation.REFERRED_BY -> R.string.type_referred_by
                Relation.PARTNER -> R.string.type_partner
                Relation.OTHER -> R.string.type_other
                Relation.CUSTOM -> R.string.type_custom
            }
        }
    }
}