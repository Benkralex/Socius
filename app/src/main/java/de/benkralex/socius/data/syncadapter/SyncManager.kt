package de.benkralex.socius.data.syncadapter

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log

/**
 * Manager class to handle account creation and sync operations.
 */
object SyncManager {

    private const val TAG = "SyncManager"

    /**
     * The account type for Socius contacts.
     * This must match the accountType in authenticator.xml
     */
    const val ACCOUNT_TYPE = "de.benkralex.socius.account"

    /**
     * The account name displayed in Android settings.
     */
    const val ACCOUNT_NAME = "Socius"

    /**
     * Gets or creates the Socius sync account.
     *
     * @param context The application context
     * @return The sync account, or null if creation failed
     */
    fun getOrCreateSyncAccount(context: Context): Account? {
        val accountManager = AccountManager.get(context)
        val account = Account(ACCOUNT_NAME, ACCOUNT_TYPE)

        // Check if account already exists
        val existingAccounts = accountManager.getAccountsByType(ACCOUNT_TYPE)
        if (existingAccounts.isNotEmpty()) {
            Log.d(TAG, "Sync account already exists")
            return existingAccounts[0]
        }

        // Create new account
        return try {
            val success = accountManager.addAccountExplicitly(account, null, null)
            if (success) {
                Log.d(TAG, "Successfully created sync account")

                // Enable sync for contacts
                ContentResolver.setIsSyncable(account, ContactsContract.AUTHORITY, 1)
                ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true)

                account
            } else {
                Log.e(TAG, "Failed to create sync account")
                null
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException while creating account", e)
            null
        }
    }

    /**
     * Requests an immediate sync of local contacts to the system.
     *
     * @param context The application context
     */
    fun requestSync(context: Context) {
        val account = getOrCreateSyncAccount(context) ?: run {
            Log.e(TAG, "Cannot request sync: no account available")
            return
        }

        val extras = Bundle().apply {
            putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
            putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
        }

        ContentResolver.requestSync(account, ContactsContract.AUTHORITY, extras)
        Log.d(TAG, "Sync requested")
    }

    /**
     * Checks if sync is enabled for the Socius account.
     *
     * @param context The application context
     * @return True if sync is enabled, false otherwise
     */
    /*fun isSyncEnabled(context: Context): Boolean {
        val account = getOrCreateSyncAccount(context) ?: return false
        return ContentResolver.getSyncAutomatically(account, ContactsContract.AUTHORITY)
    }

    *//**
     * Enables or disables automatic sync.
     *
     * @param context The application context
     * @param enabled Whether to enable automatic sync
     *//*
    fun setSyncEnabled(context: Context, enabled: Boolean) {
        val account = getOrCreateSyncAccount(context) ?: return
        ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, enabled)
        Log.d(TAG, "Sync enabled: $enabled")
    }

    *//**
     * Sets the periodic sync interval.
     *
     * @param context The application context
     * @param intervalSeconds The sync interval in seconds (minimum 60 seconds)
     *//*
    fun setPeriodicSync(context: Context, intervalSeconds: Long) {
        val account = getOrCreateSyncAccount(context) ?: return
        ContentResolver.addPeriodicSync(
            account,
            ContactsContract.AUTHORITY,
            Bundle.EMPTY,
            intervalSeconds.coerceAtLeast(60)
        )
        Log.d(TAG, "Periodic sync set to $intervalSeconds seconds")
    }

    *//**
     * Removes the periodic sync.
     *
     * @param context The application context
     *//*
    fun removePeriodicSync(context: Context) {
        val account = getOrCreateSyncAccount(context) ?: return
        ContentResolver.removePeriodicSync(account, ContactsContract.AUTHORITY, Bundle.EMPTY)
        Log.d(TAG, "Periodic sync removed")
    }

    *//**
     * Deletes all synced contacts from the system.
     *
     * @param context The application context
     *//*
    fun deleteAllSyncedContacts(context: Context) {
        val account = getOrCreateSyncAccount(context) ?: return

        try {
            val deleteCount = context.contentResolver.delete(
                ContactsContract.RawContacts.CONTENT_URI,
                "${ContactsContract.RawContacts.ACCOUNT_TYPE} = ? AND ${ContactsContract.RawContacts.ACCOUNT_NAME} = ?",
                arrayOf(ACCOUNT_TYPE, account.name)
            )
            Log.d(TAG, "Deleted $deleteCount synced contacts")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting synced contacts", e)
        }
    }

    *//**
     * Removes the Socius sync account completely.
     * This will also delete all contacts associated with the account.
     *
     * @param context The application context
     *//*
    fun removeSyncAccount(context: Context) {
        val accountManager = AccountManager.get(context)
        val accounts = accountManager.getAccountsByType(ACCOUNT_TYPE)

        for (account in accounts) {
            try {
                accountManager.removeAccountExplicitly(account)
                Log.d(TAG, "Removed sync account: ${account.name}")
            } catch (e: Exception) {
                Log.e(TAG, "Error removing sync account", e)
            }
        }
    }*/
}

