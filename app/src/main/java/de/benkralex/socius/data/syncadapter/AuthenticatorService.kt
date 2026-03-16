package de.benkralex.socius.data.syncadapter

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Service that provides the authenticator binder to the system.
 */
class AuthenticatorService : Service() {

    private lateinit var authenticator: StubAuthenticator

    override fun onCreate() {
        super.onCreate()
        authenticator = StubAuthenticator(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return authenticator.iBinder
    }
}

