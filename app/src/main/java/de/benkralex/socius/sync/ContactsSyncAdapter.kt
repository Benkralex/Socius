package de.benkralex.socius.sync

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.ContentProviderOperation
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.contacts.local.database.LocalContactsDatabase
import de.benkralex.socius.data.settings.getFormattedName
import de.benkralex.socius.data.settings.noName
import kotlinx.coroutines.runBlocking

/**
 * SyncAdapter that syncs local contacts to the Android system contacts database.
 * The synced contacts are marked as read-only so that other apps can see them
 * but cannot modify them.
 */
class ContactsSyncAdapter(
    context: Context,
    autoInitialize: Boolean,
    allowParallelSyncs: Boolean = false
) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {

    companion object {
        private const val TAG = "ContactsSyncAdapter"
    }

    override fun onPerformSync(
        account: Account?,
        extras: Bundle?,
        authority: String?,
        provider: ContentProviderClient?,
        syncResult: SyncResult?
    ) {
        if (account == null) {
            Log.e(TAG, "Account is null, cannot sync")
            return
        }

        Log.d(TAG, "Starting sync for account: ${account.name}")

        try {
            // Get local contacts from Room database
            val localContacts = runBlocking {
                val database = LocalContactsDatabase.getDatabase(context)
                val dao = database.localContactsDao()
                dao.getAll().map { it.toContact(context) }
            }

            Log.d(TAG, "Found ${localContacts.size} local contacts to sync")

            // First, delete all existing contacts for this account
            deleteExistingContacts(account)

            // Then insert all local contacts
            for (contact in localContacts) {
                insertContact(account, contact)
            }

            Log.d(TAG, "Sync completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error during sync", e)
            syncResult?.stats?.numIoExceptions = (syncResult.stats?.numIoExceptions ?: 0) + 1
        }
    }

    private fun deleteExistingContacts(account: Account) {
        val contentResolver = context.contentResolver

        // Delete all raw contacts associated with this account
        val deleteCount = contentResolver.delete(
            ContactsContract.RawContacts.CONTENT_URI,
            "${ContactsContract.RawContacts.ACCOUNT_TYPE} = ? AND ${ContactsContract.RawContacts.ACCOUNT_NAME} = ?",
            arrayOf(SyncManager.ACCOUNT_TYPE, account.name)
        )

        Log.d(TAG, "Deleted $deleteCount existing contacts for account ${account.name}")
    }

    private fun insertContact(account: Account, contact: Contact) {
        val operations = ArrayList<ContentProviderOperation>()

        // Insert raw contact with read-only flag
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, SyncManager.ACCOUNT_TYPE)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, account.name)
                .withValue(ContactsContract.RawContacts.SOURCE_ID, contact.id)
                .withValue(ContactsContract.RawContacts.RAW_CONTACT_IS_READ_ONLY, 1)
                .build()
        )

        // Insert display name
        val displayName = contact.displayName ?: buildDisplayName(contact)
        if (!displayName.isNullOrBlank()) {
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.PREFIX, contact.prefix)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact.givenName)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME, contact.middleName)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, contact.familyName)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.SUFFIX, contact.suffix)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_GIVEN_NAME, contact.phoneticGivenName)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_MIDDLE_NAME, contact.phoneticMiddleName)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME, contact.phoneticFamilyName)
                    .build()
            )
        }

        // Insert nickname
        if (!contact.nickname.isNullOrBlank()) {
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Nickname.NAME, contact.nickname)
                    .build()
            )
        }

        // Insert phone numbers
        for (phone in contact.phoneNumbers) {
            val phoneType = mapPhoneType(phone.type)
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.number)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phoneType)
                    .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, phone.label)
                    .build()
            )
        }

        // Insert email addresses
        for (email in contact.emails) {
            val emailType = mapEmailType(email.type)
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, email.address)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, emailType)
                    .withValue(ContactsContract.CommonDataKinds.Email.LABEL, email.label)
                    .build()
            )
        }

        // Insert postal addresses
        for (address in contact.addresses) {
            val addressType = mapAddressType(address.type)
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, address.street)
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, address.city)
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.REGION, address.region)
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, address.postcode)
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, address.country)
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, addressType)
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.LABEL, address.label)
                    .build()
            )
        }

        // Insert organization
        if (!contact.organization.isNullOrBlank() || !contact.jobTitle.isNullOrBlank()) {
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, contact.organization)
                    .withValue(ContactsContract.CommonDataKinds.Organization.DEPARTMENT, contact.department)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, contact.jobTitle)
                    .build()
            )
        }

        // Insert websites
        for (website in contact.websites) {
            val websiteType = mapWebsiteType(website.type)
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Website.URL, website.url)
                    .withValue(ContactsContract.CommonDataKinds.Website.TYPE, websiteType)
                    .withValue(ContactsContract.CommonDataKinds.Website.LABEL, website.label)
                    .build()
            )
        }

        // Insert note
        if (!contact.note.isNullOrBlank()) {
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Note.NOTE, contact.note)
                    .build()
            )
        }

        // Insert relations
        for (relation in contact.relations) {
            val relationType = mapRelationType(relation.type)
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Relation.NAME, relation.name)
                    .withValue(ContactsContract.CommonDataKinds.Relation.TYPE, relationType)
                    .withValue(ContactsContract.CommonDataKinds.Relation.LABEL, relation.label)
                    .build()
            )
        }

        // Insert events (birthday, anniversary, etc.)
        for (event in contact.events) {
            val eventType = mapEventType(event.type)
            val dateString = buildEventDateString(event.year, event.month, event.day)
            if (dateString != null) {
                operations.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
                        )
                        .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, dateString)
                        .withValue(ContactsContract.CommonDataKinds.Event.TYPE, eventType)
                        .withValue(ContactsContract.CommonDataKinds.Event.LABEL, event.label)
                        .build()
                )
            }
        }

        // Apply all operations
        try {
            context.contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting contact ${contact.id}", e)
        }
    }

    private fun buildDisplayName(contact: Contact): String? {
        val formattedName = getFormattedName(contact)
        return if (formattedName.isBlank() || formattedName == noName) null else formattedName
    }

    private fun buildEventDateString(year: Int?, month: Int?, day: Int?): String? {
        if (month == null || day == null) return null

        return if (year != null) {
            String.format("%04d-%02d-%02d", year, month, day)
        } else {
            String.format("--%02d-%02d", month, day)
        }
    }

    private fun mapPhoneType(type: String): Int {
        return when (type.lowercase()) {
            "mobile" -> ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
            "home" -> ContactsContract.CommonDataKinds.Phone.TYPE_HOME
            "work" -> ContactsContract.CommonDataKinds.Phone.TYPE_WORK
            "fax_home" -> ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME
            "fax_work" -> ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK
            "pager" -> ContactsContract.CommonDataKinds.Phone.TYPE_PAGER
            "callback" -> ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK
            "car" -> ContactsContract.CommonDataKinds.Phone.TYPE_CAR
            "company_main" -> ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN
            "isdn" -> ContactsContract.CommonDataKinds.Phone.TYPE_ISDN
            "main" -> ContactsContract.CommonDataKinds.Phone.TYPE_MAIN
            "mms" -> ContactsContract.CommonDataKinds.Phone.TYPE_MMS
            "fax_other" -> ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX
            "radio" -> ContactsContract.CommonDataKinds.Phone.TYPE_RADIO
            "telex" -> ContactsContract.CommonDataKinds.Phone.TYPE_TELEX
            "tty_tdd" -> ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD
            "work_mobile" -> ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE
            "work_pager" -> ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER
            "assistant" -> ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT
            "custom" -> ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM
            else -> ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
        }
    }

    private fun mapEmailType(type: String): Int {
        return when (type.lowercase()) {
            "home" -> ContactsContract.CommonDataKinds.Email.TYPE_HOME
            "work" -> ContactsContract.CommonDataKinds.Email.TYPE_WORK
            "mobile" -> ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
            "custom" -> ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM
            else -> ContactsContract.CommonDataKinds.Email.TYPE_OTHER
        }
    }

    private fun mapAddressType(type: String): Int {
        return when (type.lowercase()) {
            "home" -> ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME
            "work" -> ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK
            "custom" -> ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM
            else -> ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER
        }
    }

    private fun mapWebsiteType(type: String): Int {
        return when (type.lowercase()) {
            "homepage" -> ContactsContract.CommonDataKinds.Website.TYPE_HOMEPAGE
            "blog" -> ContactsContract.CommonDataKinds.Website.TYPE_BLOG
            "profile" -> ContactsContract.CommonDataKinds.Website.TYPE_PROFILE
            "home" -> ContactsContract.CommonDataKinds.Website.TYPE_HOME
            "work" -> ContactsContract.CommonDataKinds.Website.TYPE_WORK
            "ftp" -> ContactsContract.CommonDataKinds.Website.TYPE_FTP
            "custom" -> ContactsContract.CommonDataKinds.Website.TYPE_CUSTOM
            else -> ContactsContract.CommonDataKinds.Website.TYPE_OTHER
        }
    }

    private fun mapRelationType(type: String): Int {
        return when (type.lowercase()) {
            "spouse" -> ContactsContract.CommonDataKinds.Relation.TYPE_SPOUSE
            "child" -> ContactsContract.CommonDataKinds.Relation.TYPE_CHILD
            "mother" -> ContactsContract.CommonDataKinds.Relation.TYPE_MOTHER
            "father" -> ContactsContract.CommonDataKinds.Relation.TYPE_FATHER
            "parent" -> ContactsContract.CommonDataKinds.Relation.TYPE_PARENT
            "brother" -> ContactsContract.CommonDataKinds.Relation.TYPE_BROTHER
            "sister" -> ContactsContract.CommonDataKinds.Relation.TYPE_SISTER
            "friend" -> ContactsContract.CommonDataKinds.Relation.TYPE_FRIEND
            "relative" -> ContactsContract.CommonDataKinds.Relation.TYPE_RELATIVE
            "domestic_partner" -> ContactsContract.CommonDataKinds.Relation.TYPE_DOMESTIC_PARTNER
            "manager" -> ContactsContract.CommonDataKinds.Relation.TYPE_MANAGER
            "assistant" -> ContactsContract.CommonDataKinds.Relation.TYPE_ASSISTANT
            "referred_by" -> ContactsContract.CommonDataKinds.Relation.TYPE_REFERRED_BY
            "partner" -> ContactsContract.CommonDataKinds.Relation.TYPE_PARTNER
            "custom" -> ContactsContract.CommonDataKinds.Relation.TYPE_CUSTOM
            else -> ContactsContract.CommonDataKinds.Relation.TYPE_CUSTOM
        }
    }

    private fun mapEventType(type: String): Int {
        return when (type.lowercase()) {
            "birthday" -> ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY
            "anniversary" -> ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY
            "custom" -> ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM
            else -> ContactsContract.CommonDataKinds.Event.TYPE_OTHER
        }
    }
}

