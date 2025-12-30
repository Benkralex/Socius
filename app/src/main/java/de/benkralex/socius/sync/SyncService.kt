package de.benkralex.socius.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Service that provides the SyncAdapter binder to the system.
 */
class SyncService : Service() {

    companion object {
        private val syncAdapterLock = Any()
        private var syncAdapter: ContactsSyncAdapter? = null
    }

    override fun onCreate() {
        super.onCreate()
        synchronized(syncAdapterLock) {
            if (syncAdapter == null) {
                syncAdapter = ContactsSyncAdapter(applicationContext, true)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return syncAdapter?.syncAdapterBinder
    }
}

