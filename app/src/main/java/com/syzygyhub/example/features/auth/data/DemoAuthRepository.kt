package com.syzygyhub.example.features.auth.data

import com.syzygy.services.auth.JWTAuthProvider
import com.syzygyhub.example.features.auth.domain.AuthRepositoryProtocol
import com.syzygyhub.example.features.auth.domain.AuthenticatedUser
import com.syzygyhub.foundation.contracts.auth.AuthState
import com.syzygyhub.foundation.contracts.auth.AuthToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Demo values only. These are not secrets and grant access to nothing but
 * this example app. Selected in `di/AppModule.kt` by `USE_DEMO_LOGIN`.
 */
object DemoCredentials {
    const val EMAIL = "demo@syzygy.example"
    const val PASSWORD = "demo-password"
}

/**
 * Offline login for an example app that is not connected to a backend.
 *
 * It accepts only [DemoCredentials], makes no network calls, and stores the
 * demo session through [JWTAuthProvider], the same provider [AuthRepository]
 * uses.
 */
class DemoAuthRepository(
    private val authProvider: JWTAuthProvider,
) : AuthRepositoryProtocol {
    override suspend fun login(
        email: String,
        password: String
    ): AuthenticatedUser =
        withContext(Dispatchers.IO) {
            val matches =
                email.trim().lowercase() == DemoCredentials.EMAIL &&
                    password == DemoCredentials.PASSWORD
            if (!matches) {
                throw IllegalArgumentException("Invalid email or password.")
            }
            authProvider.authenticate(
                AuthToken(
                    accessToken = "demo-access-token",
                    refreshToken = "demo-refresh-token",
                )
            )
            demoUser()
        }

    override suspend fun logout() =
        withContext(Dispatchers.IO) {
            authProvider.signOut()
        }

    override suspend fun currentUser(): AuthenticatedUser? =
        withContext(Dispatchers.IO) {
            if (authProvider.state.value is AuthState.Authenticated) demoUser() else null
        }

    override suspend fun isLoggedIn(): Boolean =
        withContext(Dispatchers.IO) {
            authProvider.state.value is AuthState.Authenticated
        }

    private fun demoUser() =
        AuthenticatedUser(
            id = "demo-user",
            email = DemoCredentials.EMAIL,
            displayName = "Demo User",
        )
}
