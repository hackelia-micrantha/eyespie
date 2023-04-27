package com.micrantha.bluebell.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/** Executes the use case asynchronously and returns an [Outcome].
 *
 * @return an [Outcome].
 *
 * @param coroutineDispatcher the dispatcher to dispatch the use case to.
 * @param block the block to execute.
 */
suspend fun <Out> dispatchUseCase(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    block: suspend () -> Out
): Result<Out> {
    return try {
        withContext(coroutineDispatcher) {
            Result.success(block())
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [Outcome<R>].
 * Handling an exception (emit [Outcome.Failure] to the result) is the subclasses responsibility.
 */
suspend fun <Out> flowUseCase(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    block: suspend () -> Flow<Result<Out>>
) = block().catch { e -> emit(Result.failure(Exception(e))) }
    .flowOn(coroutineDispatcher)
