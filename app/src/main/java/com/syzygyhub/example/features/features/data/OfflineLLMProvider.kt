package com.syzygyhub.example.features.features.data

import com.syzygy.ai.contracts.AIError
import com.syzygy.ai.contracts.llm.FinishReason
import com.syzygy.ai.contracts.llm.LLMChunk
import com.syzygy.ai.contracts.llm.LLMMessage
import com.syzygy.ai.contracts.llm.LLMProvider
import com.syzygy.ai.contracts.llm.LLMRequest
import com.syzygy.ai.contracts.llm.LLMResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * An offline [LLMProvider] for the example. It answers locally, needs no API key
 * and makes no network call. Replace it with a real provider to talk to a model.
 */
class OfflineLLMProvider : LLMProvider {
    private val providerName = "offline"

    override suspend fun complete(request: LLMRequest): LLMResponse {
        val prompt =
            request.messages
                .lastOrNull { it.role == LLMMessage.Role.USER }
                ?.content
                ?.trim()
        if (prompt.isNullOrEmpty()) {
            throw AIError.InvalidRequest("The prompt is empty.")
        }
        return LLMResponse(
            content = "Offline reply to: $prompt",
            finishReason = FinishReason.STOP,
            providerName = providerName,
            modelName = request.model,
        )
    }

    override fun stream(request: LLMRequest): Flow<LLMChunk> =
        flow {
            val response = complete(request)
            emit(LLMChunk(content = response.content, providerName = providerName, modelName = request.model))
            emit(LLMChunk(finishReason = FinishReason.STOP, providerName = providerName))
        }
}
