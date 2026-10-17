package com.syzygyhub.example.features.features.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.syzygy.ai.contracts.AIError
import com.syzygy.ai.contracts.llm.LLMMessage
import com.syzygy.ai.contracts.llm.LLMProvider
import com.syzygy.ai.contracts.llm.LLMRequest
import com.syzygy.services.persistence.StorageTypeMismatchError
import com.syzygyhub.core.configuration.ConfigKey
import com.syzygyhub.core.configuration.ConfigRegistry
import com.syzygyhub.core.di.Container
import com.syzygyhub.core.di.resolve
import com.syzygyhub.core.featureflags.FeatureFlag
import com.syzygyhub.core.featureflags.InMemoryFeatureFlagProvider
import com.syzygyhub.example.features.features.data.OfflineLLMProvider
import com.syzygyhub.foundation.contracts.storage.StorageKey
import com.syzygyhub.foundation.contracts.storage.StorageProvider
import com.syzygyhub.foundation.primitives.id.SyzygyID
import com.syzygyhub.ui.android.components.buttons.PrimaryButton
import com.syzygyhub.ui.android.components.cards.CardView
import com.syzygyhub.ui.android.components.feedback.ProgressBar
import com.syzygyhub.ui.android.tokens.Spacing

private class DemoItem

/** A small demonstration of each Syzygy layer, using only public APIs and no keys. */
@Composable
fun FeaturesScreen(
    container: Container,
    onBack: () -> Unit = {},
) {
    var foundationId by remember { mutableStateOf("") }
    var coreResult by remember { mutableStateOf("") }
    var servicesResult by remember { mutableStateOf("") }
    var aiResult by remember { mutableStateOf("") }
    var progress by remember { mutableFloatStateOf(0f) }
    var runCount by remember { mutableStateOf(0) }

    LaunchedEffect(runCount) {
        progress = 0f

        // Foundation: a typed identifier.
        foundationId = SyzygyID.generate<DemoItem>().toString()
        progress = 0.2f

        // Core: a config value and a feature flag, each with a default.
        coreResult =
            runCatching {
                val registry = container.resolve<ConfigRegistry>()
                val flags = container.resolve<InMemoryFeatureFlagProvider>()
                val greeting = registry.get(ConfigKey("example.greeting", "Hello from Core"))
                val enabled = flags.value(FeatureFlag("example.showAIDemo", true, "Shows the AI demo"))
                "$greeting | flag: ${if (enabled) "on" else "off"}"
            }.getOrElse { "Core registry unavailable: ${it.message}" }
        progress = 0.4f

        // Services: storage write and read, handling the storage error type.
        servicesResult =
            try {
                val storage = container.resolve<StorageProvider>()
                val key = StorageKey<String>("example.lastRun")
                storage.set("run ${System.currentTimeMillis()}", key) { it }
                "Stored: ${storage.get(key) { it } ?: "nothing"}"
            } catch (error: StorageTypeMismatchError) {
                "Storage error: ${error.message}"
            } catch (error: Exception) {
                "Storage unavailable: ${error.message}"
            }
        progress = 0.6f

        // AI: one call through the LLMProvider contract with the offline provider.
        val provider: LLMProvider = OfflineLLMProvider()
        val request =
            LLMRequest(
                messages = listOf(LLMMessage(LLMMessage.Role.USER, "Hello Syzygy")),
                model = "offline-demo",
            )
        aiResult =
            try {
                val response = provider.complete(request)
                "${response.content} (${response.providerName ?: "unknown"})"
            } catch (error: AIError) {
                "AI error: ${error.message}"
            }
        progress = 1f
    }

    Scaffold { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(Spacing.lg)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            Text(text = "Features", style = MaterialTheme.typography.headlineMedium)
            LayerCard("Foundation", "SyzygyID.generate<T>()", foundationId)
            LayerCard("Core", "ConfigRegistry.get and FeatureFlag, each with a default", coreResult)
            LayerCard("Services", "StorageProvider.set and get, StorageTypeMismatchError", servicesResult)
            LayerCard("AI", "LLMProvider.complete with the offline provider", aiResult)
            CardView(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(Spacing.md)) {
                    Text(text = "UI", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "PrimaryButton, CardView and ProgressBar from syzygy-ui-android",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    ProgressBar(progress = progress, modifier = Modifier.fillMaxWidth())
                    PrimaryButton(text = "Run the layer demos", onClick = { runCount += 1 })
                }
            }
            TextButton(onClick = onBack) {
                Text(text = "Back")
            }
        }
    }
}

@Composable
private fun LayerCard(
    title: String,
    detail: String,
    result: String,
) {
    CardView(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(Spacing.md)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = detail, style = MaterialTheme.typography.bodySmall)
            Text(
                text = result.ifEmpty { "Not run yet" },
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
