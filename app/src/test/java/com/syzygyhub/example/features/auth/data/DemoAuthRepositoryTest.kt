package com.syzygyhub.example.features.auth.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.syzygy.services.auth.JWTAuthProvider
import com.syzygyhub.example.di.AppModule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DemoAuthRepositoryTest {
    private lateinit var repository: DemoAuthRepository

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val appModule = AppModule(context)
        appModule.setup()
        val authProvider = runBlocking { appModule.container.resolve(JWTAuthProvider::class) }
        repository = DemoAuthRepository(authProvider)
    }

    @Test
    fun `valid demo login signs the user in`() =
        runBlocking {
            val user = repository.login(DemoCredentials.EMAIL, DemoCredentials.PASSWORD)

            assertEquals(DemoCredentials.EMAIL, user.email)
            assertTrue(repository.isLoggedIn())
        }

    @Test
    fun `wrong password is rejected and the session stays signed out`() =
        runBlocking {
            val result =
                runCatching {
                    repository.login(DemoCredentials.EMAIL, "not-the-password")
                }

            assertTrue(result.isFailure)
            assertFalse(repository.isLoggedIn())
        }

    @Test
    fun `logout clears the demo session`() =
        runBlocking {
            repository.login(DemoCredentials.EMAIL, DemoCredentials.PASSWORD)

            repository.logout()

            assertFalse(repository.isLoggedIn())
            assertNull(repository.currentUser())
        }
}
