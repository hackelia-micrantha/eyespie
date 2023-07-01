package com.micrantha.bluebell.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/** Executes the use case asynchronously and returns a [Result].
 *
 * @return a [Result].
 *
 * @param coroutineDispatcher the dispatcher to dispatch the use case to.
 * @param block the block to execute.
 */
suspend fun <Out> dispatchUseCase(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    always: () -> Unit = {},
    block: suspend CoroutineScope.() -> Out
): Result<Out> {
    return try {
        withContext(coroutineDispatcher) {
            Result.success(block(this))
        }
    } catch (e: Exception) {
        Result.failure(e)
    } finally {
        always()
    }
}

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [Result<R>].
 * Handling an exception (emit [Result.Failure] to the result) is the subclasses responsibility.
 */
suspend fun <Out> flowUseCase(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    block: suspend () -> Flow<Result<Out>>
) = block().catch { e -> emit(Result.failure(Exception(e))) }
    .flowOn(coroutineDispatcher)
