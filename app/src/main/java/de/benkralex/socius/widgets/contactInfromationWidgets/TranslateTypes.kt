package de.benkralex.socius.widgets.contactInfromationWidgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.benkralex.socius.R

@Composable
fun translateType(type: String?, label: String?): String {
    if (type.isNullOrBlank()) {
        return label ?: ""
    }
    return when (type) {
        "home" -> stringResource(R.string.type_home)
        "work" -> stringResource(R.string.type_work)
        "mobile" -> stringResource(R.string.type_mobile)
        "other" -> stringResource(R.string.type_other)
        "unknown" -> stringResource(R.string.type_unknown)
        "homepage" -> stringResource(R.string.type_homepage)
        "blog" -> stringResource(R.string.type_blog)
        "profile" -> stringResource(R.string.type_profile)
        "ftp" -> stringResource(R.string.type_ftp)
        "fax_home" -> stringResource(R.string.type_fax_home)
        "fax_work" -> stringResource(R.string.type_fax_work)
        "pager" -> stringResource(R.string.type_pager)
        "spouse" -> stringResource(R.string.type_spouse)
        "child" -> stringResource(R.string.type_child)
        "mother" -> stringResource(R.string.type_mother)
        "father" -> stringResource(R.string.type_father)
        "parent" -> stringResource(R.string.type_parent)
        "brother" -> stringResource(R.string.type_brother)
        "sister" -> stringResource(R.string.type_sister)
        "friend" -> stringResource(R.string.type_friend)
        "relative" -> stringResource(R.string.type_relative)
        "domestic_partner" -> stringResource(R.string.type_domestic_partner)
        "manager" -> stringResource(R.string.type_manager)
        "assistant" -> stringResource(R.string.type_assistant)
        "referred_by" -> stringResource(R.string.type_referred_by)
        "partner" -> stringResource(R.string.type_partner)
        "birthday" -> stringResource(R.string.type_birthday)
        "anniversary" -> stringResource(R.string.type_anniversary)
        "custom" -> label ?: stringResource(R.string.type_custom)
        "car" -> stringResource(R.string.type_car)
        "callback" -> stringResource(R.string.type_callback)
        "company_main" -> stringResource(R.string.type_company_main)
        "isdn" -> stringResource(R.string.type_isdn)
        "main" -> stringResource(R.string.type_main)
        "mms" -> stringResource(R.string.type_mms)
        "fax_other" -> stringResource(R.string.type_fax_other)
        "radio" -> stringResource(R.string.type_radio)
        "telex" -> stringResource(R.string.type_telex)
        "tty_tdd" -> stringResource(R.string.type_tty_tdd)
        "work_mobile" -> stringResource(R.string.type_work_mobile)
        "work_pager" -> stringResource(R.string.type_work_pager)
        else -> stringResource(R.string.type_unknown)
    }
}